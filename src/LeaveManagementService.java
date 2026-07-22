import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaveManagementService
{
    private Scanner sc;

    private ArrayList<Leave> executivePendingLeaves;
    private ArrayList<Leave> leadPendingLeaves;
    private ArrayList<Leave> managerLeaves;

    public LeaveManagementService(
            Scanner sc,
            ArrayList<Leave> executivePendingLeaves,
            ArrayList<Leave> leadPendingLeaves,
            ArrayList<Leave> managerLeaves)
    {
        this.sc = sc;
        this.executivePendingLeaves = executivePendingLeaves;
        this.leadPendingLeaves = leadPendingLeaves;
        this.managerLeaves = managerLeaves;
    }

    public boolean isLeaveAvailable(Employee currentUser, String leaveType, int numberOfDays)
    {
        if(leaveType.equals("CL"))
        {
            int balance = currentUser.getLeaveBalance().get(leaveType);
            return balance >= numberOfDays;
        }
        return true;
    }

    private boolean validateCL(Employee currentUser, int numberOfDays,
                               LocalDate fromDate, LocalDate endDate)
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

    private boolean validateML(Employee currentUser, int numberOfDays)
    {
        if(!currentUser.getGender().equals("FEMALE"))
        {
            System.out.println("Only female employees can apply for Maternity Leave.");
            return false;
        }

        int mlCount = 0;

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getLeaveType().equals("ML") && leave.getStatus().equals("APPROVED"))
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

    private boolean validateLWP(Employee currentUser, int numberOfDays)
    {
        if(numberOfDays > 180)
        {
            System.out.println("LWP cannot exceed 180 days at a stretch.");
            return false;
        }
        return true;
    }

    private boolean validatePL(Employee currentUser, int numberOfDays)
    {
        int plCount = 0;
        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getLeaveType().equals("PL")
                    && leave.getStatus().equals("APPROVED"))
            {
                plCount++;
            }
        }

        if(plCount >= 2)
        {
            System.out.println("Parental Leave can be availed only twice during employment.");
            return false;
        }

        return true;
    }

    private boolean validateLeaveRequest(Employee currentUser, String leaveType, int numberOfDays,
                                         LocalDate fromDate, LocalDate endDate) throws InvalidLeaveRequestException
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

        if(leaveType.equals("CL"))
        {
            return validateCL(currentUser, numberOfDays,fromDate, endDate);
        }

        if(leaveType.equals("ML"))
        {
            return validateML(currentUser, numberOfDays);
        }

        if(leaveType.equals("PL"))
        {
            return validatePL(currentUser, numberOfDays);
        }

        if(leaveType.equals("LWP"))
        {
            return validateLWP(currentUser, numberOfDays);
        }

        return true;
    }

    private void processELEncashment(Employee currentUser)
    {
        int currentELBalance = currentUser.getLeaveBalance().get("EL");

        if(currentELBalance > 45)
        {
            currentUser.getLeaveBalance().put("EL", currentELBalance - 30);
            System.out.println("30 EL days have been encashed. Current EL Balance : " + (currentELBalance - 30));
        }
    }

    private void updateSLBalance(Employee currentUser)
    {
        int allowedSL = 12;

        LocalDate joiningDate = currentUser.getJoiningDate();

        if(joiningDate.getYear() == LocalDate.now().getYear())
        {
            int remainingMonths = 12 - joiningDate.getMonthValue() + 1;
            allowedSL = (remainingMonths * 12) / 12;
        }

        if(allowedSL > 60)
        {
            allowedSL = 60;
        }
        int currentSLBalance = currentUser.getLeaveBalance().get("SL");
        if(allowedSL > currentSLBalance)
        {
            currentUser.getLeaveBalance().put("SL", allowedSL);
        }
    }

    private void updateELBalance(Employee currentUser)
    {
        LocalDate joiningDate = currentUser.getJoiningDate();
        long completedMonths = ChronoUnit.MONTHS.between(joiningDate, LocalDate.now());
        int eligibleEL = (int)(completedMonths * 1.25);
        int currentELBalance = currentUser.getLeaveBalance().get("EL");

        if(eligibleEL > 45)
        {
            eligibleEL = 45;
        }

        if(eligibleEL > currentELBalance)
        {
            currentUser.getLeaveBalance().put("EL", eligibleEL);
        }
    }

    public void requestLeave(Employee currentUser) throws InvalidLeaveRequestException
    {
        if(currentUser == null)
        {
            System.out.println("\nPlease Login For This Operation!\n");
            return;
        }

        System.out.println("\nSelect Type Of Leave:");
        System.out.println("CL");
        System.out.println("EL");
        System.out.println("SL");
        System.out.println("ML");
        System.out.println("PL");
        System.out.println("DL");
        System.out.println("LWP");
        System.out.println("Please enter the leave type with exact same letters\n");
        String leaveType = sc.nextLine();

        if(!(leaveType.equals("CL")
                || leaveType.equals("EL")
                || leaveType.equals("SL")
                || leaveType.equals("ML")
                || leaveType.equals("PL")
                || leaveType.equals("DL")
                || leaveType.equals("LWP")))
        {
            System.out.println("Invalid Input. Try Again!");
            return;
        }

        System.out.println("Enter Number Of Days of leaves needed: ");
        int numberOfDays = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter From Date of your leave (YYYY-MM-DD):");
        LocalDate fromDate = LocalDate.parse(sc.nextLine());

        System.out.println("Enter End Date of your leave (YYYY-MM-DD):");
        LocalDate endDate = LocalDate.parse(sc.nextLine());

        if(!validateLeaveRequest(currentUser, leaveType, numberOfDays, fromDate, endDate))
        {
            return;
        }

        updateELBalance(currentUser);
        processELEncashment(currentUser);
        updateSLBalance(currentUser);

        if(!isLeaveAvailable(currentUser, leaveType, numberOfDays))
        {
            System.out.println("Insufficient Leave Balance");
            return;
        }

        for(Leave leave : currentUser.getLeaveRequests())
        {
            if(leave.getStatus().equals("PENDING"))
            {
                System.out.println("You already have a pending leave request.");
                return;
            }
        }

        Leave leaveObject = new Leave();
        leaveObject.setEmployeeId(currentUser.getEmpId());
        leaveObject.setLeaveType(leaveType);

        leaveObject.setFromDate(fromDate);
        leaveObject.setEndDate(endDate);
        leaveObject.setRequestDate(LocalDate.now());

        leaveObject.setNumberOfDays(numberOfDays);
        leaveObject.setStatus("PENDING");

        currentUser.getLeaveRequests().add(leaveObject);

        if(currentUser instanceof Executive)
        {
            executivePendingLeaves.add(leaveObject);
        }
        else if(currentUser instanceof Lead)
        {
            leadPendingLeaves.add(leaveObject);
        }
        else if(currentUser instanceof Manager)
        {
            managerLeaves.add(leaveObject);
        }
        System.out.println("Leave Request Submitted Successfully!");
    }

    public void viewLeaveRequest(Employee currentUser)
    {
        if(currentUser == null)
        {
            System.out.println("\nPlease Login First!\n");
            return;
        }

        if(currentUser.getLeaveRequests().isEmpty())
        {
            System.out.println("\nNo Leave Records Found.\n");
            return;
        }

        System.out.println("\n===== LEAVE HISTORY =====\n");
        for(Leave leave : currentUser.getLeaveRequests())
        {
            System.out.println("Employee ID : " + leave.getEmployeeId());
            System.out.println("Leave Type  : " + leave.getLeaveType());
            System.out.println("From Date   : " + leave.getFromDate());
            System.out.println("Days        : " + leave.getNumberOfDays());
            System.out.println("Status      : " + leave.getStatus());
            System.out.println();
        }
    }

}