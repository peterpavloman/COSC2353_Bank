package beans;

import data.Customer;
import data.Employee;
import data.Savings;
import data.Transaction;
import data.access.CustomerDAO;
import data.access.EmployeeDAO;
import data.access.SavingsDAO;
import data.access.TransactionDAO;
import data.access.rdb.CustomerRBD;
import data.access.rdb.EmployeeRDB;
import data.access.rdb.SavingsRDB;
import data.access.rdb.TransactionRDB;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.sql.DataSource;

/**
 *
 * @author Peter (s3286430)
 */
@Stateful
public class SavingsClientBean implements SavingsClientBeanRemote
{
	private static final long	LOGIN_SESSION_PERIOD_MS = 60000;
	private static final int	MAX_OPERATIONS_PER_SESSION = 10;
	
	private static final int	MAX_SAVINGS_ACCOUNTS_PER_CUSTOMER = 2;
	
	@Resource(lookup = "jdbc/ACMEDBDatasource")
    private DataSource mDataSource;
    private Connection mDBConnection;
	
	private boolean mLoggedIn;
	private int mOperationCount;
	private long mLoginTime;
	
    @PostConstruct
    public void initialize()
    {
		mLoggedIn = false;
		mOperationCount = 0;
		mLoginTime = new java.util.Date().getTime();
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
            EmployeeDAO lDAO = new EmployeeRDB(mDBConnection);
            Employee lEmployee = lDAO.get(aIDEmployee);
			
			if (lEmployee.getPassword().equals(aPassword))
			{
				mLoggedIn = true;
				return true;
			}
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
		return mOperationCount;
	}

	@Override
	public int getOperationCountLimit()
	{
		return MAX_OPERATIONS_PER_SESSION;
	}
	
	@Override
	public boolean getIsLoggedIn()
	{
		return mLoggedIn;
	}

	@Override
	public int createCustomer(String aFirstName, String aLastName,
			Date aDateOfBirth, String aAddress) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		CustomerDAO lDAO = new CustomerRBD(mDBConnection);
		Customer lNewCustomer = new Customer(aFirstName, aLastName, aDateOfBirth, aAddress);
		lDAO.create(lNewCustomer);
		
		int lCustomerId = lNewCustomer.getIDCustomer();
		return lCustomerId;
	}

	@Override
	public int createSavingsAccount(int aIDCustomer) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		
		SavingsDAO lSavingsDAO = new SavingsRDB(mDBConnection);
		
		// Enforce maximum of 2 savings account per customer
		int lAccountCount = lSavingsDAO.getSavingsAccountCount(aIDCustomer);
		if (lAccountCount >= MAX_SAVINGS_ACCOUNTS_PER_CUSTOMER)
		{
			throw new ApplicationLogicException("Customer has reached limit of 2 saving accounts!");
		}
		
		Savings lNewAccount = new Savings(aIDCustomer, new BigDecimal("0.0"));
		lSavingsDAO.create(lNewAccount);
		
		int lID = lNewAccount.getIDSavings();
		return lID;
	}

	@Override
	public void depositIntoSavingsAccount(int aIDSavings, BigDecimal aAmount) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		SavingsDAO lSavingsDAO = new SavingsRDB(mDBConnection);
		TransactionDAO lTransactionDAO = new TransactionRDB(mDBConnection);
		
		Savings lSavings = lSavingsDAO.get(aIDSavings);
		lSavings.setBalance(lSavings.getBalance().add(aAmount));
		
		Transaction lTransaction = new Transaction(aIDSavings, aAmount, 
				"Deposit into savings account");
		
		// Record transaction, then do update
		lTransactionDAO.create(lTransaction);
		lSavingsDAO.update(lSavings);
	}

	@Override
	public void withdrawIntoSavingsAccount(int aIDSavings, BigDecimal aAmount) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		SavingsDAO lSavingsDAO = new SavingsRDB(mDBConnection);
		TransactionDAO lTransactionDAO = new TransactionRDB(mDBConnection);
		
		Savings lSavings = lSavingsDAO.get(aIDSavings);
		lSavings.setBalance(lSavings.getBalance().subtract(aAmount));
		
		if (lSavings.getBalance().compareTo(BigDecimal.ZERO) < 0)
		{
			throw new ApplicationLogicException("Account does not have sufficient funds!");
		}
		
		Transaction lTransaction = new Transaction(aIDSavings, aAmount, 
				"Withdraw from savings account");
		
		// Record transaction, then do update
		lTransactionDAO.create(lTransaction);
		lSavingsDAO.update(lSavings);
	}

	@Override
	public BigDecimal getSavingsAccountBalance(int aIDSavings) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		SavingsDAO lSavingsDAO = new SavingsRDB(mDBConnection);
		Savings lSavings = lSavingsDAO.get(aIDSavings);
		return lSavings.getBalance();
	}

	
}
