package service;

import dao.EmployeeDAO;
import dao.LeaveBalanceDAO;
import database.DBConnection;
import model.Employee;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeeService
{
    private final EmployeeDAO employeeDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;

    public EmployeeService()
    {
        employeeDAO = new EmployeeDAO();
        leaveBalanceDAO = new LeaveBalanceDAO();
    }

    public boolean registerEmployee(Employee employee)
    {
        Connection connection = DBConnection.getConnection();

        if(connection == null)
        {
            return false;
        }

        try
        {
            connection.setAutoCommit(false);
            String employeeId = employeeDAO.generateEmployeeId();

            employee.setEmployeeId(employeeId);
            employee.setJoiningDate(LocalDate.now());

            boolean employeeRegistered = employeeDAO.registerEmployee(connection, employee);

            if(!employeeRegistered)
            {
                connection.rollback();
                return false;
            }

            boolean leaveBalanceInitialized = leaveBalanceDAO.initializeLeaveBalance(
                            connection,
                            employeeId);

            if(!leaveBalanceInitialized)
            {
                connection.rollback();
                return false;
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
            try
            {
                connection.setAutoCommit(true);
                connection.close();
            }
            catch(SQLException exception)
            {
                exception.printStackTrace();
            }
        }

        return false;
    }
}