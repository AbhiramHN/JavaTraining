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
public class DashboardController extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        Employee employee =
                (Employee) session.getAttribute("employee");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Dashboard</title>

                <link rel="stylesheet" href="css/common.css">
                <link rel="stylesheet" href="css/dashboard.css">
            </head>

            <body>
            <div class="dashboard-container">

            <div class="dashboard-header">

                <div>
                    <h1>Leave Management System</h1>
                </div>

                <div class="user-section">

                    <span class="user-name">
            """);

        out.println(employee.getName());

        out.println("""
                    </span>

                    <a class="header-logout"
                       href="logout">
                        Logout
                    </a>

                </div>

            </div>

            <div class="dashboard-grid">

            <div class="employee-card">

                <h2>Profile</h2>

                <div class="employee-details">
            """);

        out.println("""
                <div class="detail-item">
                    <span>Employee ID</span>
                    <strong>
            """
                + employee.getEmployeeId()
                + """
                    </strong>
                </div>
            """);

        out.println("""
                <div class="detail-item">
                    <span>Designation</span>
                    <strong>
            """
                + employee.getDesignation()
                + """
                    </strong>
                </div>
            """);

        out.println("""
                <div class="detail-item">
                    <span>Name</span>
                    <strong>
            """
                + employee.getName()
                + """
                    </strong>
                </div>
            """);

        out.println("""
                </div>

            </div>

            <div class="actions-card">

                <h2>Quick Actions</h2>

                <div class="action-grid">

                    <a href="pages/requestLeave.html">
                        Request Leave
                    </a>

                    <a href="leaveHistory">
                        Leave History
                    </a>
            """);

        if (employee.getDesignation() == Designation.LEAD
                || employee.getDesignation() == Designation.MANAGER) {

            out.println("""
                    <a href="approveLeave">
                        Approve Leave
                    </a>

                    <a href="generateReport">
                        Generate Report
                    </a>

                    <a href="revokeLeave">
                        Revoke Leave
                    </a>
                """);
        }

        out.println("""
                </div>

            </div>

            </div>

            </div>

            </body>

            </html>
            """);
    }
}