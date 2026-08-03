package controller;

import enums.Designation;
import enums.LeaveStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.Leave;
import service.LeaveApprovalService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/approveLeave")
public class ApproveLeaveServlet extends HttpServlet
{
    private LeaveApprovalService leaveApprovalService;

    @Override
    public void init()
    {
        leaveApprovalService = new LeaveApprovalService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("pages/login.html");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            response.sendRedirect("pages/login.html");
            return;
        }

        if (employee.getDesignation() == Designation.EXECUTIVE) {
            response.sendRedirect("dashboard");
            return;
        }

        ArrayList<Leave> pendingLeaves = leaveApprovalService.getPendingLeaveRequests(employee.getDesignation());

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Approve Leave</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Pending Leave Requests</h1>");

        out.println("<table border='1'>");

        out.println("<tr>");
        out.println("<th>Leave ID</th>");
        out.println("<th>Employee ID</th>");
        out.println("<th>Leave Type</th>");
        out.println("<th>From Date</th>");
        out.println("<th>To Date</th>");
        out.println("<th>Days</th>");
        out.println("<th>Reason</th>");
        out.println("<th>Approve</th>");
        out.println("<th>Reject</th>");
        out.println("</tr>");

        for (Leave leave : pendingLeaves) {
            out.println("<tr>");

            out.println("<td>" + leave.getLeaveId() + "</td>");
            out.println("<td>" + leave.getEmployeeId() + "</td>");
            out.println("<td>" + leave.getLeaveType() + "</td>");
            out.println("<td>" + leave.getFromDate() + "</td>");
            out.println("<td>" + leave.getToDate() + "</td>");
            out.println("<td>" + leave.getNumberOfDays() + "</td>");
            out.println("<td>" + leave.getReason() + "</td>");

            out.println("<td>");
            out.println("<form action='approveLeave' method='post'>");
            out.println("<input type='hidden' name='leaveId' value='" + leave.getLeaveId() + "'>");
            out.println("<input type='hidden' name='action' value='APPROVED'>");
            out.println("<input type='submit' value='Approve'>");
            out.println("</form>");
            out.println("</td>");

            out.println("<td>");
            out.println("<form action='approveLeave' method='post'>");
            out.println("<input type='hidden' name='leaveId' value='" + leave.getLeaveId() + "'>");
            out.println("<input type='hidden' name='action' value='REJECTED'>");
            out.println("<input type='submit' value='Reject'>");
            out.println("</form>");
            out.println("</td>");

            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<br><br>");

        out.println("<a href='dashboard'>Back to Dashboard</a>");

        out.println("</body>");
        out.println("</html>");
    }


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);

        if(session == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        Employee approver = (Employee) session.getAttribute("employee");

        if(approver.getDesignation() == Designation.EXECUTIVE)
        {
            response.sendRedirect("dashboard");
            return;
        }

        if(approver == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        int leaveId = Integer.parseInt(request.getParameter("leaveId"));

        LeaveStatus leaveStatus = LeaveStatus.valueOf(request.getParameter("action"));

        boolean processed = leaveApprovalService.processLeave(approver, leaveId, leaveStatus);

        if(processed)
        {
            response.sendRedirect("approveLeave");
        }
        else
        {
            response.sendRedirect("approveLeave");
        }
    }
}