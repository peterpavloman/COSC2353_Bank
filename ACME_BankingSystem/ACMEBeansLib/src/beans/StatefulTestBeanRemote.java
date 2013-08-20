/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import exceptions.ApplicationLogicException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author narks
 */

@Remote
public interface StatefulTestBeanRemote
{
    public void addItemToCart(int aId) throws ApplicationLogicException;
    public List<String> getItemsInCart();
    public void emptyCart();
}
