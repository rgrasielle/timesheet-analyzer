package com.rhobertta.timesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class TimesheetReader {

    public static List<TimesheetRecord> readFrom(String filePath) throws Exception {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException(
                    "Arquivo '" + filePath + "' não encontrado."
            );
        }

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(file, new TypeReference<List<TimesheetRecord>>() {});
    }
}