package data.access.rdb;

import data.Customer;
import data.access.CustomerDAO;
import exceptions.ApplicationLogicException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Relational database implementation of Customer DAO.
 *
 * @author s3286430
 */

public class CustomerRBD implements CustomerDAO
{
    private Connection mDBConnection;

    public CustomerRBD(Connection aDBConnection)
    {
        mDBConnection = aDBConnection;
    }

	@Override
    public void create(Customer aCustomer) throws ApplicationLogicException
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
            throw new ApplicationLogicException("Could not add new customer.");
        }
    }

    @Override
    public Customer get(int aIDCustomer) throws ApplicationLogicException
    {
        try
        {
            PreparedStatement statement = mDBConnection.prepareStatement(
                    "SELECT firstname, lastname, dateofbirth, address FROM "
					+ "ACMEBANK.CUSTOMER WHERE id_customer = ?;");

            statement.setInt(1, aIDCustomer);
            ResultSet result = statement.executeQuery();

			if(result.next())
				return new Customer(result.getString(1), result.getString(2), result.getDate(3), result.getString(4));
			else
				throw new ApplicationLogicException("ERROR: Invalid customer ID.");
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            throw new ApplicationLogicException("SYSTEM ERROR: SQL Exception thrown.");
        }
    }

    @Override
    public void update(Customer aCustomer) throws ApplicationLogicException
    {
        try
        {
			/*
			 * UPDATE ACMEBANK.CUSTOMER SET "FIRSTNAME" = 'Test', "LASTNAME" = 'Me' WHERE ID_CUSTOMER = 2;
			 */
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "UPDATE ACMEBANK.CUSTOMER SET firstname=?, lastname=?, "
					+ "dateofbirth=?, address=? WHERE id_customer=?;");

            lStatement.setString(1, aCustomer.getFirstName());
            lStatement.setString(2, aCustomer.getLastName());
            lStatement.setDate(3, aCustomer.getDateOfBirth());
            lStatement.setString(4, aCustomer.getAddress());

			lStatement.setInt(5, aCustomer.getIDCustomer());

            int lRowsAffected = lStatement.executeUpdate();
			if (lRowsAffected != 1)
			{
				throw new ApplicationLogicException("Could not update customer.");
			}
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not update customer.");
            throw new ApplicationLogicException("Could not update customer.");
        }
    }

    @Override
    public void delete(Customer aCustomer) throws ApplicationLogicException
    {
        try
        {
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "DELETE FROM ACMEBANK.CUSTOMER WHERE id_customer=?");
            lStatement.setInt(1, aCustomer.getIDCustomer());

            int lRowsAffected = lStatement.executeUpdate();
			if (lRowsAffected != 1)
			{
				throw new ApplicationLogicException("Could not delete customer.");
			}
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not delete customer.");
            throw new ApplicationLogicException("Could not delete customer.");
        }
    }
}
