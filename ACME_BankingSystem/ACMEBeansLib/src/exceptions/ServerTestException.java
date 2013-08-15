/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author narks
 */
@ApplicationException(rollback=true)
public class ServerTestException extends Exception implements java.io.Serializable
{
	
}
