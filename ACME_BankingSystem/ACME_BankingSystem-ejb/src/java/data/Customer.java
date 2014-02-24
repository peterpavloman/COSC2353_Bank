package data;

import exceptions.ApplicationLogicException;
import java.sql.Date;

/**
 * Data object representing a Customer account.
 * 
 * @author Peter
 */
public class Customer 
{
    private int mIDCustomer;
    private String mFirstName;
    private String mLastName;
    private Date mDateOfBirth;
    private String mAddress;
	private String mPassword;
    
    /**
     * Customer IDs are not initialized in constructor, as we assume
	 * that a Customer object is created and assigned an ID when added
	 * by a data access object.
	 * Note that any changes to the object are not persisted unless
	 * the object is explicitly saved through a data access object.
     * 
     * @param aFirstName Given name of customer. Required, 100 char max.
     * @param aLastName Surname of customer. Required, 100 char max.
     * @param aDateOfBirth Date of birth of customer, as a java.sql.Date. Required.
     * @param aAddress Address of the customer. Required, 128 char max.
	 * @param aPassword Customer password (in plaintext). Required, 64 char max.
     */
    public Customer(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress, String aPassword)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
        mDateOfBirth = aDateOfBirth;
        mAddress = aAddress;
		
		// TODO: Do not store password as plaintext!
		mPassword = aPassword;
    }
    
    /**
     * Gets the given name (first name) of the customer.
     * @return String representing given name of the customer.
	 * @see setFirstName()
     */
    public String getFirstName() { return mFirstName; }
	
    /**
     * Sets the given name (first name) of the customer.
	 * Required field for customer, 100 char max.
     * @param aFirstName String representing given name of the customer.
     * @see getFirstName()
     */
    public void setFirstName(String aFirstName) throws ApplicationLogicException
    {
		if (aFirstName.length() <= 0 || aFirstName.length() > 100)
		{
			throw new ApplicationLogicException("First name is invalid length.");
		}
        mFirstName = aFirstName;
    }
	
    /**
     * Gets the surname (last name) of the customer.
     * @return String representing surname of the customer.
	 * @see setLastName()
     */
    public String getLastName() { return mLastName; }
    
	/**
     * Sets the surname (last name) of the customer.
	 * Required field for customer, 100 char max.
     * @param aLastName String representing surname of the customer.
     * @see getLastName()
     */
    public void setLastName(String aLastName) throws ApplicationLogicException
    {
		if (aLastName.length() <= 0 || aLastName.length() > 100)
		{
			throw new ApplicationLogicException("Last name is invalid length.");
		}
        mLastName = aLastName;
    }
	
    /**
     * Gets the customer's date of birth.
     * @return SQL Date representing date of birth of the customer.
     */
    public Date getDateOfBirth() { return mDateOfBirth; }
    
	/**
     * Sets the customer's date of birth.
	 * Required field for customer.
     * @param aDateOfBirth SQL Date representing date of birth of the customer.
     */
    public void setDateOfBirth(Date aDateOfBirth)
    {
        mDateOfBirth = aDateOfBirth;
    }
	
    /**
     * Gets the customer's address.
     * @return String representing address of the customer.
     */
    public String getAddress() { return mAddress; }
    /**
     * Sets the customer's address.
	 * Required field for customer, 128 char max.
     * @param aAddress String representing address of the customer.
     */
	
    public void setAddress(String aAddress) 
    {
        mAddress = aAddress;
    } 
	
    /**
     * Gets unique customer ID.
     * @return
     */
    public int getIDCustomer() { return mIDCustomer; }
	
    /**
     * Sets unique customer ID.
     * @param aIDCustomer
     */
    public void setIDCustomer(int aIDCustomer)
    {
        mIDCustomer = aIDCustomer;
    }
	
	/**
	 * Gets the customer's password.
	 * @return Customer's password as plaintext.
	 */
	public String getPassword() 
	{ 
		// TODO: Deprecate this function and store passwords as a hash!
		return mPassword; 
	}

	/**
	 * Sets the customer's password.
	 * Required field for customer, 64 char max.
	 * @param aPassword Customer's password as plaintext.
	 */
	public void setPassword(String aPassword) { mPassword = aPassword; }
    
}
