/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import exceptions.LoginFailureException;
import javax.ejb.Remote;
import security.LoginSession;

/**
 *
 * @author narks
 */
@Remote
public interface EmployeeBeanRemote
{	
	public LoginSession login(int aEmployeeId, String aPassword)
			throws LoginFailureException;
}
