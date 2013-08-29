package data.access.rdb;

/**
 *
 * @author nanxinglin
 */
import java.sql.*;
import data.*;
import data.access.*;
import exceptions.ApplicationLogicException;

public class RDBTransactionDAO implements TransactionDAO
{

	private Connection connection;

	public RDBTransactionDAO(Connection conn)
	{
		connection = conn;
	}

	@Override
	public void create(Transaction aTransaction) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO ACMEBANK.BANKTRANSACTION(ID_SAVINGS, AMOUNT, DESCRIPTION) VALUES(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, aTransaction.getSavingID());
			statement.setBigDecimal(2, aTransaction.getAmount());
			statement.setString(3, aTransaction.getDescription());

			
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();

			if (!result.next())
			{
				throw new ApplicationLogicException(
						"Unable to add new transaction.");
			}

			aTransaction.setID(result.getInt(1));

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public Transaction get(int aIDTransaction) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM ACMEBANK.BANKTRANSACTION WHERE ID_BANKTRANSACTION=?");

			statement.setInt(1, aIDTransaction);
			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				return new Transaction(result.getInt(1), result.getInt(2),
						result.getBigDecimal(3), result.getString(4));
			}
			else
			{
				throw new ApplicationLogicException("Invalid transation ID.");
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
	public void update(Transaction aTransaction) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE ACMEBANK.BANKTRANSCATION SET ID_SAVINGS= ?, AMOUNT= ?, DESCRIPTION= ? WHERE ID_BANKTRANSACTION= ?");

			statement.setInt(1, aTransaction.getID());
			statement.setBigDecimal(2, aTransaction.getAmount());
			statement.setString(3, aTransaction.getDescription());

			if (statement.executeUpdate() != 1)
			{
				throw new ApplicationLogicException(
						"Unable to update transaction.");
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
	public void delete(Transaction aTransaction) throws ApplicationLogicException
	{
		this.delete(aTransaction.getID());
	}

	@Override
	public void delete(int id) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"DELETE FROM ACMEBANK.BANKTRANSACTION WHERE ID_BANKTRANSACTION= ?");

			statement.setInt(1, id);

			if (statement.executeUpdate() != 1)
			{
				throw new ApplicationLogicException(
						"Unable to remove transation.");
			}

		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}
}
