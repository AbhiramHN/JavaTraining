package validation;

import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;

public class PLValidationRule implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee currentUser,
                            int numberOfDays,
                            LocalDate fromDate,
                            LocalDate endDate)
    {
        int plCount = 0;

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getLeaveType().equals("PL")
                    && leave.getStatus().equals("APPROVED"))
            {
                plCount++;
            }
        }

        if(plCount >= 2)
        {
            System.out.println("Parental Leave can be availed only twice during employment.");
            return false;
        }

        return true;
    }

    @Override
    public String getLeaveType()
    {
        return "PL";
    }
}