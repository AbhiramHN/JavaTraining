package service;

import dao.EmployeeDAO;
import dao.LeaveBalanceDAO;
import dao.LeaveDAO;
import database.DBConnection;
import enums.Designation;
import enums.LeaveStatus;
import enums.LeaveType;
import model.Employee;
import model.Leave;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class LeaveApprovalService
{
    private final LeaveDAO leaveDAO;
    private final EmployeeDAO employeeDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;

    public LeaveApprovalService()
    {
        leaveDAO = new LeaveDAO();
        employeeDAO = new EmployeeDAO();
        leaveBalanceDAO = new LeaveBalanceDAO();
    }

    public ArrayList<Leave> getPendingLeaveRequests(Designation designation)
    {
        return leaveDAO.getPendingLeaveRequests(designation);
    }

    public ArrayList<Leave> getApprovedLeaveRequests(Designation designation)
    {
        return leaveDAO.getApprovedLeaveRequests(designation);
    }

    public boolean processLeave(Employee approver,
                                int leaveId,
                                LeaveStatus leaveStatus)
    {

        if(leaveStatus != LeaveStatus.APPROVED
                && leaveStatus != LeaveStatus.REJECTED)
        {
            return false;
        }

        if(approver == null)
        {
            return false;
        }

        Leave leave = leaveDAO.getLeaveById(leaveId);

        if(leave == null)
        {
            return false;
        }

        if(approver.getDesignation() == Designation.EXECUTIVE)
        {
            return false;
        }

        if(leave.getStatus() != LeaveStatus.PENDING)
        {
            return false;
        }

        if(approver.getEmployeeId().equals(leave.getEmployeeId()))
        {
            return false;
        }

        Employee employee = employeeDAO.getEmployeeById(leave.getEmployeeId());

        if(employee == null)
        {
            return false;
        }

        if(approver.getDesignation() == Designation.LEAD
                && employee.getDesignation() != Designation.EXECUTIVE)
        {
            return false;
        }

        Map<LeaveType, Integer> leaveBalance =
                leaveBalanceDAO.getLeaveBalance(employee.getEmployeeId());

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return false;
            }

            try
            {
                connection.setAutoCommit(false);

                boolean leaveUpdated =
                        leaveDAO.updateLeaveStatus(
                                connection,
                                leaveId,
                                leaveStatus,
                                approver.getEmployeeId(),
                                LocalDate.now());

                if(!leaveUpdated)
                {
                    connection.rollback();
                    return false;
                }

                if(leaveStatus == LeaveStatus.APPROVED)
                {
                    int currentBalance =
                            leaveBalance.get(leave.getLeaveType());

                    int newBalance =
                            currentBalance - leave.getNumberOfDays();

                    boolean balanceUpdated =
                            leaveBalanceDAO.updateLeaveBalance(
                                    connection,
                                    employee.getEmployeeId(),
                                    leave.getLeaveType(),
                                    newBalance);

                    if(!balanceUpdated)
                    {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();

                return true;
            }
            catch(SQLException exception)
            {
                try
                {
                    connection.rollback();
                }
                catch(SQLException rollbackException)
                {
                    rollbackException.printStackTrace();
                }

                exception.printStackTrace();
            }
            finally
            {
                connection.setAutoCommit(true);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public boolean revokeLeave(Employee employee, int leaveId)
    {
        if(employee == null)
        {
            return false;
        }

        Leave leave = leaveDAO.getLeaveById(leaveId);

        if(leave == null)
        {
            return false;
        }

        if(!leave.getEmployeeId().equals(employee.getEmployeeId()))
        {
            return false;
        }

        if(leave.getStatus() == LeaveStatus.REJECTED
                || leave.getStatus() == LeaveStatus.REVOKED)
        {
            return false;
        }

        Map<LeaveType, Integer> leaveBalance =
                leaveBalanceDAO.getLeaveBalance(employee.getEmployeeId());

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return false;
            }

            try
            {
                connection.setAutoCommit(false);

                boolean leaveUpdated =
                        leaveDAO.updateLeaveStatus(
                                connection,
                                leaveId,
                                LeaveStatus.REVOKED,
                                employee.getEmployeeId(),
                                LocalDate.now());

                if(!leaveUpdated)
                {
                    connection.rollback();
                    return false;
                }

                if(leave.getStatus() == LeaveStatus.APPROVED)
                {
                    int currentBalance =
                            leaveBalance.get(leave.getLeaveType());

                    int newBalance =
                            currentBalance + leave.getNumberOfDays();

                    boolean balanceUpdated =
                            leaveBalanceDAO.updateLeaveBalance(
                                    connection,
                                    employee.getEmployeeId(),
                                    leave.getLeaveType(),
                                    newBalance);

                    if(!balanceUpdated)
                    {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();

                return true;
            }
            catch(SQLException exception)
            {
                try
                {
                    connection.rollback();
                }
                catch(SQLException rollbackException)
                {
                    rollbackException.printStackTrace();
                }

                exception.printStackTrace();
            }
            finally
            {
                connection.setAutoCommit(true);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }
}