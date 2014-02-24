package entitydata;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * Serializable Customer object.
 * 
 * @author Peter (s3286430)
 */
public class Customer implements Serializable
{
    private String mFirstName;
    private String mLastName;
    private Date mDateOfBirth;
    private String mAddress;
    
    /**
     * Constructor for Customer class.
     * 
     * @param aFirstName
     * @param aLastName
     * @param aDateOfBirth 
     * @param aAddress
     */
    public Customer(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
        mDateOfBirth = aDateOfBirth;
        mAddress = aAddress;
    }
    
    /**
     * 
     * @return
     */
    public String getFirstName() { return mFirstName; }
    /**
     *
     * @return
     */
    public String getLastName() { return mLastName; }
    /**
     *
     * @return
     */
    public Date getDateOfBirth() { return mDateOfBirth; }
    /**
     *
     * @return
     */
    public String getAddress() { return mAddress; }
    
}
