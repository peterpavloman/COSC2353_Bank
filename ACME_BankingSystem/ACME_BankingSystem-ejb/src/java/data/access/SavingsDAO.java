package data.access;

import data.Savings;
import exceptions.ApplicationLogicException;

/**
 *
 * @author s3286430
 */
public interface SavingsDAO
{
    public void create(Savings aSavings) throws ApplicationLogicException;
    public Savings get(int aIDSavings) throws ApplicationLogicException;
    public void update(Savings aSavings) throws ApplicationLogicException;
    public void delete(Savings aSavings) throws ApplicationLogicException;
	public void delete(int id) throws ApplicationLogicException;
	
	public int getSavingsAccountCount(int aCustomerId) 
			throws ApplicationLogicException;
}
