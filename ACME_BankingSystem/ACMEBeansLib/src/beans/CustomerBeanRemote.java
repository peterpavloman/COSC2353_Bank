package beans;

import java.sql.Date;
import javax.ejb.Remote;

/**
 * Remote Interface of a Stateless Session Bean that holds 
 * Customer related business logic.
 * 
 * @author Peter (s3286430)
 */

@Remote
public interface CustomerBeanRemote 
{
    public void create(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress);
    
    public void update(int aIDCustomer, String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress);
    
    public void delete(int aIDCustomer);
}
