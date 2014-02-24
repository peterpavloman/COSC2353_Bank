package jms.homeloan;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * Format and parses JMS messages sent between the HomeLoan system and the
 * Savings system.
 *
 * @author Peter (s3286430)
 */
public class MessageProtocolSavings
{
			
	public static class GetTotalSavingsBalance
	{
		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetTotalSavingsBalance");
			aMessage.setObject(lMap);
			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage, BigDecimal aBalance)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetTotalSavingsBalance");
			lMap.put("Balance", aBalance);
			aMessage.setObject(lMap);

			return aMessage;
		}
		
		public static BigDecimal getResponseBalance(ObjectMessage aMessage)
				throws JMSException
		{
			return (BigDecimal) ((HashMap) aMessage.getObject()).get("Balance");
		}
	}		
	
	public static class WithdrawFromSavingsAccount
	{
		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDSavings, BigDecimal aAmount) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "WithdrawFromSavingsAccount");
			lMap.put("IDSavings", aIDSavings);
			lMap.put("Amount", aAmount);
			aMessage.setObject(lMap);
			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage, boolean aSuccess)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "WithdrawFromSavingsAccount");
			// hacky :D
			int lFakeBoolean = 0;
			if (aSuccess)
			{
				lFakeBoolean = 1;
			}
			lMap.put("Success", lFakeBoolean);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDSavings(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDSavings");
		}
		
		public static BigDecimal getRequestAmount(ObjectMessage aMessage)
				throws JMSException
		{
			return (BigDecimal) ((HashMap) aMessage.getObject()).get("Amount");
		}

		public static boolean getResponseSuccess(ObjectMessage aMessage)
				throws JMSException
		{
			int lFakeBoolean = (int) ((HashMap) aMessage.getObject()).get(
					"Success");
			if (lFakeBoolean == 1) { return true; }
			return false;
		}
	}
	
	public static class IsCustomerValidForHomeLoan
	{
		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDCustomer) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "IsCustomerValidForHomeLoan");
			lMap.put("IDCustomer", aIDCustomer);
			aMessage.setObject(lMap);
			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage, boolean aIsValid)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "IsCustomerValidForHomeLoan");
			// hacky :D
			int lFakeBoolean = 0;
			if (aIsValid)
			{
				lFakeBoolean = 1;
			}
			lMap.put("IsValid", lFakeBoolean);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDCustomer(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDCustomer");
		}

		public static boolean getResponseIsValid(ObjectMessage aMessage)
				throws JMSException
		{
			int lFakeBoolean = (int) ((HashMap) aMessage.getObject()).get(
					"IsValid");
			if (lFakeBoolean == 1) { return true; }
			return false;
		}
	}
	
	public static class AuthenticateLogin
	{

		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDCustomer, String aPassword) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "AuthenticateLogin");
			lMap.put("IDCustomer", aIDCustomer);
			lMap.put("Password", aPassword);
			aMessage.setObject(lMap);
			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage, boolean aLoginSuccess)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "AuthenticateLogin");
			// hacky :D
			int lFakeBoolean = 0;
			if (aLoginSuccess)
			{
				lFakeBoolean = 1;
			}
			lMap.put("LoginSuccess", lFakeBoolean);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDCustomer(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDCustomer");
		}

		public static String getRequestPassword(ObjectMessage aMessage)
				throws JMSException
		{
			return (String) ((HashMap) aMessage.getObject()).get("Password");
		}

		public static boolean getResponseLoginSuccess(ObjectMessage aMessage)
				throws JMSException
		{
			int lFakeBoolean = (int) ((HashMap) aMessage.getObject()).get(
					"LoginSuccess");
			if (lFakeBoolean == 1) { return true; }
			return false;
		}
	}

	public static class GetCustomerDetails
	{

		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDCustomer) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetCustomerDetails");
			lMap.put("IDCustomer", aIDCustomer);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage,
				String aFirstName, String aLastName, Date aDate, String aAddress)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetCustomerDetails");
			lMap.put("FirstName", aFirstName);
			lMap.put("LastName", aLastName);
			lMap.put("Date", aDate);
			lMap.put("Address", aAddress);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDCustomer(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDCustomer");
		}

		public static String getResponseFirstName(ObjectMessage aMessage)
				throws JMSException
		{
			return (String) ((HashMap) aMessage.getObject()).get("FirstName");
		}

		public static String getResponseLastName(ObjectMessage aMessage)
				throws JMSException
		{
			return (String) ((HashMap) aMessage.getObject()).get("LastName");
		}

		public static Date getResponseDate(ObjectMessage aMessage)
				throws JMSException
		{
			return (Date) ((HashMap) aMessage.getObject()).get("Date");
		}

		public static String getResponseAddress(ObjectMessage aMessage)
				throws JMSException
		{
			return (String) ((HashMap) aMessage.getObject()).get("Address");
		}
	}

	public static class GetSavingsDetails
	{

		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDSavings) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetSavingsDetails");
			lMap.put("IDSavings", aIDSavings);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage,
				int aIDCustomer, BigDecimal aBalance)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetSavingsDetails");
			lMap.put("IDCustomer", aIDCustomer);
			lMap.put("Balance", aBalance);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDSavings(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDSavings");
		}

		public static int getResponseIDCustomer(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDCustomer");
		}

		public static BigDecimal getResponseBalance(ObjectMessage aMessage)
				throws JMSException
		{
			return (BigDecimal) ((HashMap) aMessage.getObject()).get("Balance");
		}
	}

	public static class GetSavingsIdList
	{

		public static ObjectMessage formatRequestMessage(ObjectMessage aMessage,
				int aIDCustomer) throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetSavingsIdList");
			lMap.put("IDCustomer", aIDCustomer);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static ObjectMessage formatResponseMessage(ObjectMessage aMessage,
				List<Integer> aSavingsIDList)
				throws JMSException
		{
			HashMap lMap = new HashMap();
			lMap.put("MethodType", "GetSavingsIdList");
			lMap.put("SavingsIdList", aSavingsIDList);
			aMessage.setObject(lMap);

			return aMessage;
		}

		public static int getRequestIDCustomer(ObjectMessage aMessage)
				throws JMSException
		{
			return (int) ((HashMap) aMessage.getObject()).get("IDCustomer");
		}

		public static List<Integer> getResponseSavingsIdList(
				ObjectMessage aMessage)
				throws JMSException
		{
			return (List<Integer>) ((HashMap) aMessage.getObject()).get(
					"SavingsIdList");
		}
	}

	public static Class getMessageType(ObjectMessage aMessage) throws JMSException
	{
		String mMethodType = (String) ((HashMap) aMessage.getObject()).get(
				"MethodType");

		switch (mMethodType)
		{
			case "GetCustomerDetails":
				return GetCustomerDetails.class;
			case "GetSavingsDetails":
				return GetSavingsDetails.class;
			case "GetSavingsIdList":
				return GetSavingsIdList.class;
			case "AuthenticateLogin":
				return AuthenticateLogin.class;
			case "IsCustomerValidForHomeLoan":
				return IsCustomerValidForHomeLoan.class;
			case "WithdrawFromSavingsAccount":
				return WithdrawFromSavingsAccount.class;
			case "GetTotalSavingsBalance":
				return GetTotalSavingsBalance.class;
		}
		// Not exactly the right exception to throw here, but...
		throw new JMSException("Invalid message type!");
	}
}
