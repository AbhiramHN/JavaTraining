package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVEmployeeReader
{
    public ArrayList<String> readEmployeeData() throws IOException
    {
        ArrayList<String> employeeRecords = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(EmployeeCSVConstants.FILE_PATH));

        String line;

        while ((line = bufferedReader.readLine()) != null)
        {
            if (!line.trim().isEmpty())
            {
                employeeRecords.add(line);
            }
        }

        bufferedReader.close();

        return employeeRecords;
    }
}