package service;

import model.Employee;

import java.util.HashMap;

public class EmployeeReportService
{
    private HashMap<String, Employee> employeesHashMap;

    public EmployeeReportService(HashMap<String, Employee> employeesHashMap)
    {
        this.employeesHashMap = employeesHashMap;
    }

    public void generateEmployeeList()
    {
        if(employeesHashMap.isEmpty())
        {
            System.out.println("\nNo Employees Found.\n");
            return;
        }

        System.out.println("\n===== EMPLOYEE LIST =====\n");

        for(Employee employee : employeesHashMap.values())
        {
            System.out.println("Employee ID : " + employee.getEmpId());
            System.out.println("Name        : " + employee.getName());

            if(employee.getDesignation() == 1)
            {
                System.out.println("Designation : Executive");
            }
            else if(employee.getDesignation() == 2)
            {
                System.out.println("Designation : Lead");
            }
            else
            {
                System.out.println("Designation : Manager");
            }

            System.out.println("Age         : " + employee.getAge());
            System.out.println("Gender      : " + employee.getGender());
            System.out.println("Joining Date: " + employee.getJoiningDate());
            System.out.println();
        }
    }
}