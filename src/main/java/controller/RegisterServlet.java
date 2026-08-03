package controller;

import enums.Designation;
import enums.Gender;
import factory.EmployeeFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import service.EmployeeService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet
{
    private EmployeeService employeeService;

    @Override
    public void init()
    {
        employeeService = new EmployeeService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String name = request.getParameter("name");

        Designation designation = Designation.valueOf(request.getParameter("designation"));

        int age = Integer.parseInt(request.getParameter("age"));

        Gender gender = Gender.valueOf(request.getParameter("gender"));

        String password = request.getParameter("password");

        EmployeeFactory employeeFactory = new EmployeeFactory();

        Employee employee = employeeFactory.createEmployee(designation);

        employee.setName(name);
        employee.setAge(age);
        employee.setGender(gender);
        employee.setPassword(password);

        boolean registered = employeeService.registerEmployee(employee);

        if(registered)
        {
            response.sendRedirect("pages/login.html");
        }
        else
        {
            response.sendRedirect("pages/register.html");
        }
    }
}