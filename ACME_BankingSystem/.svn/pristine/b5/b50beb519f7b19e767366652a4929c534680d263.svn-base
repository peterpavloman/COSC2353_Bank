package exceptions;

import javax.ejb.ApplicationException;

/**
 * A generic EJB Application exception that represents a login error.
 * 
 * 
 * @author s3286430
 */
@ApplicationException(rollback=true)
public class LoginFailureException extends Exception
{
	private String mUserMessage;
	public LoginFailureException()
	{	
		mUserMessage = "";
	}
	
	public LoginFailureException(String aUserMessage)
	{
		mUserMessage = aUserMessage;
	}
	
	public String getUserMessage()
	{
		return mUserMessage;
	}
}
