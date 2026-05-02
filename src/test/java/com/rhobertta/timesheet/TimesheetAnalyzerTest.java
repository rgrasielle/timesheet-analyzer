package com.rhobertta.timesheet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class TimesheetAnalyzerTest {

    @Test
    void deveIgnorarRegistrosComMinutosZeroOuNegativo() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 60, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 102, "Tarefa B", "done", 0, "2026-01-01"),
                new TimesheetRecord(3, "Carla", 103, "Tarefa C", "done", -1, "2026-01-01")
        );

        List<TimesheetRecord> valid = TimesheetAnalyzer.filterValidRecords(records);

        assertEquals(1, valid.size());
        assertEquals(60, valid.get(0).minutes());
    }

    @Test
    void deveAgruparMinutosPorTarefa() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 10, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 101, "Tarefa A", "done", 20, "2026-01-01"),
                new TimesheetRecord(3, "Carla", 101, "Tarefa A", "done", 30, "2026-01-01")
        );

        Map<Integer, TaskSummary> taskMap = TimesheetAnalyzer.groupByTask(records);

        assertEquals(1, taskMap.size());
        assertEquals(60, taskMap.get(101).getTotalMinutes());
    }

    @Test
    void deveAgruparMinutosPorFuncionario() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 30, "2026-01-01"),
                new TimesheetRecord(1, "Ana", 102, "Tarefa B", "done", 20, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 101, "Tarefa A", "done", 10, "2026-01-01")
        );

        Map<Integer, EmployeeSummary> employeeMap = TimesheetAnalyzer.groupByEmployee(records);

        assertEquals(2, employeeMap.size());
        assertEquals(50, employeeMap.get(1).getTotalMinutes());
        assertEquals(10, employeeMap.get(2).getTotalMinutes());
    }

    @Test
    void deveOrdenarTarefasPorMinutosDescEDesempatarPorIdAsc() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 102, "Tarefa B", "done", 100, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 101, "Tarefa A", "done", 100, "2026-01-01"),
                new TimesheetRecord(3, "Carla", 103, "Tarefa C", "done", 50, "2026-01-01")
        );

        Map<Integer, TaskSummary> taskMap = TimesheetAnalyzer.groupByTask(records);
        List<TaskSummary> sorted = TimesheetAnalyzer.sortTasks(taskMap);

        assertEquals(101, sorted.get(0).getTaskId());
        assertEquals(102, sorted.get(1).getTaskId());
        assertEquals(103, sorted.get(2).getTaskId());
    }

    @Test
    void deveCalcularTotalDeMinutos() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 100, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 102, "Tarefa B", "done", 200, "2026-01-01")
        );

        Map<Integer, TaskSummary> taskMap = TimesheetAnalyzer.groupByTask(records);
        List<TaskSummary> sorted = TimesheetAnalyzer.sortTasks(taskMap);
        int total = TimesheetAnalyzer.calculateTotalMinutes(sorted);

        assertEquals(300, total);
    }

    @Test
    void deveCalcularPercentualComDuasCasasDecimais() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 1000, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 102, "Tarefa B", "done", 2000, "2026-01-01")
        );

        Map<Integer, TaskSummary> taskMap = TimesheetAnalyzer.groupByTask(records);
        List<TaskSummary> sorted = TimesheetAnalyzer.sortTasks(taskMap);
        TimesheetAnalyzer.calculatePercentages(sorted, 3000);

        assertEquals("66.67%", sorted.get(0).getPercentage());
        assertEquals("33.33%", sorted.get(1).getPercentage());
    }

    @Test
    void deveIdentificarUsuarioComMaisTarefasDistintas() {
        List<TimesheetRecord> records = List.of(
                new TimesheetRecord(1, "Ana", 101, "Tarefa A", "done", 10, "2026-01-01"),
                new TimesheetRecord(1, "Ana", 102, "Tarefa B", "done", 10, "2026-01-01"),
                new TimesheetRecord(1, "Ana", 103, "Tarefa C", "done", 10, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 101, "Tarefa A", "done", 10, "2026-01-01"),
                new TimesheetRecord(2, "Bruno", 101, "Tarefa A", "done", 10, "2026-01-01")
        );

        Map<Integer, EmployeeSummary> employeeMap = TimesheetAnalyzer.groupByEmployee(records);
        EmployeeSummary most = TimesheetAnalyzer.findMostDistinctUser(employeeMap);

        assertEquals(1, most.getUserId());
        assertEquals(3, most.getDistinctTaskCount());
    }

    // Teste de integração
    @Test
    void deveGerarResultadoIgualAoGabarito() throws Exception {
        List<TimesheetRecord> records = TimesheetReader.readFrom("data.json");
        TimesheetReport report = TimesheetAnalyzer.analyze(records);

        assertEquals(28408, report.getTotalMinutes());
        assertEquals(41, report.getIgnoredRecords());
        assertEquals(103, report.getMostWorkedTask().getTaskId());
        assertEquals(4047, report.getMostWorkedTask().getTotalMinutes());
        assertEquals(5, report.getTop3Employees().get(0).getUserId());
        assertEquals(1, report.getMostDistinctUser().getUserId());
        assertEquals(10, report.getMostDistinctUser().getDistinctTaskCount());
    }
}