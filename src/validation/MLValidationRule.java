package validation;


import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;

public class MLValidationRule implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee currentUser,
                            int numberOfDays,
                            LocalDate fromDate,
                            LocalDate endDate)
    {
        if(!currentUser.getGender().equals("FEMALE"))
        {
            System.out.println("Only female employees can apply for Maternity Leave.");
            return false;
        }

        int mlCount = 0;

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getLeaveType().equals("ML")
                    && leave.getStatus().equals("APPROVED"))
            {
                mlCount++;
            }
        }

        if(mlCount >= 2)
        {
            System.out.println("Maternity Leave can be availed only twice during employment.");
            return false;
        }

        return true;
    }

    @Override
    public String getLeaveType()
    {
        return "ML";
    }
}