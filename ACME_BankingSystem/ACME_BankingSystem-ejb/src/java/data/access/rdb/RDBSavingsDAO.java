package data.access.rdb;

/**
 * Relational database implementation of Savings data access object.
 * @author nanxinglin, Peter
 */
import data.Savings;
import java.sql.*;
import data.access.*;
import exceptions.ApplicationLogicException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RDBSavingsDAO implements SavingsDAO
{

	private Connection mDBConnection;

	public RDBSavingsDAO(Connection conn)
	{
		mDBConnection = conn;
	}

	@Override
	public void create(Savings aSavings) throws ApplicationLogicException
	{
		try
		{
			// First, we should check if the customer exists
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"SELECT count(1) FROM "
					+ "ACMEBANK.CUSTOMER WHERE id_customer = ?");
			lStatement.setInt(1, aSavings.getIDCustomer());

			ResultSet lResult = lStatement.executeQuery();
			lResult.next();
			if (lResult.getInt(1) == 0)
			{
				throw new ApplicationLogicException(
						"Customer ID does not exist!");
			}

			lStatement = mDBConnection.prepareStatement(
					"INSERT INTO ACMEBANK.SAVINGS(ID_CUSTOMER, BALANCE) "
					+ "VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

			lStatement.setInt(1, aSavings.getIDCustomer());
			lStatement.setBigDecimal(2, aSavings.getBalance());

			lStatement.executeUpdate();

			lResult = lStatement.getGeneratedKeys();

			if (!lResult.next())
			{
				throw new ApplicationLogicException(
						"Unable to add new saving account.");
			}

			aSavings.setIDSavings(lResult.getInt(1));

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public Savings get(int aIDSavings) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = mDBConnection.prepareStatement(
					"SELECT ID_CUSTOMER, BALANCE FROM ACMEBANK.SAVINGS WHERE ID_SAVINGS = ?");

			statement.setInt(1, aIDSavings);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				Savings lSavings = new Savings(result.getInt(1), result.
						getBigDecimal(2));
				lSavings.setIDSavings(aIDSavings);
				return lSavings;
			}
			else
			{
				throw new ApplicationLogicException("Invalid saving ID.");
			}

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public void update(Savings aSavings) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"UPDATE ACMEBANK.SAVINGS SET ID_CUSTOMER = ?, BALANCE = ? WHERE ID_SAVINGS= ?");

			lStatement.setInt(1, aSavings.getIDCustomer());
			lStatement.setBigDecimal(2, aSavings.getBalance());
			lStatement.setInt(3, aSavings.getIDSavings());

			int lRowsAffected = lStatement.executeUpdate();
			lStatement.close();
			if (lRowsAffected == 0)
			{
				throw new ApplicationLogicException(
						"Unable to update saving account.");
			}

		}
		catch (SQLException aException)
		{
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public void delete(Savings aSavings) throws ApplicationLogicException
	{
		this.delete(aSavings.getIDSavings());
	}

	public void delete(int id) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = mDBConnection.prepareStatement(
					"DELETE FROM ACMEBANK.SAVINGS WHERE ID_SAVINGS= ?");

			statement.setInt(1, id);

			if (statement.executeUpdate() != 1)
			{
				throw new ApplicationLogicException(
						"Unable to remove the saving account.");
			}

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public int getSavingsAccountCount(int aCustomerId) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"SELECT COUNT(*) FROM ACMEBANK.SAVINGS WHERE ID_CUSTOMER = ?");

			lStatement.setInt(1, aCustomerId);

			ResultSet lResult = lStatement.executeQuery();
			lResult.next();
			return lResult.getInt(1);

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown when fetching savings account count.");
		}
	}

	@Override
	public List<Integer> getSavingsIdList(int aIDCustomer) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"SELECT ID_SAVINGS FROM ACMEBANK.SAVINGS WHERE ID_CUSTOMER = ?");

			lStatement.setInt(1, aIDCustomer);

			ResultSet lResult = lStatement.executeQuery();
			
			ArrayList<Integer> lIdList = new ArrayList<Integer>();
			while (lResult.next())
			{
				lIdList.add(lResult.getInt(1));
			}
			return lIdList;

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown when fetching savings account count.");
		}
	}

	@Override
	public int getDepositCount(int aIDSavings) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"SELECT COUNT(*) FROM ACMEBANK.BANKTRANSACTION WHERE ID_SAVINGS = ? AND AMOUNT > 0");

			lStatement.setInt(1, aIDSavings);

			ResultSet lResult = lStatement.executeQuery();
			lResult.next();
			return lResult.getInt(1);

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown when fetching savings account deposit count.");
		}
	}
	
	@Override
	public BigDecimal getTotalSavingsBalance() throws ApplicationLogicException
	{
		try
		{
			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"SELECT SUM(BALANCE) FROM ACMEBANK.SAVINGS");

			ResultSet lResult = lStatement.executeQuery();
			lResult.next();
			return lResult.getBigDecimal(1);

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown when fetching savings account balance.");
		}
	}
}
