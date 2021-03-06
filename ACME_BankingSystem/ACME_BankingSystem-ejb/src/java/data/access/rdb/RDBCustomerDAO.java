package data.access.rdb;

import data.Customer;
import data.access.CustomerDAO;
import exceptions.ApplicationLogicException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Relational database implementation of Customer data access object.
 * @author Peter
 */
public class RDBCustomerDAO implements CustomerDAO
{

	private Connection mDBConnection;

	public RDBCustomerDAO(Connection aDBConnection)
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
					+ "dateofbirth, address, password) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			lStatement.setString(1, aCustomer.getFirstName());
			lStatement.setString(2, aCustomer.getLastName());
			lStatement.setString(3, aCustomer.getDateOfBirth().toString());
			lStatement.setString(4, aCustomer.getAddress());
			lStatement.setString(5, aCustomer.getPassword());

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
					"SELECT firstname, lastname, dateofbirth, address, password FROM "
					+ "ACMEBANK.CUSTOMER WHERE id_customer = ?");

			statement.setInt(1, aIDCustomer);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				Customer lCustomer = new Customer(result.getString(1), result.getString(2),
						result.getDate(3), result.getString(4), result.getString(5));
				lCustomer.setIDCustomer(aIDCustomer);
				
				return lCustomer;
			}
			else
			{
				throw new ApplicationLogicException(
						"ERROR: Invalid customer ID.");
			}
		}
		catch (SQLException aException)
		{
			aException.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL Exception thrown.");
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
					+ "dateofbirth=?, address=?, password=? WHERE id_customer=?");

			lStatement.setString(1, aCustomer.getFirstName());
			lStatement.setString(2, aCustomer.getLastName());
			lStatement.setDate(3, aCustomer.getDateOfBirth());
			lStatement.setString(4, aCustomer.getAddress());
			lStatement.setString(5, aCustomer.getPassword());

			lStatement.setInt(6, aCustomer.getIDCustomer());

			int lRowsAffected = lStatement.executeUpdate();
			lStatement.close();
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
			lStatement.close();
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
