package validation;

import enums.LeaveType;
import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;

public class LeaveWithoutPayValidation implements LeaveValidationRule
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

        return !leave.getFromDate().isBefore(LocalDate.now());
    }

    @Override
    public LeaveType getLeaveType()
    {
        return LeaveType.LWP;
    }
}