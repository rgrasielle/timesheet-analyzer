package com.rhobertta.timesheet;

public class TaskSummary {

    private final int taskId;
    private final String taskName;
    private int totalMinutes;
    private String percentage;

    public TaskSummary(int taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.totalMinutes = 0;
    }

    public void addMinutes(int minutes) {
        this.totalMinutes += minutes;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public int getTotalMinutes() { return totalMinutes; }
    public String getPercentage() { return percentage; }
}