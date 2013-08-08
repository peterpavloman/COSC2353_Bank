/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.sql.Date;
import javax.ejb.Remote;

/**
 *
 * @author narks
 */
@Remote
public interface CustomerBeanRemote 
{
    public void addCustomer(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress);
}
