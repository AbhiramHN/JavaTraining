package validation;

import enums.LeaveType;
import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;

public class CasualLeaveValidation implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee employee, Leave leave)
    {
        if(leave.getNumberOfDays() <= 0)
        {
            return false;
        }

        if(leave.getFromDate().isAfter(leave.getToDate()))
        {
            return false;
        }

        if(leave.getFromDate().isBefore(LocalDate.now()))
        {
            return false;
        }

        return employee.getLeaveBalance().get(LeaveType.CL) >= leave.getNumberOfDays();
    }

    @Override
    public LeaveType getLeaveType()
    {
        return LeaveType.CL;
    }
}