package thread;

import service.ReportService;

public class ReportGenerationThread extends Thread
{
    private final ReportService reportService;

    public ReportGenerationThread()
    {
        reportService = new ReportService();
    }

    @Override
    public void run()
    {

        reportService.generateReport();
    }
}