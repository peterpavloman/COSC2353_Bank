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

/**
 *
 * @author Peter (s3286430)
 */
public class Main {
//    @EJB private static SavingsClientBeanRemote msSavingBean;
//    @EJB
//    private static SavingsClientBeanRemote msSavingBean;
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
        String loginId="";
        String pwd="";
        boolean flag=false;
        BufferedReader lReadInput = new BufferedReader(new InputStreamReader(System.in));
        do{
            try {
                System.out.println("Login Employee:");
                System.out.println("Login ID:");
                loginId=lReadInput.readLine();
                System.out.println("Login Password:");
                pwd=lReadInput.readLine();
                //for test
                if(loginId.equals(pwd)){
                    flag=true;
                    System.out.println("Login Successful!");
                    System.out.println();
                }
                else
                {
                    System.out.println("Login fail!");
                    System.out.println();
                }
//                if(msSavingBean.login(Integer.parseInt(loginId), pwd)){
//                    flag=true;
//                    System.out.println("Login Successful!");
//                    System.out.println();
//                }
//                else
//                {
//                    System.out.println("Login fail!");
//                    System.out.println();
//                }
            } catch (IOException ex) {
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
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
//				try
//				{
//					msStatefulTestBean.addItemToCart(lSelection);
//					System.out.println("Adding item " + lSelection);
//				}
//				catch (ApplicationLogicException lException)
//				{
//					System.out.println("An error has occurred: " +
//							lException.getUserMessage());
//				}
			}
                        else if (lSelection == 2)
                        {
                            
                        }
                        else if (lSelection == 3)
                        {
                            
                        }
                        else if (lSelection == 4)
                        {
                            
                        }
			else if (lSelection == 5)
			{
//				List<String> lItems = msStatefulTestBean.getItemsInCart();
				System.out.println("Items in cart: ");
//				for (String lItem : lItems)
//				{
//					System.out.println(lItem);
//				}
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
