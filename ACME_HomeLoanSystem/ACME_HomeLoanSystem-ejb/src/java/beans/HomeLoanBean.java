package beans;

import jpa.entity.HomeLoan;
import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import jms.SavingsProducerBridge;
import jms.SavingsProducerJMS;
import entitydata.Customer;
import entitydata.Savings;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * 
 * @author Peter
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HomeLoanBean implements HomeLoanBeanRemote
{

	private SavingsProducerBridge mSavingsSystem;
	@Resource
	private UserTransaction mTransaction;
	@PersistenceContext(unitName = "ACME_HomeLoanSystem-ejbPU")
	private EntityManager mEntManager;
	@EJB
	private RepayHomeLoanBean mRepayHomeLoan;

	@PostConstruct
	public void postConstruct()
	{
		try
		{
			mSavingsSystem = new SavingsProducerJMS();
		}
		catch (JMSException ex)
		{
			Logger.getLogger(HomeLoanBean.class.getName()).
					log(Level.SEVERE, null, ex);
		}
		catch (NamingException ex)
		{
			Logger.getLogger(HomeLoanBean.class.getName()).
					log(Level.SEVERE, null, ex);
		}
	}

	@PreDestroy
	public void close()
	{
		mSavingsSystem.close();
	}

	@Override
	public long createHomeLoanAccount(int aIDCustomer,
			BigDecimal aAmountBorrowed,
			String aCurrentJob, String aSalaryYear, String aContactPhone,
			String aContactEmail,
			int aContactType)
			throws ApplicationLogicException
	{
		// Check requirements for HomeLoan first

		// Amount borrowed must be > 0
		if (aAmountBorrowed.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new ApplicationLogicException("Invalid loan amount!");
		}

		// Must have a Savings account must exist and must have at least 3 
		// deposits made into it
		if (mSavingsSystem.isCustomerValidForHomeLoan(aIDCustomer) == false)
		{
			throw new ApplicationLogicException(
					"Require at least 3 deposits into a savings account to be eligable for home loans.");
		}

		try
		{
			mTransaction.begin();

			HomeLoan lHomeLoan = new HomeLoan();

			lHomeLoan.setCustomerId(aIDCustomer);
			lHomeLoan.setAmountBorrowed(aAmountBorrowed);
			lHomeLoan.setCurrentJob(aCurrentJob);
			lHomeLoan.setSalaryYear(aSalaryYear);
			lHomeLoan.setContactPhone(aContactPhone);
			lHomeLoan.setContactEmail(aContactEmail);
			lHomeLoan.setContactType(aContactType);

			mEntManager.persist(lHomeLoan);

			mTransaction.commit();
			return lHomeLoan.getId();
		}
		catch (Exception aException)
		{
			Logger.getLogger(HomeLoanBean.class.getName()).
					log(Level.SEVERE, null, aException);
			try
			{
				mTransaction.rollback();
			}
			catch (Exception aException2)
			{
				Logger.getLogger(HomeLoanBean.class.getName()).
						log(Level.SEVERE, null, aException2);
			}
		}

		throw new ApplicationLogicException("Could not create home loan.");
	}

	@Override
	public Customer getCustomerDetails(int aIDCustomer) throws ApplicationLogicException
	{
		return mSavingsSystem.getCustomerDetails(aIDCustomer);
	}

	@Override
	public Savings getSavingsDetails(int aIDSavings) throws ApplicationLogicException
	{
		return mSavingsSystem.getSavingsDetails(aIDSavings);
	}

	@Override
	public entitydata.HomeLoan getHomeLoanDetails(long aIDHomeLoan) throws ApplicationLogicException
	{
		HomeLoan lLoanEnt = mEntManager.find(HomeLoan.class, aIDHomeLoan);
		if (lLoanEnt == null)
		{
			throw new ApplicationLogicException("Home loan does not exist.");
		}
		entitydata.HomeLoan lLoan = new entitydata.HomeLoan(
				lLoanEnt.getCustomerId(),
				lLoanEnt.getCurrentJob(),
				lLoanEnt.getSalaryYear(),
				lLoanEnt.getContactPhone(),
				lLoanEnt.getContactEmail(),
				lLoanEnt.getContactType(),
				lLoanEnt.getAmountBorrowed(),
				lLoanEnt.getAmountRepayed());

		return lLoan;
	}

	@Override
	public List<Integer> getSavingsIdList(int aIDCustomer) throws ApplicationLogicException
	{
		return mSavingsSystem.getSavingsIdList(aIDCustomer);
	}

	@Override
	public List<Long> getHomeLoanIdList(int aIDCustomer) throws ApplicationLogicException
	{
		CriteriaQuery lCritQuery = mEntManager.getCriteriaBuilder().
				createQuery();
		lCritQuery.select(lCritQuery.from(HomeLoan.class));
		Query lQuery = mEntManager.createQuery(lCritQuery);

		List<HomeLoan> lEntList = lQuery.getResultList();
		List<Long> lIdList = new ArrayList<Long>();
		for (HomeLoan lEnt : lEntList)
		{
			if (lEnt.getCustomerId() == aIDCustomer)
			{
				lIdList.add(lEnt.getId());
			}
		}

		return lIdList;
	}

	@Override
	public void repayHomeLoanAccount(int aIDCustomer, long aHomeLoanId,
			int aIDSavings,
			BigDecimal aAmount) throws ApplicationLogicException
	{
		// Check requirements for HomeLoan first

		// Amount repayed must be > 0
		if (aAmount.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new ApplicationLogicException("Invalid repayment amount!");
		}

		// Accounts must actually belong to the customer
		Savings lSavings = mSavingsSystem.getSavingsDetails(aIDSavings);
		if (lSavings.getIDCustomer() != aIDCustomer)
		{
			throw new ApplicationLogicException(
					"This savings account does not belong to the customer!");
		}

		HomeLoan lHomeLoanEnt = mEntManager.find(HomeLoan.class, aHomeLoanId);
		if (lHomeLoanEnt.getCustomerId() != aIDCustomer)
		{
			throw new ApplicationLogicException(
					"This home loan account does not belong to the customer!");
		}

		boolean lResult;
		try
		{
			// Do local first, then remote
			// If local fails, do not do remote
			// If remote fails, roll back local
			mTransaction.begin();

			BigDecimal lAmountRepayed = lHomeLoanEnt.getAmountRepayed();
			if (lAmountRepayed == null)
			{
				lAmountRepayed = BigDecimal.ZERO;
			}

			lHomeLoanEnt.setAmountRepayed(lAmountRepayed.add(aAmount));

			mEntManager.merge(lHomeLoanEnt);

			// Suspend the transaction by calling a different EJB
			// that has no transaction support.
			lResult = mRepayHomeLoan.withdrawFromSavingsAccount(
					aIDSavings,
					aAmount);

			if (lResult == false)
			{
				// The account did not have enough money.
				mTransaction.rollback();
			}
			else
			{
				mTransaction.commit();
			}
		}
		catch (Exception aException)
		{
			Logger.getLogger(HomeLoanBean.class.getName()).
					log(Level.SEVERE, null, aException);
			try
			{
				mTransaction.rollback();
			}
			catch (Exception aException2)
			{
				Logger.getLogger(HomeLoanBean.class.getName()).
						log(Level.SEVERE, null, aException2);
			}
			throw new ApplicationLogicException();
		}
		
		if (lResult == false)
		{
			throw new ApplicationLogicException("Insufficient funds in Savings account.");
		}
		throw new ApplicationLogicException();
	}

	@Override
	public boolean authenticateLogin(int aIDCustomer, String aPassword)
			throws ApplicationLogicException
	{
		return mSavingsSystem.verifyCustomerLogin(aIDCustomer, aPassword);
	}
}
