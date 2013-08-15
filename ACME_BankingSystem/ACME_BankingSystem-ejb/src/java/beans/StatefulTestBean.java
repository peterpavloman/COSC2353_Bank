/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import exceptions.ServerTestException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;

/**
 *
 * @author narks
 */
@Stateful
public class StatefulTestBean implements StatefulTestBeanRemote
{
    private List<String> mCartItems;
    private List<String> mInventoryItems;
    
	@PostConstruct
	public void initialize()
    {
		mInventoryItems = new ArrayList<String>();
		mCartItems = new ArrayList<String>();
        mInventoryItems.add("Test Item 1");
        mInventoryItems.add("Test Item 2");
        mInventoryItems.add("Test Item 3");
        mInventoryItems.add("Test Item 4");
    }

    @Override
    public void addItemToCart(int aId) throws ServerTestException
    {
		if (aId == 4)
		{
			// Lets say item 4 is out of stock - we want to raise an error
			// and make sure its reported to the client.
			throw new ServerTestException();
		}
        mCartItems.add(mInventoryItems.get(aId - 1));
    }

    @Override
    public List<String> getItemsInCart()
    {
        return mCartItems;
    }

    @Override
    public void emptyCart()
    {
        mCartItems.clear();
    }
}
