package controller;

import enums.Designation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.Leave;
import service.LeaveApprovalService;
import util.ToastUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/revokeLeave")
public class RevokeLeaveServlet extends HttpServlet
{
    private LeaveApprovalService leaveApprovalService;

    @Override
    public void init()
    {
        leaveApprovalService = new LeaveApprovalService();
    }

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

        Employee employee =
                (Employee) session.getAttribute("employee");

        if(employee == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        if(employee.getDesignation() == Designation.EXECUTIVE)
        {
            response.sendRedirect("dashboard");
            return;
        }

        ArrayList<Leave> approvedLeaves =
                leaveApprovalService.getApprovedLeaveRequests(
                        employee.getDesignation());

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Revoke Leave</title>");
        out.println("<link rel='stylesheet' href='css/common.css'>");
        out.println("<link rel='stylesheet' href='css/revokeLeave.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='revoke-container'>");
        out.println("<div class='revoke-card'>");

        out.println("<h1>Approved Leave Requests</h1>");

        out.println("<table border='1'>");

        out.println("<tr>");
        out.println("<th>Leave ID</th>");
        out.println("<th>Employee ID</th>");
        out.println("<th>Leave Type</th>");
        out.println("<th>From Date</th>");
        out.println("<th>To Date</th>");
        out.println("<th>Days</th>");
        out.println("<th>Reason</th>");
        out.println("<th>Revoke</th>");
        out.println("</tr>");

        for(Leave leave : approvedLeaves)
        {
            out.println("<tr>");

            out.println("<td>" + leave.getLeaveId() + "</td>");
            out.println("<td>" + leave.getEmployeeId() + "</td>");
            out.println("<td>" + leave.getLeaveType() + "</td>");
            out.println("<td>" + leave.getFromDate() + "</td>");
            out.println("<td>" + leave.getToDate() + "</td>");
            out.println("<td>" + leave.getNumberOfDays() + "</td>");
            out.println("<td>" + leave.getReason() + "</td>");

            out.println("<td>");
            out.println("<form class='action-form' action='revokeLeave' method='post'>");
            out.println("<input type='hidden' name='leaveId' value='" + leave.getLeaveId() + "'>");
            out.println("<input class='revoke-button' type='submit' value='Revoke'>");
            out.println("</form>");
            out.println("</td>");

            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<a class='back-button' href='dashboard'>Back to Dashboard</a>");

        out.println("</div>");
        out.println("</div>");
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

        Employee employee =
                (Employee) session.getAttribute("employee");

        if(employee == null)
        {
            response.sendRedirect("pages/login.html");
            return;
        }

        if(employee.getDesignation() == Designation.EXECUTIVE)
        {
            response.sendRedirect("dashboard");
            return;
        }

        int leaveId = Integer.parseInt(request.getParameter("leaveId"));

        boolean revoked = leaveApprovalService.revokeLeave(employee, leaveId);
        System.out.println("Revoked = " + revoked);
        PrintWriter out = response.getWriter();
        if(revoked)
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_OK,
                    "success", "Leave revoked successfully.", "revokeLeave");
        }
        else
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_BAD_REQUEST,
                    "error", "Unable to revoke leave.", "revokeLeave");
        }
    }
}