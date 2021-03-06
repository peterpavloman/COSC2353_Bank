package jms;

import data.Savings;
import data.Transaction;
import data.access.CustomerDAO;
import data.access.SavingsDAO;
import data.access.TransactionDAO;
import data.access.rdb.RDBCustomerDAO;
import data.access.rdb.RDBSavingsDAO;
import data.access.rdb.RDBTransactionDAO;
import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.ObjectMessage;
import javax.sql.DataSource;
import jms.homeloan.MessageProtocolSavings;

/**
 *
 * Business logic that determines and formats a response using the
 * Savings system to received messages from the HomeLoan system.
 *
 * @author Peter
 */

// In hindsight, functions should take typical arguments instead of
// object messages - conversation to-from object message should occur
// in the MDB. Then this bean would be renamed to something more appropriate.

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HomeLoanMessageHandler
{

	@Resource(lookup = "jdbc/ACMEDBDatasource")
	private DataSource mDataSource;
	private Connection mDBConnection;

	@PostConstruct
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

	/**
	 * 
	 * 
	 * @param aRequest
	 * @param aResponse
	 * @return 
	 */
	public ObjectMessage withdrawFromSavingsAccount(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{		
		try
		{
			SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

			BigDecimal lAmount = MessageProtocolSavings.WithdrawFromSavingsAccount.
					getRequestAmount(aRequest);

			if (lAmount.compareTo(BigDecimal.ZERO) <= 0)
			{
				throw new ApplicationLogicException("Invalid withdraw amount!");
			}

			TransactionDAO lTransactionDAO = new RDBTransactionDAO(mDBConnection);

			int lIDSavings = MessageProtocolSavings.WithdrawFromSavingsAccount.
					getRequestIDSavings(aRequest);
			
			Savings lSavings = lSavingsDAO.get(lIDSavings);
			
			BigDecimal lNewBalance = lSavings.getBalance().subtract(lAmount);
			if (lNewBalance.compareTo(BigDecimal.ZERO) < 0)
			{
				// Report insufficient funds
				aResponse = MessageProtocolSavings.WithdrawFromSavingsAccount.
					formatResponseMessage(aResponse, false);
				return aResponse;
			}

			lSavings.setBalance(lNewBalance);

			// We store a negative amount to represent a withdrawal
			lAmount = BigDecimal.ZERO.subtract(lAmount);
			Transaction lTransaction = new Transaction(lIDSavings, lAmount,
					"Withdraw from savings account to pay homeloan");

			// Record transaction, then do update
			lTransactionDAO.create(lTransaction);
			lSavingsDAO.update(lSavings);

			aResponse = MessageProtocolSavings.WithdrawFromSavingsAccount.
					formatResponseMessage(aResponse, true);

			return aResponse;
		}
		catch (Exception aException)
		{
			aException.printStackTrace();
			return null;
		}
	}
	
	public ObjectMessage getTotalSavingsBalance(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

			BigDecimal lBalance = lSavingsDAO.getTotalSavingsBalance();
			
			aResponse = MessageProtocolSavings.GetTotalSavingsBalance.
					formatResponseMessage(aResponse, lBalance);

			return aResponse;
		}
		catch (Exception aException)
		{
			aException.printStackTrace();
			return null;
		}
	}
	
	private static final int VALID_DEPOSIT_MINIMUM = 3;

	public ObjectMessage isCustomerValidForHomeLoan(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

			List<Integer> lSavingsIdList = lSavingsDAO.getSavingsIdList(
					MessageProtocolSavings.IsCustomerValidForHomeLoan.
					getRequestIDCustomer(aRequest));

			boolean lIsValid = false;
			for (Integer lId : lSavingsIdList)
			{
				if (lSavingsDAO.getDepositCount(lId) >= VALID_DEPOSIT_MINIMUM)
				{
					lIsValid = true;
					break;
				}
			}

			aResponse = MessageProtocolSavings.IsCustomerValidForHomeLoan.
					formatResponseMessage(aResponse, lIsValid);

			return aResponse;
		}
		catch (Exception aException)
		{
			aException.printStackTrace();
			return null;
		}
	}

	public ObjectMessage authenticateLogin(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			CustomerDAO lCustomerDAO = new RDBCustomerDAO(mDBConnection);

			int lIDCustomer = MessageProtocolSavings.AuthenticateLogin.
					getRequestIDCustomer(aRequest);

			data.Customer lCustomer = lCustomerDAO.get(lIDCustomer);

			boolean lSuccess;
			// If a password has not been set, ALWAYS fail login
			if (lCustomer.getPassword() == null)
			{
				lSuccess = false;
			}
			else
			{
				lSuccess = lCustomer.getPassword().equals(
						MessageProtocolSavings.AuthenticateLogin.
						getRequestPassword(aRequest));
			}

			aResponse = MessageProtocolSavings.AuthenticateLogin.
					formatResponseMessage(aResponse, lSuccess);

			return aResponse;
		}
		catch (Exception aException)
		{
			aException.printStackTrace();
			return null;
		}
	}

	public ObjectMessage getCustomerDetails(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			CustomerDAO lCustomerDAO = new RDBCustomerDAO(mDBConnection);

			int lIDCustomer = MessageProtocolSavings.GetCustomerDetails.
					getRequestIDCustomer(aRequest);

			data.Customer lCustomer = lCustomerDAO.get(lIDCustomer);

			aResponse = MessageProtocolSavings.GetCustomerDetails.
					formatResponseMessage(
					aResponse, lCustomer.getFirstName(),
					lCustomer.getLastName(),
					lCustomer.getDateOfBirth(),
					lCustomer.getAddress());

			return aResponse;
		}
		catch (Exception aException)
		{
			return null;
		}
	}

	public ObjectMessage getSavingsDetails(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

			int lIDSavings = MessageProtocolSavings.GetSavingsDetails.
					getRequestIDSavings(aRequest);

			data.Savings lSavings = lSavingsDAO.get(lIDSavings);

			aResponse = MessageProtocolSavings.GetSavingsDetails.
					formatResponseMessage(aResponse,
					lSavings.getIDCustomer(),
					lSavings.getBalance());

			return aResponse;
		}
		catch (Exception aException)
		{
			return null;
		}
	}

	public ObjectMessage getSavingsIdList(ObjectMessage aRequest,
			ObjectMessage aResponse)
	{
		try
		{
			SavingsDAO lSavingsDAO = new RDBSavingsDAO(mDBConnection);

			int lIDCustomer = MessageProtocolSavings.GetSavingsIdList.
					getRequestIDCustomer(aRequest);

			List<Integer> lIdList = lSavingsDAO.getSavingsIdList(lIDCustomer);

			aResponse = MessageProtocolSavings.GetSavingsIdList.
					formatResponseMessage(aResponse,
					lIdList);

			return aResponse;
		}
		catch (Exception aException)
		{
			return null;
		}
	}
}
