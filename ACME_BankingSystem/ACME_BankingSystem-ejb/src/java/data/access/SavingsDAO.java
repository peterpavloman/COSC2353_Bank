package data.access;

import data.Savings;
import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Peter
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
	
	public List<Integer> getSavingsIdList(int aIDCustomer)
			throws ApplicationLogicException;
	
	public int getDepositCount(int aIDSavings) throws ApplicationLogicException;
	
	public BigDecimal getTotalSavingsBalance() throws ApplicationLogicException;
}
