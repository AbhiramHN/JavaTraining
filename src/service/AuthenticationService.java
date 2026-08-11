package service;

import model.Employee;

import java.util.HashMap;
import java.util.Scanner;

public class AuthenticationService
{
    private Scanner sc;
    private HashMap<String, Employee> employeesHashMap;
    private Employee currentUser;

    public AuthenticationService(Scanner sc,
                                 HashMap<String, Employee> employeesHashMap)
    {
        this.sc = sc;
        this.employeesHashMap = employeesHashMap;
        this.currentUser = null;
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

    public Employee getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser)
    {
        this.currentUser = currentUser;
    }
}