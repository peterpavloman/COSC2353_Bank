package data.access;

import data.Transaction;

/**
 *
 * @author narks
 */
public interface TransactionDAO 
{
    public void create(Transaction aTransaction);
    public Transaction get(int aIDTransaction);
    public void update(Transaction aTransaction);
    public void delete(Transaction aTransaction);
}
