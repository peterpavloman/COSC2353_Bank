package beans;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;

/**
 * 
 * @author nanxinglin
 */

@Named(value="userAction")
@ApplicationScoped
public class UserAction{
	@EJB
	private HomeLoanBeanRemote homeLoanUser;
	@ManagedProperty(value="#{userSession}")
	private UserSession	userSession;

	private int		userId;
	private String	password;

	public void login(){
		try{
			if(homeLoanUser.authenticateLogin(userId, password)){
				Constant.getSession().setAttribute("CUSTOMERID", userId);
				Constant.redirect("action.xhtml");
			}else
				Constant.addMessage(null, "Invalid user ID or password.", FacesMessage.SEVERITY_ERROR);
		}catch(Exception ex){
			ex.printStackTrace();
			Constant.addMessage(null, "Internal server error occurred.", FacesMessage.SEVERITY_FATAL);
		}
	}

	public void logout(){
		Constant.removeSessionAttribute("CUSTOMERID");
		Constant.redirect("login.xhtml");
	}

	public boolean isLoggedIn(){
		boolean isLoggedIn= Constant.getSessionAttribute("CUSTOMERID")!= null;
		String	uri= Constant.getRequest().getPathInfo();

		if(!isLoggedIn){
			if(!uri.contains("login"))
				Constant.redirect("login.xhtml");
		}else if(uri.contains("login"))
			Constant.redirect("action.xhtml");

		return isLoggedIn;
	}

	public int getUserId(){ return userId; }
	public void setUserId(int userId){ this.userId = userId; }
	public String getPassword(){ return password; }
	public void setPassword(String password){ this.password = password; }
}
