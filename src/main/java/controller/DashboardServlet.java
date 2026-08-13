package controller;

import enums.Designation;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        Employee employee = (Employee) session.getAttribute("employee");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dashboard</title>");
        out.println("<link rel='stylesheet' href='css/common.css'>");
        out.println("<link rel='stylesheet' href='css/dashboard.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='dashboard-container'>");
        out.println("<div class='dashboard-card'>");

        out.println("<h1>Leave Management System</h1>");

        out.println("<h2>Hello, " + employee.getName() + "</h2>");

        out.println("<div class='dashboard-info'>");

        out.println("<p><strong>Employee ID :</strong> " + employee.getEmployeeId()
                + "</p>");

        out.println("<p><strong>Designation :</strong> " + employee.getDesignation()
                + "</p>");

        out.println("</div>");

        out.println("<div class='dashboard-links'>");
        out.println("<a href='pages/requestLeave.html'>Request Leave</a>");

        out.println("<a href='leaveHistory'>View Leave History</a>");

        if(employee.getDesignation() == Designation.LEAD || employee.getDesignation() == Designation.MANAGER)
        {
            out.println("<a href='approveLeave'>Approve Leave</a>");
            out.println("<a href='revokeLeave'>Revoke Leave</a>");
            out.println("<a href='generateReport'>Generate Report</a>");


        }

        out.println("<a href='logout'>Logout</a>");

        out.println("</div>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}