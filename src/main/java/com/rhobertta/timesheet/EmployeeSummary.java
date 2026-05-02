package com.rhobertta.timesheet;

import java.util.Set;
import java.util.TreeSet;

public class EmployeeSummary {

    private final int userId;
    private final String userName;
    private int totalMinutes;
    private final Set<Integer> taskIds;

    public EmployeeSummary(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.totalMinutes = 0;
        this.taskIds = new TreeSet<>();
    }

    public void addMinutes(int minutes) {
        this.totalMinutes += minutes;
    }

    public void addTaskId(int taskId) {
        this.taskIds.add(taskId);
    }

    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getTotalMinutes() { return totalMinutes; }
    public Set<Integer> getTaskIds() { return taskIds; }
    public int getDistinctTaskCount() { return taskIds.size(); }
}