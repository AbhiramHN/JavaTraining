package util;

public class EmployeeIdGenerator
{
    private int employeeCounter;

    public EmployeeIdGenerator()
    {
        employeeCounter = 1;
    }

    public String generateEmployeeId()
    {
        String employeeId = String.format("EMP%03d", employeeCounter);
        employeeCounter++;
        return employeeId;
    }
}