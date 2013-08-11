package beans;

import data.Customer;
import data.access.CustomerDAO;
import data.access.rdb.RDBCustomerDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 * Server-side implementation of a Stateless Session Bean that holds 
 * Customer business logic.
 * 
 * @author Peter (s3286430)
 */
@Stateless
public class CustomerBean implements CustomerBeanRemote
{

    @Resource(lookup = "jdbc/ACMEDBDatasource")
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

    public void create(String aFirstName, String aLastName,
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
            // May need to throw exception here
            // How do we get the client to catch the exception?
        }
    }

    @Override
    public void update(int aIDCustomer, String aFirstName, String aLastName,
            Date aDateOfBirth, String aAddress)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int aIDCustomer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
