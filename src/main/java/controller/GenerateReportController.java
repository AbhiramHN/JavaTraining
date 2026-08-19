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
public class GenerateReportController extends HttpServlet
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
            ToastUtil.showToast(response, out, HttpServletResponse.SC_BAD_REQUEST,
                    "error", "Not Authorized.", "dashboard");
            //response.sendRedirect("dashboard");
            return;
        }

        ReportGenerationThread reportGenerationThread = new ReportGenerationThread();

        reportGenerationThread.start();

        response.setContentType("text/html");

        //PrintWriter out = response.getWriter();

        out.println("<html>");

        out.println("<head>");
        out.println("<title>Report Generation</title>");
        out.println("<link rel='stylesheet' href='"
                + request.getContextPath()
                + "/css/report.css'>");
        out.println("</head>");

        out.println("<body>");

        out.println("<div class='container'>");

        out.println("<div class='card'>");

        out.println("<h2>Report generation started.</h2>");

        out.println("<p>Your report is being generated in the background.</p>");

        out.println("<p>Once completed, it will be available in the <b>reports</b> folder.</p>");

        out.println("<a href='dashboard'>Back to Dashboard</a>");

        out.println("</div>");

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}