package beans;

import exceptions.LoginFailureException;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import security.LoginSession;
import security.LoginSessionImpl;

/**
 *
 * @author Peter (s3286430)
 */
@Stateful
public class EmployeeBean implements EmployeeBeanRemote
{
	private LoginSession mLoginSession;
	
	@PostConstruct
	public void initialize()
	{
		
	}

	@Override
	public LoginSession login(int aEmployeeId, String aPassword) throws LoginFailureException
	{
		return new LoginSessionImpl(aEmployeeId, aPassword);
	}
}
