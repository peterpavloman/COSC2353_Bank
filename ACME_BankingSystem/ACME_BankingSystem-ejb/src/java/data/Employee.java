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
	// TODO: passwords plaintext, refactor later
	private String mPassword;

    /**
     * Employee IDs are not initialized in constructor, as we assume
	 * that a Employee object is created and assigned an ID when added
	 * by a data access object.
	 * Note that any changes to the object are not persisted unless
	 * the object is explicitly saved through a data access object.s.
     *
     * @param aFirstName Given name of employee. Required, 100 char max.
     * @param aLastName Surname of employee. Required, 100 char max.
	 * @param aPassword Password (in plaintext - TODO deprecate and use hashes),
	 * required, 64 char max.
	 */
    public Employee(String aFirstName, String aLastName, String aPassword)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
		mPassword = aPassword;
    }
	
	
	/**
     * Accessor for the given name (first name) of the employee.
     * @param aFirstName Given name of employee. Required, 100 char max.
     * @see setFirstName()
     */
    public String getFirstName() { return mFirstName; }
    /**
     * Mutator for the given name of the employee.
     * @return Given name of employee. Required, 100 char max.
	 * @see getFirstName()
     */
    public void setFirstName(String aFirstName)
    {
        mFirstName = aFirstName;
    }
    
	/**
	 * Accessor for the surname of the employee.
	 * @return Surname of employee. Required, 100 char max.
	 */
    public String getLastName() { return mLastName; }

	/**
	 * Mutator for the surname of the employee.
	 * @param aLastName Surname of employee. Required, 100 char max.
	 */
    public void setLastName(String aLastName)
    {
        mLastName = aLastName;
    }
	

	/**
     * Gets unique employee ID.
	 * @return 
	 */
	public int getIDEmployee() { return mIDEmployee; }
	/**
     * Sets unique employee ID.
     * @return
     */
    public void setIDEmployee(int aIDEmployee)
    {
        mIDEmployee = aIDEmployee;
    }
	
	/**
	 * Accessor for employee password (stored in plaintext, TODO: make more secure).
	 * @return Password, required, 64 char max.
	 */
    public String getPassword() { return mPassword; }

	/**
	 * Mutator for employee password (stored in plaintext, TODO: make more secure).
	 * @return Password, required, 64 char max.
	 */
	public void setPassword(String aPassword)
	{
		setPassword(aPassword);
	}
}

