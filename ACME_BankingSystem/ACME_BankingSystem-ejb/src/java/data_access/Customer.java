/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access;

import java.sql.Date;

/**
 *
 * @author narks
 */
public class Customer 
{
    public int mIDCustomer;
    public String mFirstName;
    public String mLastName;
    public Date mDateOfBirth;
    public String mAddress;
    
    public Customer(String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress)
    {
        mFirstName = aFirstName;
        mLastName = aLastName;
        mDateOfBirth = aDateOfBirth;
        mAddress = aAddress;
    }
}
