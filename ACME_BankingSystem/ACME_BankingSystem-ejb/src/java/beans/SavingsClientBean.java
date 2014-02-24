package beans;

import data.Customer;
import data.Employee;
import data.Savings;
import data.Transaction;
import data.access.CustomerDAO;
import data.access.EmployeeDAO;
import data.access.SavingsDAO;
import data.access.TransactionDAO;
import data.access.rdb.RDBCustomerDAO;
import data.access.rdb.RDBEmployeeDAO;
import data.access.rdb.RDBSavingsDAO;
import data.access.rdb.RDBTransactionDAO;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateful;
import javax.sql.DataSource;

/**
 * Business logic that provides methods for the Savings subsystem for
 * authenticated users. Stateful bean, so one bean instance per client, and
 * clients must authenticate. Clients can automatically be logged out after
 * a timeout period.
 * @author Peter
 */
@Stateful
public class SavingsClientBean implements SavingsClientBeanRemote
{
	// Configuration
	private static final long LOGIN_SESSION_PERIOD_MS = 3600000;
	private static final int MAX_OPERATIONS_PER_SESSION = 9000;
	private static final int MAX_SAVINGS_ACCOUNTS_PER_CUSTOMER = 2;
	
	@Resource(lookup = "jdbc/ACMEDBDatasource")
	private DataSource mDataSource;
	private Connection mDBConnection;
	private boolean mLoggedIn;
	private int mOperationCount;
	private long mLoginTime;

	@PostConstruct
	private void postConstruct()
	{
		mLoggedIn = false;
		mOperationCount = 0;
		mLoginTime = new java.util.Date().getTime();
		postActivate();
	}

	@PostActivate
	private void postActivate()
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

	@PrePassivate
	@PreDestroy
	private void close()
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

	// Probably should be called 'checkLoggedIn'?.
	/**
	 * Checks if a client login has expired.
	 * @return True if NOT logged in, false if logged in.
	 */
	private boolean checkLoginExpired()
	{
		if (mLoggedIn == false
				|| mLoginTime + LOGIN_SESSION_PERIOD_MS <= new java.util.Date().
				getTime()
				|| mOperationCount >= MAX_OPERATIONS_PER_SESSION)
		{
			mLoggedIn = false;
			return true;
		}
		return false;
	}

	/**
	 * Authenticates the client for this stateful bean instance.
	 * @param aIDEmployee Employee ID that is being logged in.
	 * @param aPassword Employee password as plaintext.
	 * @return true if login successful, false if login failure.
	 */
	@Override
	public boolean login(int aIDEmployee, String aPassword)
	{
		mLoggedIn = false;
		mOperationCount = 0;
		mLoginTime = new java.util.Date().getTime();
		try
		{
			EmployeeDAO lDAO = new RDBEmployeeDAO(mDBConnection);
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

	/**
	 * There are a limited number of operations a client can perform before
	 * being automatically logged out - this method returns the number of
	 * operations the client has performed.
	 * @return Number of operations the client has performed.
	 */
	@Override
	public int getOperationCount()
	{
		return mOperationCount;
	}

	/**
	 * There are a limited number of operations a client can perform before
	 * being automatically logged out - this method the maximum number of
	 * operations the client can perform before being logged out. Is constant.
	 * @return Maximum number of operations the client can perform before being
	 * logged out.
	 */
	@Override
	public int getOperationCountLimit()
	{
		return MAX_OPERATIONS_PER_SESSION;
	}

	/**
	 * Returns if the client is logged in on this stateful bean instance or not.
	 * An expired session is not considered logged in.
	 * @return true if client is logged in, false otherwise.
	 */
	@Override
	public boolean getIsLoggedIn()
	{
		checkLoginExpired();
		return mLoggedIn;
	}

	/**
	 * Creates a new Customer account in the Savings system.
	 * @param aFirstName First name (given name) of created customer.
	 * @param aLastName Last name (surname) of created customer.
	 * @param aDateOfBirth Date of birth of created customer.
	 * @param aAddress Address of created customer.
	 * @param aPassword Password (as plaintext) of created customer.
	 * (TODO: Deprecate function, don't send plaintext over network!)
	 * @return Customer ID of the created account.
	 * @throws LoginFailureException Client is not logged in.
	 * @throws ApplicationLogicException 
	 */
	@Override
	public int createCustomer(String aFirstName, String aLastName,
			Date aDateOfBirth, String aAddress, String aPassword) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		mOperationCount++;

		CustomerDAO lDAO = new RDBCustomerDAO(mDBConnection);
		Customer lNewCustomer = new Customer(aFirstName, aLastName, aDateOfBirth,
				aAddress, aPassword);
		lDAO.create(lNewCustomer);

		int lCustomerId = lNewCustomer.getIDCustomer();
		return lCustomerId;
	}

	/**
	 * Creates a new Savings account for an existing Customer 
	 * in the Savings system.
	 * @param aIDCustomer A customer ID of an existing Customer who will own the
	 * created Savings account.
	 * @return Savings ID of the created account.
	 * @throws LoginFailureException Client is not logged in.
	 * @throws ApplicationLogicException 
	 */
	@Override
	public int createSavingsAccount(int aIDCustomer) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		mOperationCount++;

		SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

		// Enforce maximum of 2 savings account per customer
		// Business rule
		int lAccountCount = lSavingsDAO.getSavingsAccountCount(aIDCustomer);
		if (lAccountCount >= MAX_SAVINGS_ACCOUNTS_PER_CUSTOMER)
		{
			throw new ApplicationLogicException(
					"Customer has reached limit of 2 saving accounts!");
		}

		Savings lNewAccount = new Savings(aIDCustomer, new BigDecimal("0.0"));
		lSavingsDAO.create(lNewAccount);

		int lID = lNewAccount.getIDSavings();
		return lID;
	}

	/**
	 * Deposits money into an existing Savings account.
	 * This transaction is logged by creating a Transaction.
	 * @param aIDSavings ID of Savings account to be deposited into.
	 * @param aAmount Amount to be deposited. Must be positive non-zero.
	 * @throws LoginFailureException Client is not logged in.
	 * @throws ApplicationLogicException 
	 */
	@Override
	public void depositIntoSavingsAccount(int aIDSavings, BigDecimal aAmount) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		mOperationCount++;

		if (aAmount.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new ApplicationLogicException("Invalid deposit amount!");
		}

		SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);
		TransactionDAO lTransactionDAO = new RDBTransactionDAO(mDBConnection);

		Savings lSavings = lSavingsDAO.get(aIDSavings);
		lSavings.setBalance(lSavings.getBalance().add(aAmount));

		Transaction lTransaction = new Transaction(aIDSavings, aAmount,
				"Deposit into savings account");

		// Record transaction, then do update
		lTransactionDAO.create(lTransaction);
		lSavingsDAO.update(lSavings);
	}

	/**
	 * Withdraws money from an existing Savings account.
	 * This transaction is logged by creating a Transaction.
	 * @param aIDSavings ID of Savings account to be deposited into.
	 * @param aAmount Amount to be withdrawn. Must be positive non-zero.
	 * @throws LoginFailureException Client is not logged in.
	 * @throws ApplicationLogicException 
	 */
	@Override
	public void withdrawIntoSavingsAccount(int aIDSavings, BigDecimal aAmount) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		mOperationCount++;

		if (aAmount.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new ApplicationLogicException("Invalid withdraw amount!");
		}

		SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);
		TransactionDAO lTransactionDAO = new RDBTransactionDAO(mDBConnection);

		Savings lSavings = lSavingsDAO.get(aIDSavings);
		BigDecimal lNewBalance = lSavings.getBalance().subtract(aAmount);
		if (lNewBalance.compareTo(BigDecimal.ZERO) < 0)
		{
			throw new ApplicationLogicException(
					"Account does not have sufficient funds!");
		}

		lSavings.setBalance(lNewBalance);

		// We store a negative amount to represent a withdrawal
		aAmount = BigDecimal.ZERO.subtract(aAmount);
		Transaction lTransaction = new Transaction(aIDSavings, aAmount,
				"Withdraw from savings account");

		// Record transaction, then do update
		lTransactionDAO.create(lTransaction);
		lSavingsDAO.update(lSavings);
	}

	/**
	 * Returns the balance of a Savings account.
	 * @param aIDSavings ID of Savings account to be deposited into.
	 * @return Amount in Savings account.
	 * @throws LoginFailureException Client is not logged in.
	 * @throws ApplicationLogicException 
	 */
	@Override
	public BigDecimal getSavingsAccountBalance(int aIDSavings) throws LoginFailureException, ApplicationLogicException
	{
		if (checkLoginExpired())
		{
			throw new LoginFailureException();
		}
		mOperationCount++;

		SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);
		Savings lSavings = lSavingsDAO.get(aIDSavings);
		return lSavings.getBalance();
	}
}
