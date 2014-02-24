package jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jms.homeloan.MessageProtocolSavings;

/**
 * Message driven bean that receives messages from the HomeLoan system 
 * and hands them off to a MessageHandler EJB for business logic.
 *
 * @author Peter
 */

@MessageDriven(activationConfig =
{
	@ActivationConfigProperty(propertyName = "destinationType",
			propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destinationLookup",
			propertyValue = "jms/ACMESavingsQueue")
})

public class HomeLoanMDB implements MessageListener
{
	private QueueConnectionFactory mConnectionFactory;
	
	@EJB
	private HomeLoanMessageHandler mMessageHandler;
	
	public HomeLoanMDB() throws NamingException
	{
		Context lJDNIContext = new InitialContext();
		
		mConnectionFactory = (QueueConnectionFactory) lJDNIContext.lookup(
				"jms/ACMESavingsQueueConnectionFactory");
	}
	
	/**
	 * Takes a received ObjectMessage from the HomeLoan system, hands it to a 
	 * MessageHandler EJB that forms a response using the Savings system, 
	 * then returns the formatted response.
	 * @param aReceivedMessage Message received from the HomeLoan system.
	 * @param aSession Connection session of received message.
	 * @return Formatted response message. Can be null if message is not
	 * recognized.
	 * @throws JMSException 
	 */
	private ObjectMessage getResponseMessage(ObjectMessage aReceivedMessage,
			Session aSession) throws JMSException
	{	
		ObjectMessage lResponse;
		
		Class lResponseType = MessageProtocolSavings.getMessageType(
				aReceivedMessage);
		if (lResponseType == MessageProtocolSavings.GetCustomerDetails.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.getCustomerDetails(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.GetSavingsDetails.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.getSavingsDetails(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.GetSavingsIdList.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.getSavingsIdList(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.AuthenticateLogin.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.authenticateLogin(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.IsCustomerValidForHomeLoan.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.isCustomerValidForHomeLoan(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.WithdrawFromSavingsAccount.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.withdrawFromSavingsAccount(aReceivedMessage, lResponse);
			return lResponse;
		}
		else if (lResponseType == MessageProtocolSavings.GetTotalSavingsBalance.class)
		{
			lResponse = aSession.createObjectMessage();
			
			mMessageHandler.getTotalSavingsBalance(aReceivedMessage, lResponse);
			return lResponse;
		}	
		
		return null;
	}
	
	/**
	 * Takes a received Message from the HomeLoan system, processes the message
	 * using the Savings system, and sends a response synchronously if
	 * appropriate.
	 * @param aMessage Message received from the HomeLoan system.
	 */
	@Override
	public void onMessage(Message aMessage)
	{
		try
		{
			Destination aDestination = aMessage.getJMSReplyTo();
			if (aDestination != null)
			{
				Connection lConnection = mConnectionFactory.createConnection();				
				Session lSession = lConnection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				
				ObjectMessage lResponseMsg = getResponseMessage(
						(ObjectMessage) aMessage, lSession);
				
				if (lResponseMsg != null)
				{
					MessageProducer lProducer = lSession.createProducer(
							aDestination);
					
					lResponseMsg.setJMSCorrelationID(aMessage.
							getJMSCorrelationID());
					
					lProducer.send(lResponseMsg);
					lProducer.close();
					
				}

				lSession.close();
				lConnection.close();
			}
		}
		catch (JMSException ex)
		{
			Logger.getLogger(HomeLoanMDB.class.getName()).
					log(Level.SEVERE, null, ex);
		}
		
	}
}
