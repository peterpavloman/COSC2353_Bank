package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
public class DebugResetDBSchema implements DebugResetDBSchemaRemote
{

	@Resource(lookup = "jdbc/ACMEDBDatasource")
	private DataSource mDataSource;
	private Connection mDBConnection;

	public void resetTables()
	{
		try
		{
			mDBConnection = mDataSource.getConnection();

			PreparedStatement lStatement = mDBConnection.prepareStatement(
					"DROP TABLE ACMEBANK.BANKTRANSACTION");
			lStatement.executeUpdate();
			lStatement = mDBConnection.prepareStatement(
					"DROP TABLE ACMEBANK.SAVINGS");
			lStatement.executeUpdate();
			lStatement = mDBConnection.prepareStatement(
					"DROP TABLE ACMEBANK.CUSTOMER");
			lStatement.executeUpdate();
			lStatement = mDBConnection.prepareStatement(
					"DROP TABLE ACMEBANK.EMPLOYEE");
			lStatement.executeUpdate();

			lStatement = mDBConnection.prepareStatement(
					"CREATE TABLE ACMEBANK.employee ("
					+ "id_employee INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "firstname varchar(100), lastname varchar(100), password varchar(100))");
			lStatement.executeUpdate();

			lStatement = mDBConnection.prepareStatement(
					"CREATE TABLE ACMEBANK.customer ("
					+ "id_customer INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "firstname varchar(100),"
					+ "lastname varchar(100),"
					+ "dateofbirth date,"
					+ "address varchar(128))");
			lStatement.executeUpdate();

			lStatement = mDBConnection.prepareStatement(
					"CREATE TABLE ACMEBANK.savings ("
					+ "id_savings INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "id_customer INT NOT null,"
					+ "balance DECIMAL(19, 4), FOREIGN KEY (id_customer) REFERENCES ACMEBANK.customer(id_customer))");
			lStatement.executeUpdate();

			lStatement = mDBConnection.prepareStatement(
					"CREATE TABLE ACMEBANK.banktransaction ("
					+ "id_banktransaction INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "id_savings INT NOT null,"
					+ "amount DECIMAL(19, 4),"
					+ "description varchar(256),"
					+ "FOREIGN KEY (id_savings) REFERENCES ACMEBANK.savings(id_savings))");
			lStatement.executeUpdate();

			lStatement = mDBConnection.prepareStatement(
					"INSERT INTO ACMEBANK.EMPLOYEE(FIRSTNAME, LASTNAME, PASSWORD) "
					+ "VALUES(?, ?, ?)");

			lStatement.setString(1, "Gadriel");
			lStatement.setString(2, "Smith");
			lStatement.setString(3, "secret");
			lStatement.executeUpdate();

			lStatement.setString(1, "Bob");
			lStatement.setString(2, "Slayer");
			lStatement.setString(3, "supersecret");
			lStatement.executeUpdate();
		}
		catch (SQLException aException)
		{
			throw new RuntimeException(aException);
		}


	}
}
