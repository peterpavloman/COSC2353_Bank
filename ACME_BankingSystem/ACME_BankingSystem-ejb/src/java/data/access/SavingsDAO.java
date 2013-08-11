package data.access;

import data.Savings;

/**
 *
 * @author narks
 */
public interface SavingsDAO 
{
    public void create(Savings aSavings);
    public Savings get(int aIDSavings);
    public void update(Savings aSavings);
    public void delete(Savings aSavings);
}
