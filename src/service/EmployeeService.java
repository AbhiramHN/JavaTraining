package service;

import factory.EmployeeFactory;
import io.EmployeeCSVLoader;
import io.EmployeeCSVWriter;
import model.Employee;
import util.EmployeeIdGenerator;

import java.time.LocalDate;
import interfaces.LeaveApprovalOperations;
import java.util.HashMap;
import java.util.Scanner;

public class EmployeeService
{
    private Scanner sc;

    private HashMap<String, Employee> employeesHashMap;
    private LeaveApprovalOperations leaveApprovalService;

    private EmployeeFactory employeeFactory;
    private EmployeeIdGenerator employeeIdGenerator;
    private EmployeeCSVLoader employeeCSVLoader;
    private EmployeeCSVWriter employeeCSVWriter;

    public EmployeeService(Scanner sc, HashMap<String, Employee> employeesHashMap, LeaveApprovalOperations leaveApprovalService)
    {
        this.sc = sc;
        this.employeesHashMap = employeesHashMap;
        this.leaveApprovalService = leaveApprovalService;

        employeeFactory = new EmployeeFactory();
        employeeIdGenerator = new EmployeeIdGenerator();
        employeeCSVLoader = new EmployeeCSVLoader(leaveApprovalService);
        employeeCSVWriter = new EmployeeCSVWriter();
    }

    public void loadEmployeesFromCSV()
    {
        employeeCSVLoader.loadEmployees(employeesHashMap);

        updateEmployeeCounter();
    }

    public void saveEmployeesToCSV()
    {
        employeeCSVWriter.saveEmployees(employeesHashMap);
    }
    private void updateEmployeeCounter()
    {
        int highestEmployeeNumber = 0;

        for (Employee employee : employeesHashMap.values())
        {
            String employeeId = employee.getEmpId();

            int currentNumber =
                    Integer.parseInt(employeeId.substring(3));

            if (currentNumber > highestEmployeeNumber)
            {
                highestEmployeeNumber = currentNumber;
            }
        }

        employeeIdGenerator.setEmployeeCounter(highestEmployeeNumber + 1);
    }

    public void registerEmployee()
    {
        System.out.println("Enter Your Name");
        String name = sc.nextLine();

        System.out.println("Press the number for your designation.");
        System.out.println("1. Executive");
        System.out.println("2. Lead");
        System.out.println("3. Manager");
        int designation = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter your gender");
        String gender = sc.nextLine().toUpperCase();

        System.out.println("Enter your age");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter the new password");
        String password = sc.nextLine();

        LocalDate joiningDate = LocalDate.now();

        String employeeId = employeeIdGenerator.generateEmployeeId();

        Employee employee = employeeFactory.createEmployee(designation, leaveApprovalService); // runtime polymorphism

        employee.setName(name);
        employee.setDesignation(designation);
        employee.setEmpId(employeeId);
        employee.setAge(age);
        employee.setGender(gender);
        employee.setPassword(password);
        employee.setJoiningDate(joiningDate);

        employeesHashMap.put(employeeId, employee);

        System.out.println("\nRegistration is Successful!");
        System.out.println("Your Employee ID is : " + employeeId);
        System.out.println("Please remember your Employee ID for Login purpose\n");
    }
}