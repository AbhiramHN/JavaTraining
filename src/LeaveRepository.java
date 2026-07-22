import java.util.ArrayList;

public class LeaveRepository
{
    private ArrayList<Leave> executivePendingLeaves;
    private ArrayList<Leave> leadPendingLeaves;
    private ArrayList<Leave> managerLeaves;

    public LeaveRepository()
    {
        executivePendingLeaves = new ArrayList<>();
        leadPendingLeaves = new ArrayList<>();
        managerLeaves = new ArrayList<>();
    }

    public ArrayList<Leave> getExecutivePendingLeaves()
    {
        return executivePendingLeaves;
    }

    public ArrayList<Leave> getLeadPendingLeaves()
    {
        return leadPendingLeaves;
    }

    public ArrayList<Leave> getManagerLeaves()
    {
        return managerLeaves;
    }

    public void addExecutiveLeave(Leave leave)
    {
        executivePendingLeaves.add(leave);
    }

    public void addLeadLeave(Leave leave)
    {
        leadPendingLeaves.add(leave);
    }

    public void addManagerLeave(Leave leave)
    {
        managerLeaves.add(leave);
    }
}