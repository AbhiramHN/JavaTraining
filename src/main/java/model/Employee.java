package model;

import enums.Designation;
import enums.Gender;
import enums.LeaveType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public abstract class Employee
{
    private String employeeId;
    private String name;
    private Designation designation;
    private int age;
    private Gender gender;
    private String password;
    private LocalDate joiningDate;

    private Map<LeaveType, Integer> leaveBalance;
    private ArrayList<Leave> leaveRequests;

    public Employee()
    {
        leaveBalance = new EnumMap<>(LeaveType.class);
        leaveRequests = new ArrayList<>();
    }

    public String getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(String employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Designation getDesignation()
    {
        return designation;
    }

    public void setDesignation(Designation designation)
    {
        this.designation = designation;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public LocalDate getJoiningDate()
    {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate)
    {
        this.joiningDate = joiningDate;
    }

    public Map<LeaveType, Integer> getLeaveBalance()
    {
        return leaveBalance;
    }

    public void setLeaveBalance(Map<LeaveType, Integer> leaveBalance)
    {
        this.leaveBalance = leaveBalance;
    }

    public ArrayList<Leave> getLeaveRequests()
    {
        return leaveRequests;
    }

    public void setLeaveRequests(ArrayList<Leave> leaveRequests)
    {
        this.leaveRequests = leaveRequests;
    }
}