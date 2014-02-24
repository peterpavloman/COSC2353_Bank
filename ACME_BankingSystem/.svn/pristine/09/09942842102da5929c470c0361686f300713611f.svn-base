package exceptions;

import javax.ejb.ApplicationException;

/**
 * A generic EJB Application exception that represents an error in application logic.
 * 
 * 
 * @author s3286430
 */
@ApplicationException(rollback=true)
public class ApplicationLogicException extends Exception
{
	private String mUserMessage;
	public ApplicationLogicException()
	{	
		mUserMessage = "";
	}
	
	public ApplicationLogicException(String aUserMessage)
	{
		mUserMessage = aUserMessage;
	}
	
	public String getUserMessage()
	{
		return mUserMessage;
	}
}
