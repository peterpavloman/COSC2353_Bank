/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acme_bankclient;

import beans.CustomerBeanRemote;
import beans.EmployeeBeanRemote;
import beans.StatefulTestBeanRemote;
import exceptions.ApplicationLogicException;
import exceptions.LoginFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.ejb.EJB;
import security.LoginSession;

/**
 *
 * @author Peter (s3286430)
 */
public class Main {

    @EJB
    private static CustomerBeanRemote msCustomerBean;
	@EJB
	private static StatefulTestBeanRemote msStatefulTestBean;
	@EJB
	private static EmployeeBeanRemote msEmployeeBean;
    
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
				if (lOutput <= 0 || lOutput > 7)
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
				lNFException.printStackTrace();
                lValidInput = false;
            }
        } while (lValidInput == false);
		
		return lOutput;
    }
    
    private static void testPrintMenu()
    {	
		while (true)
		{
			System.out.println("1: Add item 1");
			System.out.println("2: Add item 2");
			System.out.println("3: Add item 3");
			System.out.println("4: Add item 4");
			System.out.println("5: View cart items");
			System.out.println("6: Empty cart items");
			System.out.println("7: Log out");
			
			int lSelection = testGetSelection();
			
			if (lSelection >= 1 && lSelection <= 4)
			{
				try
				{
					msStatefulTestBean.addItemToCart(lSelection);
					System.out.println("Adding item " + lSelection);
				}
				catch (ApplicationLogicException lException)
				{
					System.out.println("An error has occurred: " +
							lException.getUserMessage());
				}
			}
			else if (lSelection == 5)
			{
				List<String> lItems = msStatefulTestBean.getItemsInCart();
				System.out.println("Items in cart: ");
				for (String lItem : lItems)
				{
					System.out.println(lItem);
				}
			}
			else if (lSelection == 6)
			{
				msStatefulTestBean.emptyCart();
				System.out.println("Cart emptied!");
			}
			else if (lSelection == 7)
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
		try
		{
			// LoginSession lSession = msEmployeeBean.login(0, "legit");
			msEmployeeBean.login(0, "legit");
		}
		catch (LoginFailureException aException)
		{
			System.out.println("Login failed: " + aException.getUserMessage());
		}
		
		testPrintMenu();
    }
}
