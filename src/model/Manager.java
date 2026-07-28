package model;

import interfaces.LeaveApprovalOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Manager extends Employee implements LeaveApprovalOperations
{

    private LeaveApprovalOperations leaveApprovalService;

    public Manager(LeaveApprovalOperations leaveApprovalService)
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