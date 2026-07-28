package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import interfaces.LeaveApprovalOperations;

public class Lead extends Employee implements LeaveApprovalOperations
{
    private LeaveApprovalOperations leaveApprovalService;

    public Lead(LeaveApprovalOperations leaveApprovalService)
    {
        this.leaveApprovalService = leaveApprovalService;
    }


    @Override
    public void approveLeave(ArrayList<Leave> pendingLeaves, HashMap<String, Employee> employeesHashMap, Scanner sc)
    {
        leaveApprovalService.approveLeave(pendingLeaves, employeesHashMap, sc);
    }


    @Override
    public void revokeLeave(HashMap<String, Employee> employeesHashMap, Scanner sc)
    {
        leaveApprovalService.revokeLeave(employeesHashMap, sc);
    }
}