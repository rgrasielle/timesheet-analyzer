package com.rhobertta.timesheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;

public class ReportWriter {

    public static void writeTo(TimesheetReport report, String filePath) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode result = mapper.createObjectNode();

        result.put("totalMinutes", report.getTotalMinutes());

        ArrayNode tasksArray = mapper.createArrayNode();
        for (TaskSummary task : report.getTasks()) {
            ObjectNode taskNode = mapper.createObjectNode();
            taskNode.put("taskId", task.getTaskId());
            taskNode.put("taskName", task.getTaskName());
            taskNode.put("totalMinutes", task.getTotalMinutes());
            taskNode.put("percentage", task.getPercentage());
            tasksArray.add(taskNode);
        }
        result.set("tasks", tasksArray);

        TaskSummary mostWorked = report.getMostWorkedTask();
        ObjectNode mostWorkedNode = mapper.createObjectNode();
        mostWorkedNode.put("taskId", mostWorked.getTaskId());
        mostWorkedNode.put("taskName", mostWorked.getTaskName());
        mostWorkedNode.put("totalMinutes", mostWorked.getTotalMinutes());
        mostWorkedNode.put("percentage", mostWorked.getPercentage());
        result.set("mostWorkedTask", mostWorkedNode);

        ArrayNode top3TasksArray = mapper.createArrayNode();
        for (TaskSummary task : report.getTop3Tasks()) {
            ObjectNode taskNode = mapper.createObjectNode();
            taskNode.put("taskId", task.getTaskId());
            taskNode.put("taskName", task.getTaskName());
            taskNode.put("percentage", task.getPercentage());
            top3TasksArray.add(taskNode);
        }
        result.set("top3TasksPercentage", top3TasksArray);

        ArrayNode top3EmployeesArray = mapper.createArrayNode();
        for (EmployeeSummary employee : report.getTop3Employees()) {
            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("userId", employee.getUserId());
            employeeNode.put("userName", employee.getUserName());
            employeeNode.put("totalMinutes", employee.getTotalMinutes());
            top3EmployeesArray.add(employeeNode);
        }
        result.set("top3Employees", top3EmployeesArray);

        EmployeeSummary mostDistinct = report.getMostDistinctUser();
        ObjectNode mostDistinctNode = mapper.createObjectNode();
        mostDistinctNode.put("userId", mostDistinct.getUserId());
        mostDistinctNode.put("userName", mostDistinct.getUserName());
        mostDistinctNode.put("distinctTasks", mostDistinct.getDistinctTaskCount());

        ArrayNode taskIdsArray = mapper.createArrayNode();
        for (int taskId : mostDistinct.getTaskIds()) {
            taskIdsArray.add(taskId);
        }
        mostDistinctNode.set("taskIds", taskIdsArray);
        result.set("mostDistinctUserOnTasks", mostDistinctNode);

        result.put("ignoredRecords", report.getIgnoredRecords());

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(new File(filePath), result);

        System.out.println("result.json gerado com sucesso!");
    }
}