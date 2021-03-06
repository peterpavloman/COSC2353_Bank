package data;

import java.math.BigDecimal;

/**
 * Savings object used by application logic.
 * Savings represent a savings account. Customers own saving accounts.
 * 
 * @author Peter (s3286430)
 */
public class Savings
{
    private BigDecimal mBalance;
	private int mIDCustomer;
	private int mIDSavings;
	
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
    /**
     * Mutator for balance in the account (cash).
     * Uses BigDecimal for precision. Floating point numbers are not precise
	 * which is not suitable for cash.
	 * Note that changes must be explicitly saved via a data access object.
     * @param aBalance The new balance for the account.
     * @see getBalance()
     */
    public void setBalance(BigDecimal aBalance)
    {
        mBalance = aBalance;
    }
	
	
	public int getIDCustomer() { return mIDCustomer; }
    public void setIDCustomer(int aIDCustomer)
    {
        mIDCustomer = aIDCustomer;
    }
	
	public int getIDSavings() { return mIDSavings; }
    public void setIDSavings(int aIDSavings)
    {
        mIDSavings = aIDSavings;
    }
}
