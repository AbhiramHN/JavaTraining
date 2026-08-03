package service;

import dao.EmployeeDAO;
import dao.LeaveBalanceDAO;
import dao.LeaveDAO;
import model.Employee;

import java.util.ArrayList;
import java.util.Map;

import enums.LeaveType;
import model.Leave;

public class AuthenticationService
{
    private final EmployeeDAO employeeDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;
    private final LeaveDAO leaveDAO;

    public AuthenticationService()
    {
        employeeDAO = new EmployeeDAO();
        leaveBalanceDAO = new LeaveBalanceDAO();
        leaveDAO = new LeaveDAO();
    }

    public Employee login(String employeeId, String password)
    {
        Employee employee = employeeDAO.getEmployeeById(employeeId);

        if(employee == null)
        {
            return null;
        }

        if(!employee.getPassword().equals(password))
        {
            return null;
        }

        Map<LeaveType, Integer> leaveBalance = leaveBalanceDAO.getLeaveBalance(employeeId);
        employee.setLeaveBalance(leaveBalance);
        ArrayList<Leave> leaveRequests = leaveDAO.getLeaveRequests(employeeId);
        employee.setLeaveRequests(leaveRequests);

        return employee;
    }
}