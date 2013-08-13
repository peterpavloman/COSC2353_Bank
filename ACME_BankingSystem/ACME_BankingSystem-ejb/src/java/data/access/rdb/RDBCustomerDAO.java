/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data.access.rdb;

import data.Customer;
import data.access.CustomerDAO;
import java.sql.Connection;
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
            
            lStatement.setString(1, aCustomer.getFirstName());
            lStatement.setString(2, aCustomer.getLastName());
            lStatement.setString(3, aCustomer.getDateOfBirth().toString());
            lStatement.setString(4, aCustomer.getAddress());
            
            lStatement.executeUpdate();
            
            ResultSet lResult = lStatement.getGeneratedKeys();
            lResult.next();
            
            aCustomer.setIDCustomer(lResult.getInt(1));
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not add new customer.");
            // TODO: Throw another exception (we might need to print info to client?)
        }
    }

    @Override
    public Customer get(int aIDCustomer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Customer aCustomer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Customer aCustomer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
