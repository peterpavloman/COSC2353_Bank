package beans;

import exceptions.LoginFailureException;
import java.sql.Date;
import javax.ejb.Remote;
import security.LoginSession;

/**
 * Remote Interface of a Stateless Session Bean that holds 
 * Customer related business logic.
 * 
 * @author Peter (s3286430)
 */

@Remote
public interface CustomerBeanRemote 
{
	public void login(LoginSession aSession) throws LoginFailureException;
	
    public void create(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress) throws LoginFailureException;
    
    public void update(int aIDCustomer, String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress) throws LoginFailureException;
    
    public void delete(int aIDCustomer) throws LoginFailureException;
}
