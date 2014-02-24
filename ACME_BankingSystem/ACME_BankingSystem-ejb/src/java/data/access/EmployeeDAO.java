package data.access;

import data.Employee;
import exceptions.ApplicationLogicException;

/**
 *
 * @author Peter
 */
public interface EmployeeDAO
{
    public void create(Employee aEmployee) throws ApplicationLogicException;
    public Employee get(int aIDEmployee) throws ApplicationLogicException;
    public void update(Employee aEmployee) throws ApplicationLogicException;
    public void delete(Employee aEmployee) throws ApplicationLogicException;
}
