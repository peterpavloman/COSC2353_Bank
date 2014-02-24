package entitydata;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * Serializable Savings object.`
 * 
 * @author Peter (s3286430)
 */
public class Savings implements Serializable
{
	private BigDecimal mBalance;
	private int mIDCustomer;
	
	public Savings(int aIDCustomer, BigDecimal aBalance)
	{
		mIDCustomer = aIDCustomer;
		mBalance = aBalance;
	}
	
	/**
     * Accessor for balance in the account (cash).
	 * Uses BigDecimal for precision. Floating point numbers are not precise
	 * which is not suitable for cash.
	 * 
     * @return
     */
    public BigDecimal getBalance() { return mBalance; }
	
	public int getIDCustomer() { return mIDCustomer; }
}
