package service;

import exception.InvalidLeaveRequestException;
import model.Employee;
import interfaces.LeaveValidationRule;
import validation.CLValidationRule;
import validation.MLValidationRule;
import validation.PLValidationRule;
import validation.LWPValidationRule;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveValidationService
{

    private final Map<String, LeaveValidationRule> validationRules;
    public  LeaveValidationService()
    {
        validationRules = new HashMap<>();

        validationRules.put("CL", new CLValidationRule());
        validationRules.put("ML", new MLValidationRule());
        validationRules.put("PL", new PLValidationRule());
        validationRules.put("LWP", new LWPValidationRule());
    }
    public boolean validateLeaveRequest(Employee currentUser, String leaveType, int numberOfDays, LocalDate fromDate, LocalDate endDate) throws InvalidLeaveRequestException
    {
        if(numberOfDays <= 0)
        {
            throw new InvalidLeaveRequestException("Number of leave days must be greater than 0.");
        }

        if(fromDate.isBefore(LocalDate.now()))
        {
            throw new InvalidLeaveRequestException("From Date cannot be in the past.");
        }

        if(endDate.isBefore(fromDate))
        {
            throw new InvalidLeaveRequestException("End Date cannot be before From Date.");
        }

        if(leaveType.equals("EL"))
        {
            long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), fromDate);

            if(daysBetween < 7)
            {
                System.out.println("Earned Leave must be requested at least 7 days in advance.");
                return false;
            }
        }

        LeaveValidationRule rule = validationRules.get(leaveType);

        if(rule != null)
        {
            return rule.validate(currentUser, numberOfDays, fromDate, endDate);
        }

        return true;
    }
}