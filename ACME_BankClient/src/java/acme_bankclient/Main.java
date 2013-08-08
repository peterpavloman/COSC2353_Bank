/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acme_bankclient;

import beans.CustomerBeanRemote;
import java.sql.Date;
import javax.ejb.EJB;

/**
 *
 * @author Narks
 */
public class Main {

    @EJB
    private static CustomerBeanRemote msCustomerBean;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        
        msCustomerBean.addCustomer("Peter", "Pav", new Date(27, 03, 1992), 
                "1 Someplace Street");
        
        System.out.println("Application client success!");
    }
}
