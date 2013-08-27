/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acme_bankclient;

//import beans.CustomerBeanRemote;
//import beans.EmployeeBeanRemote;
//import beans.StatefulTestBeanRemote;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ejb.EJB;
import beans.SavingsClientBeanRemote;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
				if (lSelection == 1)
			{
				try
				{
					String fname;
					String lname;
					Date DOB;
					String address;
					System.out.println("Create a customer");
					System.out.printf("First Name: ");
					fname = lReadInput.readLine();
					System.out.printf("Last Name: ");
					lname = lReadInput.readLine();
					System.out.println("Date of Birth (dd,MM,YYYY):");
					// TODO: Date has to be typecasted correctly to sql date format
					StringTokenizer lToken = new StringTokenizer(lReadInput.readLine(), ",");
                                        int lDay = Integer.parseInt(lToken.nextToken());
                                        int lMonth = Integer.parseInt(lToken.nextToken());
                                        int lYear = Integer.parseInt(lToken.nextToken());
                                        DOB = new Date(lYear - 1900, lMonth - 1, lDay);
                                        System.out.println(DOB.toString());
                                        System.out.println("Input your address:");
					address = lReadInput.readLine();
					if (msSavingBean.createCustomer(fname, lname, DOB, address) > 0)
					{
						System.out.println("Create successful!");
					}
					else
					{
						System.out.println("Create fail!");
					}
				}
				catch (IOException io)
				{
					System.out.println(io.getMessage());
				}
//				catch (ParseException pe)
//				{
//					System.out.println(pe.getMessage());
//				}
				catch (ApplicationLogicException lException)
				{
					System.out.println("An error has occurred: " + lException.
							getUserMessage());
				}
			}
			else if (lSelection == 2)
			{
				System.out.println("Open a saving account:");
				int savingAccount;
				System.out.printf(
						"Please provide the customer ID to create the saving account for: ");
				try
				{
					savingAccount = Integer.parseInt(lReadInput.readLine());
					if (msSavingBean.createSavingsAccount(savingAccount) > 0)
					{
						System.out.println("Open a saving account successful!");
					}
					else
					{
						System.out.println("Open a saving account fail!");
					}
				}
				catch (IOException ex)
				{
					Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				}
				catch (NumberFormatException lNFException)
				{
					System.out.println("Error: Customer ID must be a number.");
					continue;
				}
				catch (ApplicationLogicException lLogicException)
				{
					System.out.println("Error: " + lLogicException.getUserMessage());
				}

			}
			else if (lSelection == 3)
			{
				try
				{
					System.out.println("Make deposit into Savings account:");
					System.out.println(
							"Please provide an account ID which you want to deposit:");
					int aSavingID = Integer.parseInt(lReadInput.readLine());
					System.out.println("How much moeny do you want to deposit?");
					BigDecimal money = BigDecimal.valueOf(Double.parseDouble(
							lReadInput.readLine()));
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
					continue;
				}
				catch (ApplicationLogicException lLogicException)
				{
					System.out.println("Error: " + lLogicException.getUserMessage());
				}
			}
			else if (lSelection == 4)
			{
				try
				{
					System.out.println("Make withdrawal from Savings account:");
					System.out.println("Please provide your saving account ID:");
					int aIDSaving = Integer.parseInt(lReadInput.readLine());
					System.out.
							println("How much money do you want to withdraw?");
					BigDecimal money = BigDecimal.valueOf(Double.parseDouble(
							lReadInput.readLine()));
					msSavingBean.withdrawIntoSavingsAccount(aIDSaving, money);
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
					continue;
				}
				catch (ApplicationLogicException lLogicException)
				{
					System.out.println("Error: " + lLogicException.getUserMessage());
				}

			}
			else if (lSelection == 5)
			{
				try
				{
					System.out.println("View balance of Savings account:");
					System.out.println("Your saving account ID:");
					int aIDSaving = Integer.parseInt(lReadInput.readLine());
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
						continue;
					}
					catch (ApplicationLogicException lLogicException)
					{
						System.out.println("Error: " + lLogicException.getUserMessage());
					}
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
				System.out.println("You have been logged out automatically.");
				break;
			}
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		printMenu();
	}
}
