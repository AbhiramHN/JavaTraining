package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);

        if(session == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employee");

        if(employee == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dashboard</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Leave Management System</h1>");

        out.println("<h2>Hello, "
                + employee.getName()
                + "</h2>");

        out.println("<p><strong>Employee ID:</strong> "
                + employee.getEmployeeId()
                + "</p>");

        out.println("<p><strong>Designation:</strong> "
                + employee.getDesignation()
                + "</p>");

        out.println("<hr>");

        out.println("<a href='pages/requestLeave.html'>Request Leave</a><br><br>");

        out.println("<a href='leaveHistory'>View Leave History</a><br><br>");

        if(employee.getDesignation().name().equals("LEAD")
                || employee.getDesignation().name().equals("MANAGER"))
        {
            out.println("<a href='approveLeave'>Approve Leave</a><br><br>");
            out.println("<a href='revokeLeave'>Revoke Leave</a><br><br>");
        }

        out.println("<a href='logout'>Logout</a>");

        out.println("</body>");
        out.println("</html>");
    }
}