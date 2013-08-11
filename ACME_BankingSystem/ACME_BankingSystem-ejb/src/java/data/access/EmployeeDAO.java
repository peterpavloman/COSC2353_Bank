package data.access;

import data.Employee;

/**
 *
 * @author narks
 */
public interface EmployeeDAO 
{
    public void create(Employee aEmployee);
    public Employee get(int aIDEmployee);
    public void update(Employee aEmployee);
    public void delete(Employee aEmployee);
}
