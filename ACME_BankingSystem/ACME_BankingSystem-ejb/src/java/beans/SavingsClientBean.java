package beans;

import data.Customer;
import data.Employee;
import data.access.CustomerDAO;
import data.access.EmployeeDAO;
import data.access.rdb.RDBCustomerDAO;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.sql.DataSource;
import security.LoginSession;

/**
 *
 * @author Peter (s3286430)
 */
@Stateful
public class SavingsClientBean implements SavingsClientBeanRemote
{
	private static final long	LOGIN_SESSION_PERIOD_MS = 60000;
	private static final int	MAX_OPERATIONS_PER_SESSION = 10;
	
	@Resource(lookup = "jdbc/ACMEDBDatasource")
    private DataSource mDataSource;
    private Connection mDBConnection;
	
	private boolean mLoggedIn;
	private int mOperationCount;
	private long mLoginTime;
	
    @PostConstruct
    public void initialize()
    {
        try
        {
            mDBConnection = mDataSource.getConnection();
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
        }
    }

    @PreDestroy
    public void close()
    {
        try
        {
            mDBConnection.close();
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
        }
    }
	
	private boolean checkLoginExpired()
	{
		if (mLoggedIn == false
				|| mLoginTime + LOGIN_SESSION_PERIOD_MS <= new java.util.Date().getTime()
				|| mOperationCount >= MAX_OPERATIONS_PER_SESSION)
		{
			mLoggedIn = false;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean login(int aIDEmployee, String aPassword)
	{
		mLoggedIn = false;
		mOperationCount = 0;
		mLoginTime = new java.util.Date().getTime();
		try
        {
			/*
            EmployeeDAO mDAO = new RDBEmployeeDAO(mDBConnection);
            Employee lEmployee = RDBEmployeeDAO.get(aIDEmployee);
			
			if (lEmployee.getPassword().equals(aPassword))
			{
				mLoggedIn = true;
			}
			*/
        }
        catch (Exception aException)
        {
            System.out.println("Could not check employee details.");
        }
		return false;
	}

	@Override
	public int getOperationCount()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean getIsLoggedIn()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int createCustomer(String aFirstName, String aLastName,
			Date aDateOfBirth, String aAddress) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int createSavingsAccount(int aIDCustomer) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void depositIntoSavingsAccount(int aIDSavings) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void withdrawIntoSavingsAccount(int aIDSavings) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getSavingsAccountBalance(int aIDSavings) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
}
