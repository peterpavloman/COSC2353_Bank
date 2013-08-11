/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.access;

import data.Customer;

/**
 *
 * @author narks
 */
public interface CustomerDAO 
{
    public void create(Customer aCustomer);
    public Customer get(int aIDCustomer);
    public void update(Customer aCustomer);
    public void delete(Customer aCustomer);
}
