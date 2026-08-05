package validation;

import enums.LeaveType;
import model.Employee;
import model.Executive;
import model.Leave;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SickLeaveValidationTest
{
    @Test
    public void testValidLeaveRequest()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();
        leaveBalance.put(LeaveType.SL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.SL);
        leave.setNumberOfDays(2);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(2));

        SickLeaveValidation validation = new SickLeaveValidation();

        assertTrue(validation.validate(employee, leave));
    }

    @Test
    public void testNegativeLeaveDays()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();
        leaveBalance.put(LeaveType.SL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.SL);
        leave.setNumberOfDays(-2);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(2));

        SickLeaveValidation validation = new SickLeaveValidation();

        assertFalse(validation.validate(employee, leave));
    }

    @Test
    public void testPastDate()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();
        leaveBalance.put(LeaveType.SL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.SL);
        leave.setNumberOfDays(2);
        leave.setFromDate(LocalDate.now().minusDays(1));
        leave.setToDate(LocalDate.now());

        SickLeaveValidation validation = new SickLeaveValidation();

        assertFalse(validation.validate(employee, leave));
    }

    @Test
    public void testInsufficientLeaveBalance()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();
        leaveBalance.put(LeaveType.SL, 1);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.SL);
        leave.setNumberOfDays(5);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(5));

        SickLeaveValidation validation = new SickLeaveValidation();

        assertFalse(validation.validate(employee, leave));
    }
}