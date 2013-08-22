package data.access.rdb;

import data.Customer;
import data.Employee;
import data.access.EmployeeDAO;
import exceptions.ApplicationLogicException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Relational database implementation of Employee DAO.
 * 
 * @author s3286430
 */

public class RDBEmployeeDAO implements EmployeeDAO
{
    private Connection mDBConnection;
    
    public RDBEmployeeDAO(Connection aDBConnection)
    {
        mDBConnection = aDBConnection;
    }
    
	@Override
    public void create(Employee aEmployee) throws ApplicationLogicException
    {
        try
        {
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "INSERT INTO ACMEBANK.EMPLOYEE(firstname, lastname,"
                    + "password) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            
            lStatement.setString(1, aEmployee.getFirstName());
            lStatement.setString(2, aEmployee.getLastName());
            lStatement.setString(3, aEmployee.getPassword());
            
            lStatement.executeUpdate();
            
            ResultSet lResult = lStatement.getGeneratedKeys();
            lResult.next();
            
            aEmployee.setIDEmployee(lResult.getInt(1));
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not add new employee.");
            throw new ApplicationLogicException("Could not add new employee.");
        }
    }

    @Override
    public Employee get(int aIDEmployee) throws ApplicationLogicException
    {
        try
        {
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "SELECT firstname, lastname, password FROM "
					+ "ACMEBANK.EMPLOYEE WHERE id_employee = ?;");
            
            lStatement.setInt(1, aIDEmployee);
			
            ResultSet lResult = lStatement.executeQuery();
            // Check if there was a row returned in the ResultSet
			if (1 == 1)
			{
				Employee lEmployee = new Employee("a", "b", "d");
				return lEmployee;
			}
			else
			{
				throw new ApplicationLogicException("Employee does not exist!");
			}
			
			// If not, throw an exception.
        }
        catch (SQLException aException)
        {
            aException.printStackTrace();
            System.out.println("ERROR: Could not fetch employee ID.");
            throw new ApplicationLogicException("Could not fetch employee ID.");
        }
    }

    @Override
    public void update(Employee aEmployee) throws ApplicationLogicException
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
            lStatement.setString(3, aCustomer.getDateOfBirth().toString());
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
    public void delete(Employee aEmployee) throws ApplicationLogicException
    {
        try
        {
            PreparedStatement lStatement = mDBConnection.prepareStatement(
                    "DELETE FROM ACMEBANK.CUSTOMER WHERE "
					+ "id_customer=?");
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
