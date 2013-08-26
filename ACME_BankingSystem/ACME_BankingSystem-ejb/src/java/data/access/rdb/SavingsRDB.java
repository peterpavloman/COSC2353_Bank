package data.access.rdb;

/**
 *
 * @author nanxinglin
 */
import java.sql.*;
import data.*;
import data.access.*;
import exceptions.ApplicationLogicException;

public class SavingsRDB implements SavingsDAO{
	private Connection connection;

	public SavingsRDB(Connection conn){ connection= conn; }

	@Override
	public void create(Savings aSavings) throws ApplicationLogicException{
		try{
			PreparedStatement statement= connection.prepareStatement(
				"INSERT INTO ACMEBANK.SAVINGS(ID_CUSTOMER, BALANCE) "
				+ "VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, aSavings.getIDCustomer());
			statement.setBigDecimal(2, aSavings.getBalance());

			statement.executeUpdate();

			ResultSet result= statement.getGeneratedKeys();

			if(!result.next())
				throw new ApplicationLogicException("ERROR: Unable to add new saving account.");

			aSavings.setIDCustomer(result.getInt(1));

		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new ApplicationLogicException("SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public Savings get(int aIDSavings) throws ApplicationLogicException{
		try{
			PreparedStatement statement= connection.prepareStatement(
				"SELECT ID_CUSTOMER, BALANCE FROM ACMEBANK.SAVINGS WHERE ID_SAVINGS=?");

			statement.setInt(1, aIDSavings);
			ResultSet result= statement.executeQuery();

			if(result.next())
				return new Savings(result.getInt(1), result.getBigDecimal(2));
			else
				throw new ApplicationLogicException("ERROR: Invalid saving ID.");

		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new ApplicationLogicException("SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public void update(Savings aSavings) throws ApplicationLogicException{
		try{
			PreparedStatement statement= connection.prepareStatement(
				"UPDATE ACMEBANK.SAVINGS SET ID_CUSTOMER= ?, BALANCE= ? WHERE ID_SAVINGS= ?");

			statement.setInt(1, aSavings.getIDCustomer());
			statement.setBigDecimal(2, aSavings.getBalance());
			statement.setInt(3, aSavings.getIDSavings());

			if(statement.executeUpdate()!= 1)
				throw new ApplicationLogicException("ERROR: Unable to update saving account.");

		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new ApplicationLogicException("SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public void delete(Savings aSavings) throws ApplicationLogicException{
		this.delete(aSavings.getIDSavings());
	}

	public void delete(int id) throws ApplicationLogicException{
		try{
			PreparedStatement statement= connection.prepareStatement(
				"DELETE FROM ACMEBANK.SAVINGS WHERE ID_SAVINGS= ?");

			statement.setInt(1, id);

			if(statement.executeUpdate()!= 1)
				throw new ApplicationLogicException("ERROR: Unable to remove the saving account.");

		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new ApplicationLogicException("SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public int getSavingsAccountCount(int aCustomerId) throws ApplicationLogicException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
