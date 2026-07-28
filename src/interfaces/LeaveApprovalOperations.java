package interfaces;

import model.Employee;
import model.Leave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public interface LeaveApprovalOperations
{
    void approveLeave(ArrayList<Leave> pendingLeaves, HashMap<String, Employee> employeesHashMap, Scanner sc);

    void revokeLeave(HashMap<String, Employee> employeesHashMap, Scanner sc);
}