package factory;

import enums.Designation;
import model.Employee;
import model.Executive;
import model.Lead;
import model.Manager;

public class EmployeeFactory
{
    public Employee createEmployee(Designation designation)
    {
        Employee employee;

        switch (designation)
        {
            case EXECUTIVE:
                employee = new Executive();
                break;

            case LEAD:
                employee = new Lead();
                break;

            case MANAGER:
                employee = new Manager();
                break;

            default:
                throw new IllegalArgumentException("Invalid Designation.");
        }

        employee.setDesignation(designation);

        return employee;
    }
}