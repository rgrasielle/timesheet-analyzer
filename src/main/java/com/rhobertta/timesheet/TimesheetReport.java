package com.rhobertta.timesheet;

import java.util.List;

public class TimesheetReport {

    private final int totalMinutes;
    private final List<TaskSummary> tasks;
    private final TaskSummary mostWorkedTask;
    private final List<TaskSummary> top3Tasks;
    private final List<EmployeeSummary> top3Employees;
    private final EmployeeSummary mostDistinctUser;
    private final int ignoredRecords;

    public TimesheetReport(int totalMinutes,
                           List<TaskSummary> tasks,
                           TaskSummary mostWorkedTask,
                           List<TaskSummary> top3Tasks,
                           List<EmployeeSummary> top3Employees,
                           EmployeeSummary mostDistinctUser,
                           int ignoredRecords) {
        this.totalMinutes = totalMinutes;
        this.tasks = tasks;
        this.mostWorkedTask = mostWorkedTask;
        this.top3Tasks = top3Tasks;
        this.top3Employees = top3Employees;
        this.mostDistinctUser = mostDistinctUser;
        this.ignoredRecords = ignoredRecords;
    }

    public int getTotalMinutes() { return totalMinutes; }
    public List<TaskSummary> getTasks() { return tasks; }
    public TaskSummary getMostWorkedTask() { return mostWorkedTask; }
    public List<TaskSummary> getTop3Tasks() { return top3Tasks; }
    public List<EmployeeSummary> getTop3Employees() { return top3Employees; }
    public EmployeeSummary getMostDistinctUser() { return mostDistinctUser; }
    public int getIgnoredRecords() { return ignoredRecords; }
}