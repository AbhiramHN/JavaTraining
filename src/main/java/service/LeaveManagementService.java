package service;

import dao.LeaveDAO;
import database.DBConnection;
import model.Employee;
import model.Leave;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeaveManagementService
{
    private final LeaveDAO leaveDAO;
    private final LeaveValidationService leaveValidationService;

    public LeaveManagementService()
    {
        leaveDAO = new LeaveDAO();
        leaveValidationService = new LeaveValidationService();
    }

    public ArrayList<Leave> getLeaveHistory(String employeeId)
    {
        return leaveDAO.getLeaveRequests(employeeId);
    }

    public boolean requestLeave(Employee employee, Leave leave)
    {
        if(employee == null || leave == null)
        {
            return false;
        }

        if(!leaveValidationService.validate(employee, leave))
        {
            return false;
        }

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return false;
            }

            try
            {
                connection.setAutoCommit(false);

                boolean leaveCreated = leaveDAO.createLeaveRequest(connection, leave);

                if(!leaveCreated)
                {
                    connection.rollback();
                    return false;
                }

                connection.commit();

                employee.getLeaveRequests().add(leave);

                return true;
            }
            catch(SQLException exception)
            {
                connection.rollback();
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