package com.rhobertta.timesheet;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<TimesheetRecord> records = TimesheetReader.readFrom("data.json");
            TimesheetReport report = TimesheetAnalyzer.analyze(records);
            ReportWriter.writeTo(report, "result.json");
        } catch (FileNotFoundException e) {
            System.err.println("Erro: arquivo data.json não encontrado.");
            System.exit(1);
        } catch (JsonProcessingException e) {
            System.err.println("Erro: o arquivo data.json contém JSON inválido.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            System.exit(1);
        }
    }
}