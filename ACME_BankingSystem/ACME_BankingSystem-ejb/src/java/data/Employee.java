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
	// Probably not the best idea to have passwords stored as plaintext...
	private String mPassword;

    /**
     * Constructor for Employee class.
	 * Initializes default required values.
     *
     * @param aFirstName
     * @param aLastName
	 * @param aPassword Password (in plaintext - must change later to something
	 * a little more secure when we cover security in class)
     */
    public Employee(String aFirstName, String aLastName, String aPassword)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
		mPassword = aPassword;
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
    public String getPassword() { return mPassword; }
	public void setPassword(String aPassword)
	{
		mPassword = aPassword;
	}
}

