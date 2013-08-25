package data.access;

import data.Savings;
import exceptions.ApplicationLogicException;

/**
 *
 * @author narks
 */
public interface SavingsDAO
{
    public void create(Savings aSavings) throws ApplicationLogicException;
    public Savings get(int aIDSavings) throws ApplicationLogicException;
    public void update(Savings aSavings) throws ApplicationLogicException;
    public void delete(Savings aSavings) throws ApplicationLogicException;
	public void delete(int id) throws ApplicationLogicException;
}
