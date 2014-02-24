package data;

import java.math.BigDecimal;

/**
 * Data object that represents a transaction (whenever money is withdrawn or
 * deposited).
 * 
 * @author nanxinglin, Peter
 */
public class Transaction
{
	private int		mIDTransaction, mIDSavings;
	private BigDecimal mAmount;
	private String	mDescription;
 
	public Transaction(int aIDSavings, BigDecimal aAmount, String aDescription)
	{
		this(-1, aIDSavings, aAmount, aDescription);
	}

	public Transaction(int id, int savingID, BigDecimal aAmount, String description){
		this.mIDTransaction			=id;
		this.mIDSavings	=savingID;
		this.mAmount		=aAmount;
		this.mDescription=description;
	}

	public int getID(){ return mIDTransaction; }
	public void setID(int id){ this.mIDTransaction= id; }

	public int getSavingID(){ return mIDSavings; }
	public void setSavingID(int savingID){ this.mIDSavings= savingID; }

	public BigDecimal getAmount(){ return mAmount; }
	public void setAmount(BigDecimal aAmount){ mAmount = aAmount; }

	public String getDescription(){ return mDescription; }
	public void setDescription(String description){ this.mDescription=description; }
}
