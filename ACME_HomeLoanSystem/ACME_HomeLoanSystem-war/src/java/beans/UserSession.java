package beans;

import entitydata.Customer;
import entitydata.HomeLoan;
import entitydata.Savings;
import exceptions.ApplicationLogicException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

/**
 * 
 * @author nanxinglin
 */

@Named(value="userSession")
@SessionScoped
public class UserSession implements Serializable{
	@EJB
	private HomeLoanBeanRemote user;
	private TempHomeLoan tempHomeLoan;

	public UserSession(){
		tempHomeLoan= new TempHomeLoan();
	}

	public TempHomeLoan getTempHomeLoan(){ return tempHomeLoan; }
	public void toRegisterPage(){ Constant.redirect("createAccount.xhtml"); }
	public void toRepayPage(){ Constant.redirect("repayAccount.xhtml"); }
	public void toListPage(){ Constant.redirect("listAccount.xhtml"); }
	public void toAcountMainPage(){ Constant.redirect("action.xhtml"); }

	public Customer getUser(){
		try{
			return user.getCustomerDetails((int) Constant.getSessionAttribute("CUSTOMERID"));
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public void createHomeLoan(){
		if(isAgree){
			try{
				user.createHomeLoanAccount((int)Constant.getSessionAttribute("CUSTOMERID"), new BigDecimal(tempHomeLoan.getBorrowed()),
					tempHomeLoan.getJob(), Double.toHexString(tempHomeLoan.getYearlySalary()), tempHomeLoan.getPhone(), tempHomeLoan.getEmail(), tempHomeLoan.getContactType());

				Constant.getExternalContext().addResponseHeader("refresh", "3; url= action.xhtml");
				Constant.addMessage(null, "Success!", FacesMessage.SEVERITY_INFO);
			}catch(ApplicationLogicException ex){
				String message= ex.getUserMessage();
				if(message.isEmpty())
					message= "Declined, due to not achieved our term of conditions.";

				Constant.addMessage("submit", message, FacesMessage.SEVERITY_ERROR);
			}
		}else
			Constant.addMessage("submit", "Declined, due to disagree our term of conditions.", FacesMessage.SEVERITY_ERROR);
	}

	public class DisplaySavings{
		private int id;
		private String balance;

		public DisplaySavings(int id, BigDecimal balance){
			this.id		= id;
			if(balance!= null)
				this.balance= "$"+balance.toString();
			else
				this.balance= "$0";
		}

		public int getId(){ return id; }
		public void setId(int id){ this.id = id; }
		public String getBalance(){ return balance; }
		public void setBalance(String balance){ this.balance = balance; }
	}

	public class DisplayHomeLoan{
		private long id;
		private String borrowed;
		private String repayed;

		public DisplayHomeLoan(long id, BigDecimal borrowed, BigDecimal repayed){
			this.id			= id;
			if(borrowed!= null)
				this.borrowed	= "$"+ borrowed.toString();
			else
				this.borrowed	= "$0";

			if(repayed!= null)
				this.repayed	= "$"+ repayed.toString();
			else
				this.repayed	= "$0";
		}

		public long getId(){ return id; }
		public void setId(long id){ this.id = id; }
		public String getBorrowed(){ return borrowed; }
		public void setBorrowed(String borrowed){ this.borrowed = borrowed; }
		public String getRepayed(){ return repayed; }
		public void setRepayed(String repayed){ this.repayed = repayed; }
	}

	public List<DisplaySavings> getSavings(){
		List<DisplaySavings> temp= new ArrayList<>();
		try{
			for(int i: getSavingIds()){
				Savings savings= user.getSavingsDetails(i);
				temp.add(new DisplaySavings(i, savings.getBalance()));
			}

			totalSavings= temp.size();
		}catch(ApplicationLogicException ex){
			ex.printStackTrace();

		}

		return temp;
	}

	public List<DisplayHomeLoan> getHomeLoans(){
		List<DisplayHomeLoan> temp= new ArrayList<>();
		try{
			for(long i: getHomeLoanIds()){
				HomeLoan homeLoan= user.getHomeLoanDetails(i);
				temp.add(new DisplayHomeLoan(i, homeLoan.getAmountBorrowed(), homeLoan.getAmountRepayed()));
			}
			totalHomeLoans= temp.size();
		}catch(ApplicationLogicException ex){
			ex.printStackTrace();
		}

		return temp;
	}

	public class TempHomeLoan{
		private double borrowed, yearlySalary;
		private String job, email, phone;
		private int contactType;

		public double getBorrowed(){ return borrowed; }
		public void setBorrowed(double borrowed){ this.borrowed = borrowed; }
		public double getYearlySalary(){ return yearlySalary; }
		public void setYearlySalary(double yearlySalary){ this.yearlySalary = yearlySalary; }
		public String getJob(){ return job; }
		public void setJob(String job){ this.job = job; }
		public String getEmail(){ return email; }
		public void setEmail(String email){ this.email = email; }
		public String getPhone(){ return phone; }
		public void setPhone(String phone){ this.phone = phone; }
		public int getContactType(){ return contactType; }
		public void setContactType(int contactType){ this.contactType = contactType; }
	}

	private boolean isAgree;
	private int		totalSavings;
	private int		totalHomeLoans;
	public boolean	getAgree(){ return isAgree; }
	public void		setAgree(boolean agree){ isAgree= agree; }
	public int		getTotalSavings(){ return totalSavings; }
	public int		getTotalHomeLoans(){ return totalHomeLoans; }


	private int savingID	= -1;
	private int homeLoanID	= -1;
	private BigDecimal repayAmount;

	public void repayHomeLoan(){
		try{
			user.repayHomeLoanAccount((int)Constant.getSessionAttribute("CUSTOMERID"), homeLoanID, savingID, repayAmount);

			Constant.getExternalContext().addResponseHeader("refresh", "3; url= action.xhtml");
			Constant.addMessage(null, "Success!", FacesMessage.SEVERITY_INFO);
		}catch(ApplicationLogicException ex){
			String message= ex.getUserMessage();
			if(message.isEmpty())
				message= "Internal server error occurred.";

			Constant.addMessage(null, message, FacesMessage.SEVERITY_ERROR);
		}
	}

	public List<Integer> getSavingIds() throws ApplicationLogicException{
		return user.getSavingsIdList((int)Constant.getSessionAttribute("CUSTOMERID"));
	}

	public List<Long> getHomeLoanIds() throws ApplicationLogicException{
		return user.getHomeLoanIdList((int)Constant.getSessionAttribute("CUSTOMERID"));
	}

	public int getSavingID(){ return savingID; }
	public void setSavingID(int savingID){ this.savingID = savingID; }
	public int getHomeLoanID(){ return homeLoanID; }
	public void setHomeLoanID(int homeLoanID){ this.homeLoanID = homeLoanID; }

	public BigDecimal getRepayAmount(){ return repayAmount; }
	public void setRepayAmount(BigDecimal repayAmount){ this.repayAmount = repayAmount; }
}
