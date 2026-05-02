package com.rhobertta.timesheet;

import java.util.*;

public class TimesheetAnalyzer {

    private static final int TOP_RANKING_SIZE = 3;

    public static TimesheetReport analyze(List<TimesheetRecord> records) {

        List<TimesheetRecord> validRecords = filterValidRecords(records);
        int ignoredRecords = records.size() - validRecords.size();

        Map<Integer, TaskSummary> taskMap = groupByTask(validRecords);
        Map<Integer, EmployeeSummary> employeeMap = groupByEmployee(validRecords);

        List<TaskSummary> sortedTasks = sortTasks(taskMap);
        List<EmployeeSummary> sortedEmployees = sortEmployees(employeeMap);

        int totalMinutes = calculateTotalMinutes(sortedTasks);
        calculatePercentages(sortedTasks, totalMinutes);

        if (sortedTasks.isEmpty()) {
            System.err.println("Erro: nenhum registro válido encontrado no arquivo.");
            System.exit(1);
        }

        if (sortedEmployees.isEmpty()) {
            System.err.println("Erro: nenhum funcionário encontrado no arquivo.");
            System.exit(1);
        }

        TaskSummary mostWorkedTask = sortedTasks.get(0);

        List<TaskSummary> top3Tasks = sortedTasks.subList(
                0, Math.min(TOP_RANKING_SIZE, sortedTasks.size())
        );
        List<EmployeeSummary> top3Employees = sortedEmployees.subList(
                0, Math.min(TOP_RANKING_SIZE, sortedEmployees.size())
        );

        EmployeeSummary mostDistinctUser = findMostDistinctUser(employeeMap);

        return new TimesheetReport(
                totalMinutes,
                sortedTasks,
                mostWorkedTask,
                top3Tasks,
                top3Employees,
                mostDistinctUser,
                ignoredRecords
        );
    }

    static List<TimesheetRecord> filterValidRecords(List<TimesheetRecord> records) {
        List<TimesheetRecord> valid = new ArrayList<>();
        for (TimesheetRecord record : records) {
            if (record.minutes() > 0) {
                valid.add(record);
            }
        }
        return valid;
    }

    static Map<Integer, TaskSummary> groupByTask(List<TimesheetRecord> records) {
        Map<Integer, TaskSummary> taskMap = new LinkedHashMap<>();
        for (TimesheetRecord record : records) {
            taskMap.computeIfAbsent(
                    record.taskId(),
                    k -> new TaskSummary(record.taskId(), record.taskName())
            );
            taskMap.get(record.taskId()).addMinutes(record.minutes());
        }
        return taskMap;
    }

    static Map<Integer, EmployeeSummary> groupByEmployee(List<TimesheetRecord> records) {
        Map<Integer, EmployeeSummary> employeeMap = new LinkedHashMap<>();
        for (TimesheetRecord record : records) {
            employeeMap.computeIfAbsent(
                    record.userId(),
                    k -> new EmployeeSummary(record.userId(), record.userName())
            );
            employeeMap.get(record.userId()).addMinutes(record.minutes());
            employeeMap.get(record.userId()).addTaskId(record.taskId());
        }
        return employeeMap;
    }

    static List<TaskSummary> sortTasks(Map<Integer, TaskSummary> taskMap) {
        List<TaskSummary> sorted = new ArrayList<>(taskMap.values());
        sorted.sort((a, b) -> {
            int cmp = b.getTotalMinutes() - a.getTotalMinutes();
            if (cmp != 0) return cmp;
            return a.getTaskId() - b.getTaskId();
        });
        return sorted;
    }

    static List<EmployeeSummary> sortEmployees(Map<Integer, EmployeeSummary> employeeMap) {
        List<EmployeeSummary> sorted = new ArrayList<>(employeeMap.values());
        sorted.sort((a, b) -> {
            int cmp = b.getTotalMinutes() - a.getTotalMinutes();
            if (cmp != 0) return cmp;
            return a.getUserId() - b.getUserId();
        });
        return sorted;
    }

    static int calculateTotalMinutes(List<TaskSummary> tasks) {
        return tasks.stream()
                .mapToInt(TaskSummary::getTotalMinutes)
                .sum();
    }

    static void calculatePercentages(List<TaskSummary> tasks, int totalMinutes) {
        for (TaskSummary task : tasks) {
            double percentage = (task.getTotalMinutes() * 100.0) / totalMinutes;
            task.setPercentage(String.format(Locale.US, "%.2f%%", percentage));
        }
    }

    static EmployeeSummary findMostDistinctUser(Map<Integer, EmployeeSummary> employeeMap) {
        return employeeMap.values().stream()
                .max(Comparator
                        .comparingInt(EmployeeSummary::getDistinctTaskCount)
                        .thenComparing(Comparator.comparingInt(EmployeeSummary::getUserId).reversed()))
                .orElseThrow();
    }
}