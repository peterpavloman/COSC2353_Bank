package beans;

import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.naming.NamingException;
import jms.SavingsProducerBridge;
import jms.SavingsProducerJMS;

/**
 * Used to suspend a transaction when repaying a home loan. Allows us to get a
 * response when we send a request via JMS for paying off a home loan, while
 * having transactional support.
 *
 * based off:
 * http://frankkieviet.blogspot.com.au/2006/08/jms-requestreply-from-ejb.html
 *
 * @author Peter (s3286430)
 */
@Stateless
@LocalBean
public class RepayHomeLoanBean
{

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean withdrawFromSavingsAccount(
			int aIDSavings,
			BigDecimal aAmount) throws ApplicationLogicException
	{
		try
		{
			SavingsProducerBridge lSavingsSystem = new SavingsProducerJMS();

			// Amount repayed must be > 0
			if (aAmount.compareTo(BigDecimal.ZERO) <= 0)
			{
				throw new ApplicationLogicException("Invalid repayment amount!");
			}

			boolean lResult = lSavingsSystem.withdrawFromSavingsAccount(
					aIDSavings, aAmount);

			return lResult;
		}
		catch (JMSException ex)
		{
			Logger.getLogger(RepayHomeLoanBean.class.getName()).
					log(Level.SEVERE, null, ex);
			throw new ApplicationLogicException(ex.getMessage());
		}
		catch (NamingException ex)
		{
			Logger.getLogger(RepayHomeLoanBean.class.getName()).
					log(Level.SEVERE, null, ex);
			throw new ApplicationLogicException(ex.getMessage());
		}		
	}
}
