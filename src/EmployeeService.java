import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class EmployeeService
{
    private Scanner sc;
    private int employeeCounter;

    private HashMap<String, Employee> employeesHashMap;
    private ArrayList<Leave> executivePendingLeaves;
    private ArrayList<Leave> leadPendingLeaves;

    private Employee currentUser;

    public EmployeeService(Scanner sc, HashMap<String, Employee> employeesHashMap,
                           ArrayList<Leave> executivePendingLeaves,
                           ArrayList<Leave> leadPendingLeaves)
    {
        this.sc = sc;
        this.employeesHashMap = employeesHashMap;
        this.executivePendingLeaves = executivePendingLeaves;
        this.leadPendingLeaves = leadPendingLeaves;

        employeeCounter = 1;
        currentUser = null;
    }

    private String generateEmployeeId()
    {
        String employeeId = String.format("EMP%03d", employeeCounter);
        employeeCounter++;
        return employeeId;
    }

    private Employee createObject(int designation)
    {
        Employee object;
        if(designation == 1)
        {
            object = new Executive();
        }
        else if(designation == 2)
        {
            object = new Lead(executivePendingLeaves, employeesHashMap, sc);
        }
        else
        {
            object = new Manager(executivePendingLeaves, leadPendingLeaves, employeesHashMap, sc);
        }
        return object;
    }

    public void registerEmployee()
    {
        if(currentUser != null)
        {
            System.out.println("Already a User is Logged In. Please logout to register a new user");
            return;
        }

        System.out.println("Enter Your Name");
        String name = sc.nextLine();

        System.out.println("Press the number for your designation.");
        System.out.println("1. Executive\n2. Lead\n3. Manager");
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

        String employeeId = generateEmployeeId();
        Employee object = createObject(designation);

        employeesHashMap.put(employeeId, object);

        object.setName(name);
        object.setDesignation(designation);
        object.setEmpId(employeeId);
        object.setAge(age);
        object.setGender(gender);
        object.setPassword(password);
        object.setJoiningDate(joiningDate);

        System.out.println("\nRegistration is Successful!");
        System.out.println("Your Employee ID is : " + employeeId);
        System.out.println("Please remember your Employee ID for Login purpose\n");
    }

    public Employee login()
    {
        if(currentUser != null)
        {
            System.out.println("Already a User is LoggedIn.");
            return currentUser;
        }
        System.out.println("Enter Your Employee ID: ");
        String employeeId = sc.nextLine();

        System.out.println("Enter Your Password: ");
        String password = sc.nextLine();

        if(employeesHashMap.containsKey(employeeId))
        {
            Employee tempObject = employeesHashMap.get(employeeId);

            if(tempObject.getPassword().equals(password))
            {
                currentUser = tempObject;
                System.out.println("Login Successfull!");
            }
            else
            {
                System.out.println("Invalid Credentials. Try Again!");
            }
        }
        else
        {
            System.out.println("Employee ID Not Found");
        }
        return currentUser;
    }

    public Employee logout()
    {
        if(currentUser != null)
        {
            currentUser = null;
            System.out.println("\nLogout SuccesFull!\n");
        }
        else
        {
            System.out.println("\nNo user is currently logged in.\n");
        }
        return currentUser;
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

            if(employee.getDesignation() == 1){
                System.out.println("Designation : Executive");
            }
            else if (employee.getDesignation() == 2){
                System.out.println("Designation : Lead");
            }
            else{
                System.out.println("Designation : Manager");
            }

            System.out.println("Age         : " + employee.getAge());
            System.out.println("Gender      : " + employee.getGender());
            System.out.println("Joining Date: " + employee.getJoiningDate());
            System.out.println();
        }
    }

    public Employee getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser)
    {
        this.currentUser = currentUser;
    }
}