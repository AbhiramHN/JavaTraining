package dao;

import database.DBConnection;
import enums.Designation;
import enums.Gender;
import factory.EmployeeFactory;
import jakarta.servlet.ServletException;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO
{
    public boolean registerEmployee(Connection connection, Employee employee)
    {
        if(connection == null)
        {
            return false;
        }

        String sql =
                """
                INSERT INTO employee
                (
                    employee_id,
                    name,
                    designation,
                    age,
                    gender,
                    password,
                    joining_date
                )
                VALUES
                (
                    ?, ?, ?, ?, ?, ?, ?
                )
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getName());
            preparedStatement.setString(3, employee.getDesignation().name());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setString(5, employee.getGender().name());
            preparedStatement.setString(6, employee.getPassword());
            preparedStatement.setDate(7, java.sql.Date.valueOf(employee.getJoiningDate()));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public Employee getEmployeeById(String employeeId)
    {
        String sql =
                """
                SELECT *
                FROM employee
                WHERE employee_id = ?
                """;

        try( Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return null;
            }

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, employeeId);

                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(!resultSet.next())
                    {
                        return null;
                    }

                    EmployeeFactory employeeFactory = new EmployeeFactory();

                    Employee employee = employeeFactory.createEmployee(Designation.valueOf(
                                            resultSet.getString("designation")));

                    employee.setEmployeeId(resultSet.getString("employee_id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setAge(resultSet.getInt("age"));
                    employee.setGender(Gender.valueOf(resultSet.getString("gender")));
                    employee.setPassword(resultSet.getString("password"));
                    employee.setJoiningDate(resultSet.getDate("joining_date").toLocalDate());

                    return employee;
                }
            }
        }
        catch(SQLException  exception )
        {
            exception.printStackTrace();
        }

        return null;
    }

    public boolean employeeExists(String employeeId)
    {
        String sql =
                """
                SELECT employee_id
                FROM employee
                WHERE employee_id = ?
                """;

        try (Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return false;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, employeeId);
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    return resultSet.next();
                }
            }
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return false;
    }

    public String generateEmployeeId()
    {
        String sql =
                """
                SELECT employee_id
                FROM employee
                ORDER BY employee_id DESC
                LIMIT 1
                """;

        try (Connection connection = DBConnection.getConnection())
        {
            if(connection == null)
            {
                return null;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(!resultSet.next())
                    {
                        return "EMP0001";
                    }

                    String lastEmployeeId =resultSet.getString("employee_id");

                    int number =Integer.parseInt(lastEmployeeId.substring(3));
                    number++;
                    return String.format("EMP%04d", number);
                }
            }
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
        }

        return null;
    }
}