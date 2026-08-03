package io;

import model.Employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class EmployeeCSVWriter
{
    public void saveEmployees(Map<String, Employee> employeesHashMap)
    {

        if (employeesHashMap.isEmpty())
        {
            System.out.println("No employees available to save.");
            return;
        }

        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(EmployeeCSVConstants.FILE_PATH));

            for (Employee employee : employeesHashMap.values())
            {
                bufferedWriter.write(employee.getEmpId() + EmployeeCSVConstants.DELIMITER
                                + employee.getName() + EmployeeCSVConstants.DELIMITER
                                + employee.getDesignation() + EmployeeCSVConstants.DELIMITER
                                + employee.getAge() + EmployeeCSVConstants.DELIMITER
                                + employee.getGender() + EmployeeCSVConstants.DELIMITER
                                + employee.getPassword() + EmployeeCSVConstants.DELIMITER
                                + employee.getJoiningDate()
                );
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            System.out.println("Employees saved successfully.");
        }
        catch (IOException e)
        {
            System.out.println("Unable to save employee data.");
        }
    }
}