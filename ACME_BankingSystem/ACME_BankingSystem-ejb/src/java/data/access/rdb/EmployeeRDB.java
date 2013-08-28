package data.access.rdb;

import data.Employee;
import data.access.EmployeeDAO;
import exceptions.ApplicationLogicException;
import java.sql.*;

public class EmployeeRDB implements EmployeeDAO
{

	private Connection connection;

	public EmployeeRDB(Connection conn)
	{
		connection = conn;
	}

	@Override
	public void create(Employee aEmployee) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO ACMEBANK.EMPLOYEE(FIRSTNAME, LASTNAME, PASSWORD) "
					+ "VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, aEmployee.getFirstName());
			statement.setString(2, aEmployee.getLastName());
			statement.setString(3, aEmployee.getPassword());

			statement.executeUpdate();

			ResultSet result = statement.getGeneratedKeys();

			if (!result.next())
			{
				throw new ApplicationLogicException(
						"Unable to add new employee.");
			}

			aEmployee.setIDEmployee(result.getInt(1));
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new ApplicationLogicException(
					"SYSTEM ERROR: SQL exception thrown.");
		}
	}

	@Override
	public Employee get(int aIDEmployee) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement;

			statement = connection.prepareStatement("SELECT FIRSTNAME,"
					+ "LASTNAME, PASSWORD FROM ACMEBANK.EMPLOYEE WHERE ID_EMPLOYEE= ?");

			statement.setInt(1, aIDEmployee);

			ResultSet result = statement.executeQuery();

			if (result.next())
			{
				Employee lEmployee = new Employee(result.getString(1), result.getString(2),
						result.getString(3));
				lEmployee.setIDEmployee(aIDEmployee);
				return lEmployee;
			}
			else
			{
				throw new ApplicationLogicException(
						"Invalid employee id or password.");
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
	public void update(Employee aEmployee) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE ACMEBANK.EMPLOYEE SET FIRSTNAME= ?, LASTNAME= ?,"
					+ "PASSWORD= ? WHERE ID_EMPLOYEE= ?");

			statement.setString(1, aEmployee.getFirstName());
			statement.setString(2, aEmployee.getLastName());
			statement.setString(3, aEmployee.getPassword());
			statement.setInt(4, aEmployee.getIDEmployee());

			if (statement.executeUpdate() != 1)
			{
				throw new ApplicationLogicException("Unable to update employee.");
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
	public void delete(Employee aEmployee) throws ApplicationLogicException
	{
		this.delete(aEmployee.getIDEmployee());
	}

	public void delete(int id) throws ApplicationLogicException
	{
		try
		{
			PreparedStatement statement = connection.prepareStatement(
					"DELETE FROM ACMEBANK.EMPLOYEE WHERE ID_EMPLOYEE= ?");

			statement.setInt(1, id);

			if (statement.executeUpdate() != 1)
			{
				throw new ApplicationLogicException("Unable to remove employee.");
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
