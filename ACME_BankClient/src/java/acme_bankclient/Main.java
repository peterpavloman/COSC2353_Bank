package acme_bankclient;

import beans.DebugResetDBSchemaRemote;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ejb.EJB;
import beans.SavingsClientBeanRemote;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter (s3286430)
 */
public class Main
{

	@EJB
	private static SavingsClientBeanRemote msSavingBean;
	@EJB
	private static DebugResetDBSchemaRemote msTestBean;

	private static Date convertStringToDate(String aString) throws InvalidInputException
	{
		try
		{
			StringTokenizer lToken = new StringTokenizer(aString, ",");
			int lDay = Integer.parseInt(lToken.nextToken());
			int lMonth = Integer.parseInt(lToken.nextToken());
			int lYear = Integer.parseInt(lToken.nextToken());

			// Terrible hack, but should work for validation
			Date lDate = new Date(lYear - 1900, lMonth - 1, lDay);
			if (lDate.getYear() != lYear - 1900
					|| lDate.getMonth() != lMonth - 1
					|| lDate.getDate() != lDay)
			{
				throw new InvalidInputException();
			}
				
			return lDate;
		}
		catch (Exception aException)
		{
			throw new InvalidInputException();
		}
		
	}

	private static int testGetSelection()
	{
		String lInput = "";
		int lOutput = -1;
		boolean lValidInput = false;
		BufferedReader lReadInput = new BufferedReader(
				new InputStreamReader(System.in));

		do
		{
			try
			{
				lInput = lReadInput.readLine();
				if (lInput.length() > 1)
				{
					continue;
				}
				lOutput = Integer.parseInt(lInput);
				if (lOutput <= 0 || lOutput > 6)
				{
					continue;
				}
				lValidInput = true;
			}
			catch (IOException lIOException)
			{
				lIOException.printStackTrace();
				lValidInput = false;
			}
			catch (NumberFormatException lNFException)
			{
				//lNFException.printStackTrace();
				lValidInput = false;
				System.out.println("Please input a number for menu option.");
				continue;
			}
		} while (lValidInput == false);

		return lOutput;
	}

	private static void createCustomer(BufferedReader aReadInput) throws LoginFailureException
	{
		try
		{
			String fname;
			String lname;
			Date DOB;
			String address;
			System.out.println("Create a customer");
			System.out.printf("First Name: ");
			fname = aReadInput.readLine();
			if (fname.length() == 0) { throw new InvalidInputException(); }
			System.out.printf("Last Name: ");
			lname = aReadInput.readLine();
			if (lname.length() == 0) { throw new InvalidInputException(); }
			System.out.printf("Date of Birth (dd,MM,YYYY): ");
			DOB = convertStringToDate(aReadInput.readLine());
			System.out.printf("Address: ");
			address = aReadInput.readLine();
			if (address.length() == 0) { throw new InvalidInputException(); }
			int lCustomerId = msSavingBean.createCustomer(fname,
					lname, DOB,
					address);
			if (lCustomerId > 0)
			{
				System.out.println(
						"Customer successfully created with customer id "
						+ lCustomerId + "!");
			}
			else
			{
				System.out.println("Failed to create new customer!");
			}
		}
		catch (IOException io)
		{
			System.out.println(io.getMessage());
		}
		catch (InvalidInputException lException)
		{
			System.out.println(
					"Error: invalid input!");
		}
		catch (ApplicationLogicException lException)
		{
			System.out.println(
					"An error has occurred: " + lException.
					getUserMessage());
		}
	}

	private static void openSavingsAccount(BufferedReader aReadInput) throws LoginFailureException
	{
		System.out.println("Open a saving account:");
		int lCustomerId;
		System.out.printf(
				"Please provide the customer ID to create the saving account for: ");
		try
		{
			lCustomerId = Integer.parseInt(aReadInput.readLine());
			int lAccountId = msSavingBean.createSavingsAccount(
					lCustomerId);
			System.out.println(
					"Saving account successfully created with savings id "
					+ lAccountId + "!");
		}
		catch (IOException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		catch (NumberFormatException lNFException)
		{
			System.out.println(
					"Error: Customer ID must be a number.");
		}
		catch (ApplicationLogicException lLogicException)
		{
			System.out.println("Error: " + lLogicException.
					getUserMessage());
		}
	}

	private static void makeDeposit(BufferedReader aReadInput) throws LoginFailureException
	{
		try
		{
			System.out.println("Make deposit into Savings account");
			System.out.printf(
					"Please provide an account ID which you want to deposit: ");
			int aSavingID = Integer.parseInt(aReadInput.readLine());
			System.out.printf(
					"How much money to deposit: ");
			BigDecimal money = BigDecimal.valueOf(Double.
					parseDouble(
					aReadInput.readLine()));
			msSavingBean.depositIntoSavingsAccount(aSavingID, money);
		}
		catch (IOException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		catch (NumberFormatException lNFException)
		{
			System.out.println(
					"Note:The Account ID and money should be a number.");
		}
		catch (ApplicationLogicException lLogicException)
		{
			System.out.println("Error: " + lLogicException.
					getUserMessage());
		}
	}

	private static void makeWithdrawal(BufferedReader aReadInput) throws LoginFailureException
	{
		try
		{
			System.out.println(
					"Make withdrawal from Savings account:");
			System.out.println(
					"Please provide your saving account ID:");
			int aIDSaving = Integer.parseInt(aReadInput.readLine());
			System.out.
					println(
					"How much money to withdraw?");
			BigDecimal money = BigDecimal.valueOf(Double.
					parseDouble(
					aReadInput.readLine()));
			msSavingBean.
					withdrawIntoSavingsAccount(aIDSaving, money);
		}
		catch (IOException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		catch (NumberFormatException lNFException)
		{
			System.out.println(
					"Note:The Account ID and money should be a number.");
		}
		catch (ApplicationLogicException lLogicException)
		{
			System.out.println("Error: " + lLogicException.
					getUserMessage());
		}
	}

	private static void viewBalance(BufferedReader aReadInput) throws LoginFailureException
	{
		try
		{
			System.out.println("View balance of Savings account:");
			System.out.println("Your saving account ID:");
			int aIDSaving = Integer.parseInt(aReadInput.readLine());
			System.out.println(
					"The balance of your saving account is :" + msSavingBean.
					getSavingsAccountBalance(aIDSaving));
		}
		catch (IOException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		catch (NumberFormatException lNFException)
		{
			System.out.
					println(
					"Note:The Account ID should be a number.");
		}
		catch (ApplicationLogicException lLogicException)
		{
			System.out.println("Error: " + lLogicException.
					getUserMessage());
		}
	}

	private static void printMenu()
	{
		int loginId;
		String pwd = "";
		boolean flag = false;
		BufferedReader lReadInput = new BufferedReader(new InputStreamReader(
				System.in));
		do
		{
			try
			{
				System.out.println("Please enter login details.");
				System.out.printf("Login ID: ");
				loginId = Integer.parseInt(lReadInput.readLine());
				System.out.printf("Login Password: ");
				pwd = lReadInput.readLine();
				if (msSavingBean.login(loginId, pwd))
				{
					flag = true;
					System.out.println("Login successful!");
					System.out.println();
				}
				else
				{
					System.out.println("Login failed!");
					System.out.println();
				}
			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
			}
			catch (NumberFormatException lNFException)
			{
				System.out.println("Error: Employee ID is a number.");
				continue;
			}
		} while (!flag);
		while (true)
		{
			System.out.println("Operations performed: "
					+ msSavingBean.getOperationCount()
					+ " out of " + msSavingBean.getOperationCountLimit());
			System.out.println("1: Create a customer");
			System.out.println("2: Open a Savings account");
			System.out.println("3: Make deposit into Savings account");
			System.out.println("4: Make withdrawal from Savings account");
			System.out.println("5: View balance of Savings account");
			System.out.println("6: Log out");

			int lSelection = testGetSelection();

			try
			{
				if (msSavingBean.getIsLoggedIn() == false)
				{
					System.out.println(
							"Your session has expired. You should take a break, and then restart the client.");
					break;
				}
				if (lSelection == 1)
				{
					createCustomer(lReadInput);
				}
				else if (lSelection == 2)
				{
					openSavingsAccount(lReadInput);
				}
				else if (lSelection == 3)
				{
					makeDeposit(lReadInput);
				}
				else if (lSelection == 4)
				{
					makeWithdrawal(lReadInput);
				}
				else if (lSelection == 5)
				{
					viewBalance(lReadInput);
				}
				else if (lSelection == 6)
				{
					System.out.println("Bye!");
					break;
				}
				System.out.println();
			}
			catch (LoginFailureException lLoginException)
			{
				System.out.println(
						"Your session has expired. You should take a break, and then restart the client.");
				break;
			}
		}
	}

	public static void main(String[] args)
	{
		// For testing / demo purposes only
		msTestBean.resetTables();
		
		printMenu();
	}
}
