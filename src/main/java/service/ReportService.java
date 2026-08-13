package service;

import dao.ReportDAO;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ReportService
{
    private final ReportDAO reportDAO;
    public ReportService()
    {
        reportDAO = new ReportDAO();
    }
    public void generateReport()
    {
        int totalEmployees =
                reportDAO.getTotalEmployees();

        int pendingLeaves =
                reportDAO.getPendingLeaveCount();

        int approvedLeaves =
                reportDAO.getApprovedLeaveCount();

        int rejectedLeaves =
                reportDAO.getRejectedLeaveCount();

        int revokedLeaves =
                reportDAO.getRevokedLeaveCount();

        try(FileWriter writer =
                    new FileWriter("D:\\Java Projects\\LMS_Project\\reports\\LeaveReport.txt"))
        {
            writer.write("=====================================\n");
            writer.write("     LEAVE MANAGEMENT REPORT\n");
            writer.write("=====================================\n\n");

            writer.write("Generated On : "
                    + LocalDateTime.now()
                    + "\n\n");

            writer.write("Total Employees : "
                    + totalEmployees
                    + "\n");

            writer.write("Pending Leaves  : "
                    + pendingLeaves
                    + "\n");

            writer.write("Approved Leaves : "
                    + approvedLeaves
                    + "\n");

            writer.write("Rejected Leaves : "
                    + rejectedLeaves
                    + "\n");

            writer.write("Revoked Leaves  : "
                    + revokedLeaves
                    + "\n");

            writer.write("\n=====================================\n");
            writer.write("End Of Report\n");
            writer.write("=====================================\n");

            System.out.println("Report generated successfully.");
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
    }
}