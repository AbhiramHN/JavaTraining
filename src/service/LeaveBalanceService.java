package service;

import model.Employee;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveBalanceService
{

    public boolean isLeaveAvailable(Employee currentUser, String leaveType, int numberOfDays)
    {
        if (leaveType.equals("CL"))
        {
            int balance = currentUser.getLeaveBalance().get(leaveType);
            return balance >= numberOfDays;
        }

        return true;
    }

    public void updateSLBalance(Employee currentUser)
    {
        int allowedSL = 12;

        LocalDate joiningDate = currentUser.getJoiningDate();

        if (joiningDate.getYear() == LocalDate.now().getYear())
        {
            int remainingMonths = 12 - joiningDate.getMonthValue() + 1;
            allowedSL = (remainingMonths * 12) / 12;
        }

        if (allowedSL > 60)
        {
            allowedSL = 60;
        }

        int currentSLBalance = currentUser.getLeaveBalance().get("SL");

        if (allowedSL > currentSLBalance)
        {
            currentUser.getLeaveBalance().put("SL", allowedSL);
        }
    }

    public void updateELBalance(Employee currentUser)
    {
        LocalDate joiningDate = currentUser.getJoiningDate();

        long completedMonths =
                ChronoUnit.MONTHS.between(joiningDate, LocalDate.now());

        int eligibleEL = (int) (completedMonths * 1.25);

        int currentELBalance =
                currentUser.getLeaveBalance().get("EL");

        if (eligibleEL > 45)
        {
            eligibleEL = 45;
        }

        if (eligibleEL > currentELBalance)
        {
            currentUser.getLeaveBalance().put("EL", eligibleEL);
        }
    }

}