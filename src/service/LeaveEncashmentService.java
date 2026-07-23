package service;

import model.Employee;

public class LeaveEncashmentService
{

    public void processELEncashment(Employee currentUser)
    {
        int currentELBalance = currentUser.getLeaveBalance().get("EL");

        if(currentELBalance > 45)
        {
            currentUser.getLeaveBalance().put("EL", currentELBalance - 30);

            System.out.println(
                    "30 EL days have been encashed. Current EL Balance : "
                            + (currentELBalance - 30));
        }
    }

}