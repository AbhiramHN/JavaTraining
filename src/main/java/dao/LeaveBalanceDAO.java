package dao;

import database.DBConnection;
import enums.LeaveType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

public class LeaveBalanceDAO {

    public boolean initializeLeaveBalance(Connection connection, String employeeId)
    {
        String sql =
                """
                INSERT INTO leave_balance
                (
                    employee_id,
                    leave_type,
                    balance
                )
                VALUES
                (
                    ?, ?, ?
                )
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            for (LeaveType leaveType : LeaveType.values())
            {
                preparedStatement.setString(1, employeeId);
                preparedStatement.setString(2, leaveType.name());

                switch (leaveType)
                {
                    case CL:
                        preparedStatement.setInt(3, 12);
                        break;

//                    case EL:
//                        preparedStatement.setInt(3, 0);
//                        break;

                    case SL:
                        preparedStatement.setInt(3, 12);
                        break;

                    case ML:
                        preparedStatement.setInt(3, 180);
                        break;

                    case PL:
                        preparedStatement.setInt(3, 30);
                        break;
//
//                    case DL:
//                        preparedStatement.setInt(3, 0);
//                        break;

                    case LWP:
                        preparedStatement.setInt(3, 365);
                        break;
                }

                preparedStatement.addBatch();
            }

            int[] rowsAffected = preparedStatement.executeBatch();
            return rowsAffected.length == LeaveType.values().length;
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public Map<LeaveType, Integer> getLeaveBalance(String employeeId)
    {
        String sql =
                """
                SELECT leave_type, balance
                FROM leave_balance
                WHERE employee_id = ?
                """;

        Map<LeaveType, Integer> leaveBalance =new EnumMap<>(LeaveType.class);

        try (Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return leaveBalance;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, employeeId);
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while(resultSet.next())
                    {
                        LeaveType leaveType = LeaveType.valueOf(resultSet.getString("leave_type"));
                        int balance = resultSet.getInt("balance");
                        leaveBalance.put(leaveType, balance);
                    }
                }
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }
        return leaveBalance;
    }

    public boolean updateLeaveBalance(Connection connection, String employeeId,
                                      LeaveType leaveType, int balance)
    {
        String sql =
                """
                UPDATE leave_balance
                SET balance = ?
                WHERE employee_id = ?
                AND leave_type = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, balance);
            preparedStatement.setString(2, employeeId);
            preparedStatement.setString(3, leaveType.name());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }
}
