package app;

import exception.InvalidLeaveRequestException;
import exception.UnauthorizedOperationException;
import interfaces.LeaveApprovalOperations;
import model.Employee;
import model.Manager;
import repository.LeaveRepository;
import service.AuthenticationService;
import service.EmployeeReportService;
import service.EmployeeService;
import service.LeaveManagementService;
import service.LeaveApprovalService;
import model.Lead;
import model.Leave;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import memory.MemoryUsageService;
import memory.MemoryStressService;

class LMSMenu {

    private Scanner sc = new Scanner(System.in);
    private Employee currentUser = null;

    private HashMap<String, Employee> employeesHashMap = new HashMap<>();
    private LeaveRepository leaveRepository = new LeaveRepository();
    private LeaveManagementService leaveManagementService;

    private EmployeeService employeeService;
    private AuthenticationService authenticationService;
    private EmployeeReportService employeeReportService;
    private LeaveApprovalOperations leaveApprovalService;
    MemoryUsageService memoryUsageService = new MemoryUsageService();
    MemoryStressService memoryStressService = new MemoryStressService();

    public LMSMenu()
    {
        leaveApprovalService = new LeaveApprovalService(leaveRepository);
        employeeService = new EmployeeService(sc, employeesHashMap, leaveApprovalService);

        authenticationService = new AuthenticationService(sc, employeesHashMap);

        employeeReportService = new EmployeeReportService(employeesHashMap);

        leaveManagementService = new LeaveManagementService(
                sc,
                leaveRepository.getExecutivePendingLeaves(),
                leaveRepository.getLeadPendingLeaves(),
                leaveRepository.getManagerLeaves()
        );

    }

    void printMenu() {
        System.out.println("\n\nSelect the option: ");
        System.out.println("1. Register New Employee");
        System.out.println("2. Login");
        System.out.println("3. Request Leave");
        System.out.println("4. Approve Leave");
        System.out.println("5. Revoke Leave");
        System.out.println("6. Generate All Employee List");
        System.out.println("7. View Leave Request");
        System.out.println("8. Logout");
        System.out.println("9. Display Current JVM Memory Usage");
        System.out.println("10. Simulate Memory Exhaustion");
        System.out.println("11. Load Employees From CSV");
        System.out.println("12. Save Employees To CSV");
        System.out.println("13. Exit");
        System.out.println("Please Enter Number: ");
    }

    void runSwitchCase(int inputNumber) {

        try {
            switch (inputNumber) {
                case 1:
                    employeeService.registerEmployee();
                    break;

                case 2:
                    currentUser = authenticationService.login();           //polymorphism
                    break;

                case 3:
                    try
                    {
                        leaveManagementService.requestLeave(currentUser);
                    }
                    catch (InvalidLeaveRequestException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    if (currentUser == null) {
                        System.out.println("\nPlease Login First!\n");
                        break;
                    }

                    if (!(currentUser instanceof LeaveApprovalOperations)) {
                        throw new UnauthorizedOperationException("You are not authorized to approve leaves.");
                    }

                    if (currentUser instanceof Lead)
                    {
                        ((LeaveApprovalOperations) currentUser).approveLeave(leaveRepository.getExecutivePendingLeaves(), employeesHashMap, sc);
                    }
                    else if (currentUser instanceof Manager)
                    {
                        ArrayList<Leave> pendingLeaves = new ArrayList<>();

                        pendingLeaves.addAll(leaveRepository.getExecutivePendingLeaves());
                        pendingLeaves.addAll(leaveRepository.getLeadPendingLeaves());

                        ((LeaveApprovalOperations) currentUser).approveLeave(
                                pendingLeaves,
                                employeesHashMap,
                                sc);
                    }
                    break;

                case 5:
                    if (currentUser == null) {
                        System.out.println("\nPlease Login First!\n");
                        break;
                    }

                    if (!(currentUser instanceof LeaveApprovalOperations)) {
                        throw new UnauthorizedOperationException("You are not authorized to revoke leaves.");
                    }

                    ((LeaveApprovalOperations) currentUser).revokeLeave(employeesHashMap, sc);
                    break;

                case 6:
                    if (!(currentUser instanceof Manager)) {
                        throw new UnauthorizedOperationException("Only Manager can generate employee list.");
                    }

                    employeeReportService.generateEmployeeList();
                    break;

                case 7:
                    leaveManagementService.viewLeaveRequest(currentUser);
                    break;

                case 8:
                    currentUser = authenticationService.logout();
                    break;

                case 9:
                    memoryUsageService.displayMemoryUsage();
                    break;

                case 10:
                    memoryStressService.simulateMemoryExhaustion();
                    break;

                case 11 :
                    employeeService.loadEmployeesFromCSV();
                    break;

                case 12:
                    employeeService.saveEmployeesToCSV();
                    break;

                case 13:
                    System.out.println("Exiting LMS...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
        catch (UnauthorizedOperationException e)
        {
            System.out.println(e.getMessage());
        }
    }

    void start() {
        while (true) {
            printMenu();

            try
            {
                int inputNumber = sc.nextInt();
                sc.nextLine();

                runSwitchCase(inputNumber);
            }
            catch (InputMismatchException e)
            {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
            }
        }
    }
}