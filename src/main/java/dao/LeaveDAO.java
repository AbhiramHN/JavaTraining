package dao;

import database.DBConnection;
import enums.Designation;
import enums.LeaveStatus;
import enums.LeaveType;
import model.Leave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class LeaveDAO {

    public ArrayList<Leave> getApprovedLeaveRequests(Designation designation)
    {
        String sql;

        if(designation == Designation.LEAD)
        {
            sql =
                    """
                    SELECT lr.*
                    FROM leave_request lr
                    JOIN employee e
                    ON lr.employee_id = e.employee_id
                    WHERE lr.status = 'APPROVED'
                    AND e.designation = 'EXECUTIVE'
                    """;
        }
        else
        {
            sql =
                    """
                    SELECT lr.*
                    FROM leave_request lr
                    JOIN employee e
                    ON lr.employee_id = e.employee_id
                    WHERE lr.status = 'APPROVED'
                    AND e.designation IN ('EXECUTIVE','LEAD')
                    """;
        }

        ArrayList<Leave> approvedLeaves = new ArrayList<>();

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return approvedLeaves;
            }

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while(resultSet.next())
                    {
                        Leave leave = new Leave();

                        leave.setLeaveId(resultSet.getInt("leave_id"));
                        leave.setEmployeeId(resultSet.getString("employee_id"));
                        leave.setLeaveType(LeaveType.valueOf(
                                resultSet.getString("leave_type")));
                        leave.setFromDate(resultSet.getDate("from_date").toLocalDate());
                        leave.setToDate(resultSet.getDate("to_date").toLocalDate());
                        leave.setNumberOfDays(resultSet.getInt("number_of_days"));
                        leave.setReason(resultSet.getString("reason"));
                        leave.setStatus(LeaveStatus.valueOf(
                                resultSet.getString("status")));
                        leave.setRequestDate(resultSet.getDate("request_date").toLocalDate());

                        approvedLeaves.add(leave);
                    }
                }
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return approvedLeaves;
    }

    public boolean createLeaveRequest(Connection connection, Leave leave)
    {
        if(connection == null)
        {
            return false;
        }

        String sql =
                """
                INSERT INTO leave_request
                (
                    employee_id,
                    leave_type,
                    from_date,
                    to_date,
                    number_of_days,
                    reason,
                    status,
                    request_date
                )
                VALUES
                (
                    ?, ?, ?, ?, ?, ?, ?, ?
                )
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, leave.getEmployeeId());
            preparedStatement.setString(2, leave.getLeaveType().name());
            preparedStatement.setDate(3,
                    java.sql.Date.valueOf(leave.getFromDate()));
            preparedStatement.setDate(4,
                    java.sql.Date.valueOf(leave.getToDate()));
            preparedStatement.setInt(5, leave.getNumberOfDays());
            preparedStatement.setString(6, leave.getReason());
            preparedStatement.setString(7, leave.getStatus().name());
            preparedStatement.setDate(8,
                    java.sql.Date.valueOf(leave.getRequestDate()));

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public ArrayList<Leave> getLeaveRequests(String employeeId)
    {
        String sql =
                """
                SELECT *
                FROM leave_request
                WHERE employee_id = ?
                ORDER BY request_date DESC
                """;

        ArrayList<Leave> leaveRequests = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return leaveRequests;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, employeeId);

                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while(resultSet.next())
                    {
                        Leave leave = new Leave();

                        leave.setLeaveId(resultSet.getInt("leave_id"));
                        leave.setEmployeeId(resultSet.getString("employee_id"));
                        leave.setLeaveType(LeaveType.valueOf(
                                        resultSet.getString("leave_type")));
                        leave.setFromDate(resultSet.getDate("from_date").toLocalDate());
                        leave.setToDate(resultSet.getDate("to_date").toLocalDate());
                        leave.setNumberOfDays(resultSet.getInt("number_of_days"));
                        leave.setReason(resultSet.getString("reason"));
                        leave.setStatus(LeaveStatus.valueOf(
                                        resultSet.getString("status")));
                        leave.setRequestDate(resultSet.getDate("request_date").toLocalDate());

                        if(resultSet.getDate("approval_date") != null)
                        {
                            leave.setApprovalDate(resultSet.getDate("approval_date").toLocalDate());
                            leave.setApprovedBy(resultSet.getString("approved_by"));
                        }

                        leaveRequests.add(leave);
                    }
                }
            }
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return leaveRequests;
    }

    public ArrayList<Leave> getPendingLeaveRequests(Designation designation)
    {
        String sql;

        if(designation == Designation.LEAD)
        {
            sql =
                    """
                    SELECT lr.*
                    FROM leave_request lr
                    JOIN employee e
                    ON lr.employee_id = e.employee_id
                    WHERE lr.status = 'PENDING'
                    AND e.designation = 'EXECUTIVE'
                    """;
        }
        else
        {
            sql =
                    """
                    SELECT lr.*
                    FROM leave_request lr
                    JOIN employee e
                    ON lr.employee_id = e.employee_id
                    WHERE lr.status = 'PENDING'
                    AND e.designation IN ('EXECUTIVE','LEAD')
                    """;
        }

        ArrayList<Leave> pendingLeaves = new ArrayList<>();

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return pendingLeaves;
            }

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    while(resultSet.next())
                    {
                        Leave leave = new Leave();

                        leave.setLeaveId(resultSet.getInt("leave_id"));
                        leave.setEmployeeId(resultSet.getString("employee_id"));
                        leave.setLeaveType(LeaveType.valueOf(
                                        resultSet.getString("leave_type")));
                        leave.setFromDate(resultSet.getDate("from_date").toLocalDate());
                        leave.setToDate(resultSet.getDate("to_date").toLocalDate());
                        leave.setNumberOfDays(resultSet.getInt("number_of_days"));
                        leave.setReason(resultSet.getString("reason"));
                        leave.setStatus(LeaveStatus.valueOf(
                                        resultSet.getString("status")));
                        leave.setRequestDate(resultSet.getDate("request_date").toLocalDate());

                        pendingLeaves.add(leave);
                    }
                }
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return pendingLeaves;
    }
    public boolean updateLeaveStatus(Connection connection, int leaveId, LeaveStatus leaveStatus,
                                     String approvedBy, LocalDate approvalDate)
    {
        String sql =
                """
                UPDATE leave_request
                SET
                    status = ?,
                    approved_by = ?,
                    approval_date = ?
                WHERE leave_id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, leaveStatus.name());
            preparedStatement.setString(2, approvedBy);
            preparedStatement.setDate(3, java.sql.Date.valueOf(approvalDate));
            preparedStatement.setInt(4, leaveId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public Leave getLeaveById(int leaveId)
    {
        String sql =
                """
                SELECT *
                FROM leave_request
                WHERE leave_id = ?
                """;

        try(Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return null;
            }

            try(PreparedStatement preparedStatement =
                        connection.prepareStatement(sql))
            {
                preparedStatement.setInt(1, leaveId);

                try(ResultSet resultSet =
                            preparedStatement.executeQuery())
                {
                    if(!resultSet.next())
                    {
                        return null;
                    }

                    Leave leave = new Leave();

                    leave.setLeaveId(resultSet.getInt("leave_id"));
                    leave.setEmployeeId(resultSet.getString("employee_id"));
                    leave.setLeaveType(
                            LeaveType.valueOf(
                                    resultSet.getString("leave_type")));
                    leave.setFromDate(
                            resultSet.getDate("from_date").toLocalDate());
                    leave.setToDate(
                            resultSet.getDate("to_date").toLocalDate());
                    leave.setNumberOfDays(
                            resultSet.getInt("number_of_days"));
                    leave.setReason(
                            resultSet.getString("reason"));
                    leave.setStatus(
                            LeaveStatus.valueOf(
                                    resultSet.getString("status")));
                    leave.setRequestDate(
                            resultSet.getDate("request_date").toLocalDate());

                    if(resultSet.getDate("approval_date") != null)
                    {
                        leave.setApprovalDate(
                                resultSet.getDate("approval_date").toLocalDate());

                        leave.setApprovedBy(
                                resultSet.getString("approved_by"));
                    }

                    return leave;
                }
            }
        }
        catch(SQLException exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

}
