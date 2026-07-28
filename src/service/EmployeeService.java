package service;

import factory.EmployeeFactory;
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

    public EmployeeService(Scanner sc, HashMap<String, Employee> employeesHashMap, LeaveApprovalOperations leaveApprovalService)
    {
        this.sc = sc;
        this.employeesHashMap = employeesHashMap;
        this.leaveApprovalService = leaveApprovalService;

        employeeFactory = new EmployeeFactory();
        employeeIdGenerator = new EmployeeIdGenerator();
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

        Employee employee = employeeFactory.createEmployee(designation, leaveApprovalService);

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