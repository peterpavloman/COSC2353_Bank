/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import exceptions.LoginFailureException;

/**
 *
 * @author narks
 */
public class LoginSessionImpl implements LoginSession
{
	private int mIDEmployee;
	private boolean mLoginValid;
	
	public LoginSessionImpl(int aIDEmployee, String aPassword) 
			throws LoginFailureException
	{
		mLoginValid = false;
		// Query the database for username and password
		if (aPassword == "legit")
		{
			mLoginValid = true;
		}
		else
		{
			throw new LoginFailureException();
		}
	}
}
