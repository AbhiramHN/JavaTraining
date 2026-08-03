package io;

import factory.EmployeeFactory;
import interfaces.LeaveApprovalOperations;
import model.Employee;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class EmployeeCSVLoader
{
    private CSVEmployeeReader csvEmployeeReader;
    private EmployeeFactory employeeFactory;
    private LeaveApprovalOperations leaveApprovalService;

    public EmployeeCSVLoader(LeaveApprovalOperations leaveApprovalService)
    {
        this.leaveApprovalService = leaveApprovalService;

        csvEmployeeReader = new CSVEmployeeReader();
        employeeFactory = new EmployeeFactory();
    }

    public void loadEmployees(Map<String, Employee> employeesHashMap)
    {
        ArrayList<String> employeeRecords;

        try
        {
            employeeRecords = csvEmployeeReader.readEmployeeData();
        }
        catch (IOException e)
        {
            System.out.println("Unable to read employee file.");
            return;
        }

        if (employeeRecords.isEmpty())   // check 1
        {
            System.out.println("Employee file is empty");
            return;
        }

        int addedEmployees = 0;
        int skippedEmployees = 0;

        for (String record : employeeRecords)
        {
            String[] data = record.split(EmployeeCSVConstants.DELIMITER);

            if (data.length != 7)   //check 2
            {
                skippedEmployees++;
                continue;
            }

            String employeeId = data[0];

            if (employeesHashMap.containsKey(employeeId))
            {
                skippedEmployees++;
                continue;
            }

            String name = data[1];
            int designation = Integer.parseInt(data[2]);
            int age = Integer.parseInt(data[3]);
            String gender = data[4];
            String password = data[5];
            LocalDate joiningDate = LocalDate.parse(data[6]);

            Employee employee = employeeFactory.createEmployee(designation, leaveApprovalService);

            employee.setEmpId(employeeId);
            employee.setName(name);
            employee.setDesignation(designation);
            employee.setAge(age);
            employee.setGender(gender);
            employee.setPassword(password);
            employee.setJoiningDate(joiningDate);

            employeesHashMap.put(employeeId, employee);

            addedEmployees++;
        }

        System.out.println();
        System.out.println("Employees Added   : " + addedEmployees);
        System.out.println("Employees Skipped : " + skippedEmployees);
        System.out.println();
    }
}