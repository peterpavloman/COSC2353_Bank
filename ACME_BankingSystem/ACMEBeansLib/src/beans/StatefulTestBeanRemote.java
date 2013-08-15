/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import exceptions.ServerTestException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author narks
 */

@Remote
public interface StatefulTestBeanRemote
{
    public void addItemToCart(int aId) throws ServerTestException;
    public List<String> getItemsInCart();
    public void emptyCart();
}
