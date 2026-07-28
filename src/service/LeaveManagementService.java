package service;

import exception.InvalidLeaveRequestException;
import model.Employee;
import model.Executive;
import model.Leave;
import model.Lead;
import model.Manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaveManagementService
{
    private final Scanner sc;

    private final ArrayList<Leave> executivePendingLeaves;
    private final ArrayList<Leave> leadPendingLeaves;
    private final ArrayList<Leave> managerLeaves;

    private final LeaveBalanceService leaveBalanceService;
    private final LeaveEncashmentService leaveEncashmentService;
    private final LeaveValidationService leaveValidationService;
    private final LeaveHistoryService leaveHistoryService;

    public LeaveManagementService(
            Scanner sc,
            ArrayList<Leave> executivePendingLeaves,
            ArrayList<Leave> leadPendingLeaves,
            ArrayList<Leave> managerLeaves)
    {

        this.sc = sc;
        this.executivePendingLeaves = executivePendingLeaves;
        this.leadPendingLeaves = leadPendingLeaves;
        this.managerLeaves = managerLeaves;

        leaveBalanceService = new LeaveBalanceService();
        leaveEncashmentService = new LeaveEncashmentService();
        leaveValidationService = new LeaveValidationService();
        leaveHistoryService = new LeaveHistoryService();
    }


    public void requestLeave(Employee currentUser) throws InvalidLeaveRequestException
    {
        if(currentUser == null)
        {
            System.out.println("\nPlease Login For This Operation!");
            return;
        }

        System.out.println("\nSelect Type Of Leave:");
        System.out.println("CL");
        System.out.println("EL");
        System.out.println("SL");
        System.out.println("ML");
        System.out.println("PL");
        System.out.println("DL");
        System.out.println("LWP");
        System.out.println("Please enter the leave type with exact same letters\n");
        String leaveType = sc.nextLine();

        if(!(leaveType.equals("CL")
                || leaveType.equals("EL")
                || leaveType.equals("SL")
                || leaveType.equals("ML")
                || leaveType.equals("PL")
                || leaveType.equals("DL")
                || leaveType.equals("LWP")))
        {
            System.out.println("Invalid Input. Try Again!");
            return;
        }

        System.out.println("Enter Number Of Days of leaves needed: ");
        int numberOfDays = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter From Date of your leave (YYYY-MM-DD):");
        LocalDate fromDate = LocalDate.parse(sc.nextLine());

        System.out.println("Enter End Date of your leave (YYYY-MM-DD):");
        LocalDate endDate = LocalDate.parse(sc.nextLine());

        if(!leaveValidationService.validateLeaveRequest(currentUser, leaveType, numberOfDays, fromDate, endDate))
        {
            return;
        }

        leaveBalanceService.updateELBalance(currentUser);
        leaveEncashmentService.processELEncashment(currentUser);
        leaveBalanceService.updateSLBalance(currentUser);

        if(!leaveBalanceService.isLeaveAvailable(currentUser, leaveType, numberOfDays))
        {
            System.out.println("Insufficient Leave Balance");
            return;
        }

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getStatus().equals("PENDING"))
            {
                System.out.println("You already have a pending leave request.");
                return;
            }
        }

        Leave leaveObject = new Leave();
        leaveObject.setEmployeeId(currentUser.getEmpId());
        leaveObject.setLeaveType(leaveType);
        leaveObject.setFromDate(fromDate);
        leaveObject.setEndDate(endDate);
        leaveObject.setRequestDate(LocalDate.now());
        leaveObject.setNumberOfDays(numberOfDays);
        leaveObject.setStatus("PENDING");

        currentUser.getLeaveRequests().add(leaveObject);

        if(currentUser instanceof Executive)
        {
            executivePendingLeaves.add(leaveObject);
        }
        else if(currentUser instanceof Lead)
        {
            leadPendingLeaves.add(leaveObject);
        }
        else if(currentUser instanceof Manager)
        {
            managerLeaves.add(leaveObject);
        }

        System.out.println("Leave Request Submitted Successfully!");
    }

    public void viewLeaveRequest(Employee currentUser)
    {
        leaveHistoryService.viewLeaveRequest(currentUser);
    }
}