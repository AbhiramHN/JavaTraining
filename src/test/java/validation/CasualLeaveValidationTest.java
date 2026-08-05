package validation;

import enums.LeaveType;
import model.Employee;
import model.Executive;
import model.Leave;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CasualLeaveValidationTest
{

    @Test
    public void testValidLeaveRequest()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();

        leaveBalance.put(LeaveType.CL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.CL);
        leave.setNumberOfDays(2);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(2));

        CasualLeaveValidation validation = new CasualLeaveValidation();

        assertTrue(validation.validate(employee, leave));
    }

    @Test
    public void testNegativeLeaveDays()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();

        leaveBalance.put(LeaveType.CL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.CL);
        leave.setNumberOfDays(-2);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(2));

        CasualLeaveValidation validation = new CasualLeaveValidation();

        assertFalse(validation.validate(employee, leave));
    }


    @Test
    public void testPastDate()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();

        leaveBalance.put(LeaveType.CL, 10);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.CL);
        leave.setNumberOfDays(2);
        leave.setFromDate(LocalDate.now().minusDays(1));
        leave.setToDate(LocalDate.now());

        CasualLeaveValidation validation = new CasualLeaveValidation();

        assertFalse(validation.validate(employee, leave));
    }

    @Test
    public void testInsufficientLeaveBalance()
    {
        Employee employee = new Executive();
        HashMap<LeaveType, Integer> leaveBalance = new HashMap<>();

        leaveBalance.put(LeaveType.CL, 1);
        employee.setLeaveBalance(leaveBalance);

        Leave leave = new Leave();

        leave.setLeaveType(LeaveType.CL);
        leave.setNumberOfDays(5);
        leave.setFromDate(LocalDate.now().plusDays(1));
        leave.setToDate(LocalDate.now().plusDays(5));

        CasualLeaveValidation validation = new CasualLeaveValidation();
        assertFalse(validation.validate(employee, leave));
    }
}