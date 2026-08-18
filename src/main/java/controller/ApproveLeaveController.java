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
import exception.UnauthorizedActionException;
import util.ToastUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/approveLeave")
public class ApproveLeaveController extends HttpServlet
{
    private LeaveApprovalService leaveApprovalService;

    @Override
    public void init()
    {
        leaveApprovalService = new LeaveApprovalService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Employee employee = (Employee) session.getAttribute("employee");


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
        out.println("<link rel='stylesheet' href='css/common.css'>");
        out.println("<link rel='stylesheet' href='css/approveLeave.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='approve-container'>");
        out.println("<div class='approve-card'>");

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
            out.println("<form class='action-form' action='approveLeave' method='post'>");
            out.println("<input type='hidden' name='leaveId' value='" + leave.getLeaveId() + "'>");
            out.println("<input type='hidden' name='action' value='APPROVED'>");
            out.println("<input class='approve-button' type='submit' value='Approve'>");
            out.println("</form>");
            out.println("</td>");

            out.println("<td>");
            out.println("<form class='action-form' action='approveLeave' method='post'>");
            out.println("<input type='hidden' name='leaveId' value='" + leave.getLeaveId() + "'>");
            out.println("<input type='hidden' name='action' value='REJECTED'>");
            out.println("<input class='reject-button' type='submit' value='Reject'>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        HttpSession session = request.getSession(false);
        Employee approver = (Employee) session.getAttribute("employee");

        if(approver.getDesignation() == Designation.EXECUTIVE)
        {
            response.sendRedirect("dashboard");
            return;
        }

        int leaveId = Integer.parseInt(request.getParameter("leaveId"));

        LeaveStatus leaveStatus = LeaveStatus.valueOf(request.getParameter("action"));

        PrintWriter out = response.getWriter();

        try
        {
            boolean processed = leaveApprovalService.processLeave(approver, leaveId, leaveStatus);

            if(processed)
            {
                String message;

                if(leaveStatus == LeaveStatus.APPROVED)
                {
                    message = "Leave approved successfully.";
                }
                else
                {
                    message = "Leave rejected successfully.";
                }

                ToastUtil.showToast(response, out, HttpServletResponse.SC_OK,
                        "success", message, "approveLeave");
            }
            else
            {
                ToastUtil.showToast(response, out, HttpServletResponse.SC_BAD_REQUEST,
                        "error", "Unable to process leave request.", "approveLeave");
            }
        }
        catch(UnauthorizedActionException exception)
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_FORBIDDEN,
                    "error", exception.getMessage(), "approveLeave");
        }
    }
}