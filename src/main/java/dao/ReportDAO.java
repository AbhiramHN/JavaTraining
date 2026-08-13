package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO
{
    public int getTotalEmployees()
    {
        String sql = "SELECT COUNT(*) FROM employee";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery())
        {
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return 0;
    }
    public int getPendingLeaveCount()
    {
        String sql =
                "SELECT COUNT(*) FROM leave_request WHERE status = 'PENDING'";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet =
                    preparedStatement.executeQuery())
        {
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return 0;
    }
    public int getApprovedLeaveCount()
    {
        String sql =
                "SELECT COUNT(*) FROM leave_request WHERE status = 'APPROVED'";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet =
                    preparedStatement.executeQuery())
        {
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return 0;
    }
    public int getRejectedLeaveCount()
    {
        String sql =
                "SELECT COUNT(*) FROM leave_request WHERE status = 'REJECTED'";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet =
                    preparedStatement.executeQuery())
        {
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return 0;
    }
    public int getRevokedLeaveCount()
    {
        String sql =
                "SELECT COUNT(*) FROM leave_request WHERE status = 'REVOKED'";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet =
                    preparedStatement.executeQuery())
        {
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return 0;
    }
}