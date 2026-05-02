package com.rhobertta.timesheet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TimesheetRecord(
        @JsonProperty("userId") int userId,
        @JsonProperty("userName") String userName,
        @JsonProperty("taskId") int taskId,
        @JsonProperty("taskName") String taskName,
        @JsonProperty("status") String status,
        @JsonProperty("minutes") int minutes,
        @JsonProperty("date") String date
) {}