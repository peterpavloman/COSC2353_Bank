/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author narks
 */
public class Transaction{
	private int		id, savingID, amount;
	private String	description;

	public Transaction(int savingID, int amount, String description){
		this(-1, savingID, amount, description);
	}

	public Transaction(int id, int savingID, int amount, String description){
		this.id			=id;
		this.savingID	=savingID;
		this.amount		=amount;
		this.description=description;
	}

	public int getID(){ return id; }
	public void setID(int id){ this.id= id; }

	public int getSavingID(){ return savingID; }
	public void setSavingID(int savingID){ this.savingID= savingID; }

	public int getAmount(){ return amount; }
	public void setAmount(int amount){ this.amount=amount; }

	public String getDescription(){ return description; }
	public void setDescription(String description){ this.description=description; }
}
