package data;

/**
 * Employee object used by application logic.
 * 
 * @author Peter (s3286430
 */
public class Employee 
{
    private int mIDEmployee;
    private String mFirstName;
    private String mLastName;
    
    /**
     * Constructor for Employee class.
	 * Initializes default required values.
     * 
     * @param aFirstName
     * @param aLastName
     */
    public Employee(String aFirstName, String aLastName)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
    }
    
    /**
     * 
     * @return
     */
    public String getFirstName() { return mFirstName; }
    /**
     * Mutator for FirstName.
     * Note that changes must be explicitly saved via a data access object.
     * @param aFirstName
     * @see getFirstName()
     */
    public void setFirstName(String aFirstName)
    {
        mFirstName = aFirstName;
    }
    /**
     *
     * @return
     */
    public String getLastName() { return mLastName; }
    /**
     * Mutator for LastName.
     * Note that changes must be explicitly saved via a data access object.
     * @param aLastName
     * @see getLastName()
     */
    public void setLastName(String aLastName) 
    {
        mLastName = aLastName;
    }
	public int getIDEmployee() { return mIDEmployee; }
    /**
     *
     * @param aIDEmployee
     */
    public void setIDEmployee(int aIDEmployee)
    {
        mIDEmployee = aIDEmployee;
    }
    
}

