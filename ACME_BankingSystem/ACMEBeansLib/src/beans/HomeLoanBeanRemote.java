package beans;

import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote bean interface for subsystem 2.
 * 
 * @author Peter (s3286430)
 */
@Remote
public interface HomeLoanBeanRemote
{
	public boolean authenticateLogin(int aIDCustomer, String aPassword)
			throws ApplicationLogicException;

	public entitydata.Customer getCustomerDetails(int aIDCustomer) throws ApplicationLogicException;

	public entitydata.Savings getSavingsDetails(int aIDSavings) throws ApplicationLogicException;

	public entitydata.HomeLoan getHomeLoanDetails(long aIDHomeLoan) throws ApplicationLogicException;

	public long createHomeLoanAccount(int aIDCustomer, BigDecimal aAmountBorrowed,
			String aCurrentJob, String aSalaryPerYear, String aContactPhone,
			String aContactEmail,
			int aPreferredContactType)
			throws ApplicationLogicException;

	public void repayHomeLoanAccount(int aIDCustomer, long aHomeLoanId, int aIDSavings,
			BigDecimal aAmount)
			throws ApplicationLogicException;

	public List<Integer> getSavingsIdList(int aIDCustomer)
			throws ApplicationLogicException;

	public List<Long> getHomeLoanIdList(int aIDCustomer)
			throws ApplicationLogicException;
}
