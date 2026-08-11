package validation;

import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CLValidationRule implements LeaveValidationRule
{
    @Override
    public boolean validate(Employee currentUser,
                            int numberOfDays,
                            LocalDate fromDate,
                            LocalDate endDate)
    {
        if(numberOfDays > 2)
        {
            System.out.println("Maximum 2 Casual Leave can be availed at a time.");
            return false;
        }

        long totalDays = ChronoUnit.DAYS.between(fromDate, endDate) + 1;

        if(totalDays > 4)
        {
            System.out.println("Total period of absence for Casual Leave cannot exceed 4 days.");
            return false;
        }

        int allowedCL = 10;

        LocalDate joiningDate = currentUser.getJoiningDate();

        if(joiningDate.getYear() == fromDate.getYear())
        {
            int remainingMonths = 12 - joiningDate.getMonthValue() + 1;
            allowedCL = (remainingMonths * 10) / 12;
        }

        int usedCL = 0;

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getLeaveType().equals("CL")
                    && leave.getStatus().equals("APPROVED")
                    && leave.getFromDate().getYear() == fromDate.getYear())
            {
                usedCL += leave.getNumberOfDays();
            }
        }

        if(usedCL + numberOfDays > allowedCL)
        {
            System.out.println("Casual Leave balance exceeded for the current year.");
            return false;
        }

        return true;
    }

    @Override
    public String getLeaveType()
    {
        return "CL";
    }
}