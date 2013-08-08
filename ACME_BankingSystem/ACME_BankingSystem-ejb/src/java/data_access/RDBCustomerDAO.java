/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author narks
 */
public class RDBCustomerDAO implements CustomerDAO
{
    private Connection mDBConnection;
    
    public RDBCustomerDAO(Connection aDBConnection)
    {
        mDBConnection = aDBConnection;
    }
    
    public void create(Customer aCustomer) 
    {
        try
        {
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "INSERT INTO ACMEBANK.CUSTOMER(firstname, lastname,"
                    + "dateofbirth, address) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            
            lStatement.setString(1, aCustomer.mFirstName);
            lStatement.setString(2, aCustomer.mLastName);
            lStatement.setString(3, aCustomer.mDateOfBirth.toString());
            lStatement.setString(4, aCustomer.mAddress);
            
            lStatement.executeUpdate();
            
            ResultSet lResult = lStatement.getGeneratedKeys();
            lResult.next();
            
            aCustomer.mIDCustomer = lResult.getInt(1);
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not add new customer.");
        }
    }

    @Override
    public String getFirstName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFirstName(String aFirstName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
