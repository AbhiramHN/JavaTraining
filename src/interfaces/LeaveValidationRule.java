package interfaces;

import exception.InvalidLeaveRequestException;
import model.Employee;

import java.time.LocalDate;

public interface LeaveValidationRule
{
    boolean validate(Employee currentUser, int numberOfDays, LocalDate fromDate, LocalDate endDate) throws InvalidLeaveRequestException;

    String getLeaveType();
}