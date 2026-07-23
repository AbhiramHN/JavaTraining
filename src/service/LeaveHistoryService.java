package service;

import model.Employee;
import model.Leave;

public class LeaveHistoryService
{

    public void viewLeaveRequest(Employee currentUser)
    {
        if(currentUser == null)
        {
            System.out.println("\nPlease Login First!\n");
            return;
        }

        if(currentUser.getLeaveRequests().isEmpty())
        {
            System.out.println("\nNo Leave Records Found.\n");
            return;
        }

        System.out.println("\n===== LEAVE HISTORY =====\n");

        for(Leave leave : currentUser.getLeaveRequests())
        {
            System.out.println("Employee ID : " + leave.getEmployeeId());
            System.out.println("Leave Type  : " + leave.getLeaveType());
            System.out.println("From Date   : " + leave.getFromDate());
            System.out.println("Days        : " + leave.getNumberOfDays());
            System.out.println("Status      : " + leave.getStatus());
            System.out.println();
        }
    }

}