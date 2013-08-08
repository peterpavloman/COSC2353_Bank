/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import data_access.Customer;
import data_access.CustomerDAO;
import data_access.RDBCustomerDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author narks
 */
@Stateless
public class CustomerBean implements CustomerBeanRemote 
{
    @Resource(lookup="jdbc/ACMEDBDatasource")
    private DataSource mDataSource;
    private Connection mDBConnection;
    
    @PostConstruct
    public void initialize()
    {
        try
        {
            mDBConnection = mDataSource.getConnection();
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
        }
    }
    
    @PreDestroy
    public void close()
    {
        try
        {
            mDBConnection.close();
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
        }
    }

    public void addCustomer(String aFirstName, String aLastName, 
            Date aDateOfBirth, String aAddress) 
    {
        try
        {
            CustomerDAO mDAO = new RDBCustomerDAO(mDBConnection);
            Customer lCustomer = new Customer(aFirstName, aLastName,
                    aDateOfBirth, aAddress);
            mDAO.create(lCustomer);
        }
        catch (Exception aException)
        {
            aException.printStackTrace();
            System.out.println("Could not create customer.");
        }
    }
    
    
}
