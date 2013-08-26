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
import java.util.List;
import javax.ejb.EJB;
import security.LoginSession;
import beans.SavingsClientBeanRemote;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter (s3286430)
 */
public class Main {
    @EJB
    private static SavingsClientBeanRemote msSavingBean;
//    private static CustomerBeanRemote msCustomerBean;
//	@EJB
//	private static StatefulTestBeanRemote msStatefulTestBean;
//	@EJB
//	private static EmployeeBeanRemote msEmployeeBean;
    
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
    
    private static void testPrintMenu()
    {	
        int loginId;
        String pwd="";
        boolean flag=false;
        BufferedReader lReadInput = new BufferedReader(new InputStreamReader(System.in));
        do{
            try {
                System.out.println("Login Employee:");
                System.out.println("Login ID:");
                loginId=Integer.parseInt(lReadInput.readLine());
                System.out.println("Login Password:");
                pwd=lReadInput.readLine();
                //for test
//                if(loginId.equals(pwd)){
//                    flag=true;
//                    System.out.println("Login Successful!");
//                    System.out.println();
//                }
//                else
//                {
//                    System.out.println("Login fail!");
//                    System.out.println();
//                }
                if(msSavingBean.login(loginId, pwd)){
                    flag=true;
                    System.out.println("Login Successful!");
                    System.out.println();
                }
                else
                {
                    System.out.println("Login fail!");
                    System.out.println();
                }
            } catch (IOException ex) {
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }catch (NumberFormatException lNFException){
                System.out.println("Note:Employee ID is a number.");
                continue;
            }
        }while(!flag);
	while (true)
	{
            System.out.println("1: Create a customer");
            System.out.println("2: Open a Savings account");
            System.out.println("3: Make deposit into Savings account");
            System.out.println("4: Make withdrawal from Savings account");
            System.out.println("5: View balance of Savings account");
            System.out.println("6: Log out");
			
            int lSelection = testGetSelection();
			
            if (lSelection == 1 )
            {
                try
                {
                    String fname;
                    String lname;
                    Date DOB;
                    String address;
                    System.out.println("Create a customer:");
                    System.out.println("First Name:");
                    fname=lReadInput.readLine();
                    System.out.println("Last Name:");
                    lname=lReadInput.readLine();
                    System.out.println("Date Of Birth:");
                    DOB = (Date) new SimpleDateFormat("dd,MM,YYYY").parse(lReadInput.readLine());
                    address=lReadInput.readLine();
                    if(msSavingBean.createCustomer(fname, lname, DOB, address)>0){
                        System.out.println("Create successful!");
                    }else{
                        System.out.println("Create fail!");
                    }
                }
                catch(IOException io){
                    System.out.println(io.getMessage());
                }
                catch(ParseException pe){
                    System.out.println(pe.getMessage());
                }
                catch (ApplicationLogicException lException)
                {
                    System.out.println("An error has occurred: " + lException.getUserMessage());
                } catch (LoginFailureException ex) {
                    //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Login Fail!Unable to create a customer.");
                }
            }
            else if (lSelection == 2)
            {
                System.out.println("Open a saving account:");
                int savingAccount;
                System.out.println("Please provide the saving account number which you want to open:");
                try {
                    savingAccount=Integer.parseInt(lReadInput.readLine());
                    if(msSavingBean.createSavingsAccount(savingAccount)>0){
                        System.out.println("Open a saving account successful!");
                    }else{
                        System.out.println("Open a saving account fail!");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch(NumberFormatException lNFException){
                    System.out.println("Note:The Account ID is an Integer.");
                    continue;
                } catch (LoginFailureException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ApplicationLogicException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else if (lSelection == 3)
            {
                try {
                    System.out.println("Make deposit into Savings account:");
                    System.out.println("Please provide an account ID which you want to deposit:");
                    int aSavingID=Integer.parseInt(lReadInput.readLine());
                    System.out.println("How much moeny do you want to deposit?");
                    BigDecimal money=BigDecimal.valueOf(Double.parseDouble(lReadInput.readLine()));
                    msSavingBean.depositIntoSavingsAccount(aSavingID, money);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch(NumberFormatException lNFException){
                    System.out.println("Note:The Account ID and money should be a number.");
                    continue;
                } catch (LoginFailureException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ApplicationLogicException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (lSelection == 4)
            {
                try {
                    System.out.println("Make withdrawal from Savings account:");
                    System.out.println("Please provide your saving account ID:");
                    int aIDSaving=Integer.parseInt(lReadInput.readLine());
                    System.out.println("How much money do you want to withdraw?");
                    BigDecimal money=BigDecimal.valueOf(Double.parseDouble(lReadInput.readLine()));
                    msSavingBean.withdrawIntoSavingsAccount(aIDSaving, money);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch(NumberFormatException lNFException){
                    System.out.println("Note:The Account ID and money should be a number.");
                    continue;
                } catch (LoginFailureException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ApplicationLogicException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else if (lSelection == 5)
            {
                try {
                    System.out.println("View balance of Savings account:");
                    System.out.println("Your saving account ID:");
                    int aIDSaving=Integer.parseInt(lReadInput.readLine());
                    System.out.println("The balance of your saving account is :" + msSavingBean.getSavingsAccountBalance(aIDSaving));
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch(NumberFormatException lNFException){
                    System.out.println("Note:The Account ID should be a number.");
                    continue;
                } catch (LoginFailureException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ApplicationLogicException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (lSelection == 6)
            {
                System.out.println("Bye!");
                break;
            }
            System.out.println();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        // Date constructor deprecated?
        /*
        msCustomerBean.create("Peter", "Pav", new Date(27, 03, 1992), 
                "1 Someplace Street");
        
        System.out.println("Application client success!");
        */
		
//			System.out.println("Attempting login into Employee bean");
//			LoginSession lSession = msEmployeeBean.login(0, "legit");
//			System.out.println("Attempting login into Customer bean");
//			msCustomerBean.login(lSession);
		
        testPrintMenu();
    }
}
