package beans;

import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.math.BigDecimal;
import java.sql.Date;
import javax.ejb.Remote;

/**
 *
 * @author Peter (s3286430)
 */
@Remote
public interface SavingsClientBeanRemote
{
	public boolean login(int aIDEmployee, String aPassword);
	
	public int getOperationCount();
	public int getOperationCountLimit();
	
	public boolean getIsLoggedIn();
	
	/**
	 *
	 * @param aFirstName
	 * @param aLastName
	 * @param aDateOfBirth
	 * @param aAddress
	 * @return IDCustomer of newly created customer.
	 * @throws LoginFailureException If the employee is no longer logged in.
	 * @throws ApplicationLogicException
	 */
	public int createCustomer(String aFirstName, String aLastName,
			Date aDateOfBirth, String aAddress)
			throws LoginFailureException, ApplicationLogicException;

	public int createSavingsAccount(int aIDCustomer)
			throws LoginFailureException, ApplicationLogicException;
	
	public void depositIntoSavingsAccount(int aIDSavings, BigDecimal aAmount)
			throws LoginFailureException, ApplicationLogicException;
	
	public void withdrawIntoSavingsAccount(int aIDSavings, BigDecimal aAmount)
			throws LoginFailureException, ApplicationLogicException;
	
	public BigDecimal getSavingsAccountBalance(int aIDSavings)
			throws LoginFailureException, ApplicationLogicException;
}
