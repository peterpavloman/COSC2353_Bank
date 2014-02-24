package data.access;

import data.Customer;
import exceptions.ApplicationLogicException;

/**
 * Data Access Object interface for Customers.
 * 
 * @author Peter
 */
public interface CustomerDAO 
{
    public void create(Customer aCustomer) throws ApplicationLogicException;
    public Customer get(int aIDCustomer) throws ApplicationLogicException;
    public void update(Customer aCustomer) throws ApplicationLogicException;
    public void delete(Customer aCustomer) throws ApplicationLogicException;
}
