package service;

import interfaces.LeaveApprovalOperations;
import model.Employee;
import model.Executive;
import model.Leave;
import repository.LeaveRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LeaveApprovalService implements LeaveApprovalOperations
{

    private LeaveRepository leaveRepository;

    public LeaveApprovalService(LeaveRepository leaveRepository)
    {
        this.leaveRepository = leaveRepository;
    }
    @Override
    public void approveLeave(ArrayList<Leave> pendingLeaves, HashMap<String, Employee> employeesHashMap, Scanner sc)
    {
        if(pendingLeaves.isEmpty())
        {
            System.out.println("\nNo pending leave requests.\n");
            return;
        }

        for(Leave leave : pendingLeaves)
        {
            System.out.println("\nEmployee ID : " + leave.getEmployeeId());
            System.out.println("Leave Type  : " + leave.getLeaveType());
            System.out.println("From Date   : " + leave.getFromDate());
            System.out.println("Days        : " + leave.getNumberOfDays());
            System.out.println("Status      : " + leave.getStatus());
            System.out.println();
        }

        System.out.println("Enter Employee ID:");
        String employeeId = sc.nextLine();

        Leave selectedLeave = null;

        for(Leave leave : pendingLeaves)
        {
            if(leave.getEmployeeId().equals(employeeId))
            {
                selectedLeave = leave;
                break;
            }
        }

        if(selectedLeave == null)
        {
            System.out.println("Employee ID not found in pending requests.");
            return;
        }

        System.out.println("1. Approve");
        System.out.println("2. Reject");

        int choice = sc.nextInt();
        sc.nextLine();

        if(choice != 1 && choice != 2)
        {
            System.out.println("Invalid Choice.");
            return;
        }

        if(choice == 1)
        {
            LocalDate approvalDate = LocalDate.now();
            if(selectedLeave.getLeaveType().equals("CL"))
            {
                if(approvalDate.isAfter(selectedLeave.getFromDate().plusDays(2)))
                {
                    System.out.println("CL cannot be approved after 2 days of availing.");
                    return;
                }

                selectedLeave.setApprovalDate(approvalDate);
            }

            if(selectedLeave.getLeaveType().equals("EL"))
            {
                int currentBalance = employeesHashMap.get(selectedLeave.getEmployeeId()).getLeaveBalance()
                        .get("EL");

                int newBalance = currentBalance - selectedLeave.getNumberOfDays();

                if(newBalance < -15)
                {
                    System.out.println("EL cannot exceed a negative balance of 15 days.");
                    return;
                }
            }

            if(selectedLeave.getLeaveType().equals("SL"))
            {
                int currentBalance = employeesHashMap.get(selectedLeave.getEmployeeId()).getLeaveBalance()
                        .get("SL");

                int newBalance = currentBalance - selectedLeave.getNumberOfDays();
                if(newBalance < -12)
                {
                    System.out.println("SL cannot exceed a negative balance of 12 days.");
                    return;
                }
            }

            selectedLeave.setStatus("APPROVED");

            String leaveType = selectedLeave.getLeaveType();

            int currentBalance = employeesHashMap
                    .get(selectedLeave.getEmployeeId())
                    .getLeaveBalance()
                    .get(leaveType);

            int newBalance = currentBalance - selectedLeave.getNumberOfDays();

            employeesHashMap
                    .get(selectedLeave.getEmployeeId())
                    .getLeaveBalance()
                    .put(leaveType, newBalance);

            leaveRepository.removePendingLeave(selectedLeave);

            System.out.println("\nLeave Approved Successfully!\n");
        }

        else if(choice == 2)
        {
            selectedLeave.setStatus("REJECTED");

            leaveRepository.removePendingLeave(selectedLeave);

            System.out.println("\nLeave Rejected Successfully!\n");
        }
    }

    @Override
    public void revokeLeave(HashMap<String, Employee> employeesHashMap, Scanner sc)
    {
        boolean approvedLeaveFound = false;

        for(Employee employee : employeesHashMap.values())
        {
            if(!(employee instanceof Executive))
            {
                continue;
            }

            for(Leave leave : employee.getLeaveRequests())
            {
                if(leave.getStatus().equals("APPROVED"))
                {
                    approvedLeaveFound = true;

                    System.out.println("Employee ID : " + leave.getEmployeeId());
                    System.out.println("Leave Type : " + leave.getLeaveType());
                    System.out.println("From Date : " + leave.getFromDate());
                    System.out.println("Days : " + leave.getNumberOfDays());
                    System.out.println("Status : " + leave.getStatus());
                    System.out.println();
                }
            }
        }

        if(!approvedLeaveFound)
        {
            System.out.println("No approved leaves found.");
            return;
        }

        System.out.println("Enter Employee ID:");
        String employeeId = sc.nextLine();

        Leave selectedLeave = null;

        for(Employee employee : employeesHashMap.values())
        {
            if(!(employee instanceof Executive))
            {
                continue;
            }

            for(Leave leave : employee.getLeaveRequests())
            {
                if(leave.getEmployeeId().equals(employeeId) && leave.getStatus().equals("APPROVED"))
                {
                    selectedLeave = leave;
                    break;
                }
            }

            if(selectedLeave != null)
            {
                break;
            }
        }

        if(selectedLeave == null)
        {
            System.out.println("Employee ID not found in approved leaves.");
            return;
        }

        System.out.println("1. Confirm Revoke");
        System.out.println("2. Cancel");

        int choice = sc.nextInt();
        sc.nextLine();

        if(choice != 1)
        {
            System.out.println("Operation Cancelled.");
            return;
        }

        selectedLeave.setStatus("REVOKED");
        String leaveType = selectedLeave.getLeaveType();

        Employee employee = employeesHashMap.get(selectedLeave.getEmployeeId());

        int currentBalance = employee.getLeaveBalance().get(leaveType);

        int newBalance = currentBalance + selectedLeave.getNumberOfDays();

        employee.getLeaveBalance().put(leaveType, newBalance);

        System.out.println("\nLeave Revoked Successfully!\n");
    }
}