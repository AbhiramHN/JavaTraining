package controller;

import dao.LeaveDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.Leave;
import service.LeaveManagementService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/leaveHistory")
public class LeaveHistoryServlet extends HttpServlet
{
    private LeaveManagementService leaveManagementService;

    @Override
    public void init()
    {
        leaveManagementService = new LeaveManagementService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
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

        ArrayList<Leave> leaveRequests = leaveManagementService.getLeaveHistory(employee.getEmployeeId());

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Leave History</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Leave History</h1>");

        out.println("<table border='1'>");

        out.println("<tr>");
        out.println("<th>Leave ID</th>");
        out.println("<th>Leave Type</th>");
        out.println("<th>From Date</th>");
        out.println("<th>To Date</th>");
        out.println("<th>Days</th>");
        out.println("<th>Status</th>");
        out.println("</tr>");

        for(Leave leave : leaveRequests)
        {
            out.println("<tr>");

            out.println("<td>" + leave.getLeaveId() + "</td>");
            out.println("<td>" + leave.getLeaveType() + "</td>");
            out.println("<td>" + leave.getFromDate() + "</td>");
            out.println("<td>" + leave.getToDate() + "</td>");
            out.println("<td>" + leave.getNumberOfDays() + "</td>");
            out.println("<td>" + leave.getStatus() + "</td>");

            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<br><br>");

        out.println("<a href='dashboard'>Back to Dashboard</a>");

        out.println("</body>");
        out.println("</html>");
    }
}