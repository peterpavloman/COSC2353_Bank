/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access;

/**
 *
 * @author narks
 */
public interface CustomerDAO 
{
    public void create(Customer aCustomer);
    
    public String getFirstName();
    public void setFirstName(String aFirstName);
    
    public void delete();
}
