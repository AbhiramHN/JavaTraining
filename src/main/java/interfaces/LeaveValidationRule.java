package interfaces;

import enums.LeaveType;
import model.Employee;
import model.Leave;

public interface LeaveValidationRule
{
    boolean validate(Employee employee, Leave leave);

    LeaveType getLeaveType();
}