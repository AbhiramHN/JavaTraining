package service;

import enums.LeaveType;
import interfaces.LeaveValidationRule;
import model.Employee;
import model.Leave;
import validation.*;

import java.util.ArrayList;

public class LeaveValidationService
{
    private final ArrayList<LeaveValidationRule> validationRules;

    public LeaveValidationService()
    {
        validationRules = new ArrayList<>();

        validationRules.add(new CasualLeaveValidation());
        validationRules.add(new SickLeaveValidation());
        validationRules.add(new MaternityLeaveValidation());
        validationRules.add(new PaternityLeaveValidation());
        validationRules.add(new LeaveWithoutPayValidation());
    }

    public boolean validate(Employee employee, Leave leave)
    {
        for(LeaveValidationRule validationRule : validationRules)
        {
            if(validationRule.getLeaveType() == leave.getLeaveType())
            {
                return validationRule.validate(employee, leave);
            }
        }

        return false;
    }
}