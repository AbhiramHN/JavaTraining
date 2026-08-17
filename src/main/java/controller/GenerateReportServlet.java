package controller;

import enums.Designation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Employee;
import thread.ReportGenerationThread;
import util.ToastUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/generateReport")
public class GenerateReportServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        Employee employee = (Employee) session.getAttribute("employee");
        PrintWriter out = response.getWriter();

        if(employee.getDesignation() != Designation.MANAGER)
        {
            ToastUtil.showToast(response, out, HttpServletResponse.SC_UNAUTHORIZED,
                    "error", "Authorization Failed", "dashboard");
            //response.sendRedirect("dashboard");
            return;
        }

        ReportGenerationThread reportGenerationThread = new ReportGenerationThread();

        reportGenerationThread.start();

        response.setContentType("text/html");

        out.println("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>Generate Report</title>

            <link rel='stylesheet' href='css/common.css'>
            <link rel='stylesheet' href='css/report.css'>
        </head>
        <body>
        """);

        out.println("<div class='report-container'>");

        out.println("<div class='report-card'>");

        out.println("<div class='report-icon'>📊</div>");

        out.println("<h2>Report Generation Started</h2>");

        out.println("<div class='report-message'>");

        out.println("<p>Your report is being generated in the background.</p>");

        out.println("<p>Once completed, it will be available in the <strong>reports</strong> folder.</p>");

        out.println("</div>");

        out.println("<a class='dashboard-link' href='dashboard'>Back to Dashboard</a>");

        out.println("</div>");

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");


    }
}