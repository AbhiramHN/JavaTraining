package validation;

import enums.Gender;
import enums.LeaveStatus;
import enums.LeaveType;
import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;

public class PaternityLeaveValidation implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee employee, Leave leave)
    {
        if(employee.getGender() != Gender.MALE)
        {
            return false;
        }

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

        if(employee.getLeaveBalance().get(LeaveType.PL) < leave.getNumberOfDays())
        {
            return false;
        }

        int count = 0;

        for(Leave previousLeave : employee.getLeaveRequests())
        {
            if(previousLeave.getLeaveType() == LeaveType.PL
                    && previousLeave.getStatus() == LeaveStatus.APPROVED)
            {
                count++;
            }
        }

        return count < 2;
    }

    @Override
    public LeaveType getLeaveType()
    {
        return LeaveType.PL;
    }
}