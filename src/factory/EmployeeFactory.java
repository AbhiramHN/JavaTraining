package factory;

import interfaces.LeaveApprovalOperations;
import model.Employee;
import model.Executive;
import model.Lead;
import model.Manager;

public class EmployeeFactory
{
    private enum Designation
    {
        EXECUTIVE,
        LEAD,
        MANAGER
    }

    public Employee createEmployee(int designation, LeaveApprovalOperations leaveApprovalService)
    {
        Designation employeeDesignation;

        switch (designation)
        {
            case 1:
                employeeDesignation = Designation.EXECUTIVE;
                break;

            case 2:
                employeeDesignation = Designation.LEAD;
                break;

            case 3:
                employeeDesignation = Designation.MANAGER;
                break;

            default:
                throw new IllegalArgumentException("Invalid Designation");
        }

        switch (employeeDesignation)
        {
            case EXECUTIVE:
                return new Executive();

            case LEAD:
                return new Lead(leaveApprovalService);

            case MANAGER:
                return new Manager(leaveApprovalService);

            default:
                throw new IllegalArgumentException("Invalid Designation");
        }
    }
}