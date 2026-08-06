package controller;

import enums.LeaveStatus;
import enums.LeaveType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import model.Leave;
import service.LeaveManagementService;
import util.ToastUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/requestLeave")
public class RequestLeaveServlet extends HttpServlet
{
    private LeaveManagementService leaveManagementService;

    @Override
    public void init()
    {
        leaveManagementService = new LeaveManagementService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
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

        Leave leave = new Leave();

        leave.setEmployeeId(employee.getEmployeeId());

        leave.setLeaveType(LeaveType.valueOf(request.getParameter("leaveType")));

        LocalDate fromDate = LocalDate.parse(request.getParameter("fromDate"));

        LocalDate toDate = LocalDate.parse(request.getParameter("toDate"));

        leave.setFromDate(fromDate);
        leave.setToDate(toDate);

        leave.setNumberOfDays((int) ChronoUnit.DAYS.between(fromDate, toDate) + 1);

        leave.setReason(request.getParameter("reason"));

        leave.setStatus(LeaveStatus.PENDING);
        leave.setRequestDate(LocalDate.now());

        boolean requested = leaveManagementService.requestLeave(employee, leave);
        PrintWriter out = response.getWriter();

        if(requested)
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_CREATED,
                    "success", "Leave request submitted successfully", "dashboard");
        }
        else
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_BAD_REQUEST,
                    "error", "Unable to submit leave request", "pages/requestLeave.html");
        }
    }
}