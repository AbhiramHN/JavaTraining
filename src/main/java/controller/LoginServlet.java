package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import service.AuthenticationService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet
{
    private AuthenticationService authenticationService;

    @Override
    public void init()
    {
        authenticationService = new AuthenticationService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException
    {
        String employeeId = request.getParameter("employeeId");

        String password = request.getParameter("password");

        Employee employee = authenticationService.login(employeeId, password);

        if(employee != null)
        {
            HttpSession session = request.getSession();

            session.setAttribute("employee", employee);

            response.sendRedirect("dashboard");
        }
        else
        {
            response.sendRedirect("pages/login.html");
        }
    }
}