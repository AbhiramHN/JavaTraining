package factory;

import interfaces.LeaveApprovalOperations;
import model.Employee;
import model.Executive;
import model.Lead;
import model.Manager;

public class EmployeeFactory
{
    public Employee createEmployee(int designation,
                                   LeaveApprovalOperations leaveApprovalService)
    {
        if (designation == 1)
        {
            return new Executive();
        }
        else if (designation == 2)
        {
            return new Lead(leaveApprovalService);
        }
        else
        {
            return new Manager(leaveApprovalService);
        }
    }
}