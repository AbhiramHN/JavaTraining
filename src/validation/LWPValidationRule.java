package validation;

import interfaces.LeaveValidationRule;
import model.Employee;

import java.time.LocalDate;

public class LWPValidationRule implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee currentUser,
                            int numberOfDays,
                            LocalDate fromDate,
                            LocalDate endDate)
    {
        if(numberOfDays > 180)
        {
            System.out.println("LWP cannot exceed 180 days at a stretch.");
            return false;
        }

        return true;
    }

    @Override
    public String getLeaveType()
    {
        return "LWP";
    }
}