import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

class LMSMenu {

    private Scanner sc = new Scanner(System.in);
    private Employee currentUser = null;

    private HashMap<String, Employee> employeesHashMap = new HashMap<>();
    private LeaveRepository leaveRepository = new LeaveRepository();
    private LeaveManagementService leaveManagementService;

    private EmployeeService employeeService;

    public LMSMenu()
    {
        employeeService = new EmployeeService(
                sc,
                employeesHashMap,
                leaveRepository.getExecutivePendingLeaves(),
                leaveRepository.getLeadPendingLeaves()
        );

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
        System.out.println("9. Exit");
        System.out.println("Please Enter Number: ");
    }

    void runSwitchCase(int inputNumber) {

        try {
            switch (inputNumber) {
                case 1:
                    employeeService.registerEmployee();
                    break;
                case 2:
                    currentUser = employeeService.login();
                    break;
                case 3:
                    try
                    {
                        leaveManagementService.requestLeave(currentUser);
                    }
                    catch(InvalidLeaveRequestException e)
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

                    ((LeaveApprovalOperations) currentUser).approveLeave();
                    break;
                case 5:
                    if (currentUser == null) {
                        System.out.println("\nPlease Login First!\n");
                        break;
                    }

                    if(!(currentUser instanceof LeaveApprovalOperations))
                    {
                        throw new UnauthorizedOperationException("You are not authorized to revoke leaves.");
                    }

                    ((LeaveApprovalOperations) currentUser).revokeLeave();
                    break;

                case 6:
                    if(!(currentUser instanceof LeaveApprovalOperations))
                    {
                        throw new UnauthorizedOperationException("You are not authorized to approve leaves.");
                    }

                    employeeService.generateEmployeeList();
                    break;

                case 7:
                    leaveManagementService.viewLeaveRequest(currentUser);
                    break;
                case 8:
                    currentUser = employeeService.logout();
                    break;
                case 9:
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
        while(true) {
            printMenu();
            try
            {
                int inputNumber = sc.nextInt();
                sc.nextLine();

                runSwitchCase(inputNumber);
            }
            catch(InputMismatchException e) //UnChecked exception
            {
                System.out.println("Please enter a valid number.");

                sc.nextLine();
            }

        }
    }
}