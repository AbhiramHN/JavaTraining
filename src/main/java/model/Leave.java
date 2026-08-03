package model;

import enums.LeaveStatus;
import enums.LeaveType;

import java.time.LocalDate;

public class Leave
{
    private int leaveId;
    private String employeeId;
    private LeaveType leaveType;
    private int numberOfDays;
    private String reason;
    private LeaveStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate requestDate;
    private LocalDate approvalDate;
    private String approvedBy;

    public int getLeaveId()
    {
        return leaveId;
    }

    public void setLeaveId(int leaveId)
    {
        this.leaveId = leaveId;
    }

    public String getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(String employeeId)
    {
        this.employeeId = employeeId;
    }

    public LeaveType getLeaveType()
    {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType)
    {
        this.leaveType = leaveType;
    }

    public int getNumberOfDays()
    {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays)
    {
        this.numberOfDays = numberOfDays;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public LeaveStatus getStatus()
    {
        return status;
    }

    public void setStatus(LeaveStatus status)
    {
        this.status = status;
    }

    public LocalDate getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate)
    {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate()
    {
        return toDate;
    }

    public void setToDate(LocalDate toDate)
    {
        this.toDate = toDate;
    }

    public LocalDate getRequestDate()
    {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate)
    {
        this.requestDate = requestDate;
    }

    public LocalDate getApprovalDate()
    {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate)
    {
        this.approvalDate = approvalDate;
    }

    public String getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy)
    {
        this.approvedBy = approvedBy;
    }
}