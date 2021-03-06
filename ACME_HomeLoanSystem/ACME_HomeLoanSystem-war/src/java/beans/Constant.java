package beans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author nanxinglin
 */

public class Constant{
	public static FacesContext getContext(){
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext(){
		return getContext().getExternalContext();
	}

	public static void redirect(String url){
		try{
			getExternalContext().redirect(url);
		}catch(IOException ioe){ ioe.printStackTrace(); }
	}

	public static HttpServletRequest getRequest(){
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	public static HttpSession getSession(){
		return getSession(false);
	}

	public static Object getSessionAttribute(String name){
		return getSession().getAttribute(name);
	}

	public static void removeSessionAttribute(String name){
		getSession().removeAttribute(name);
	}

	public static HttpSession getSession(boolean toCreate){
		return (HttpSession)getContext().getExternalContext().getSession(toCreate);
	}

	public static void addMessage(String id, String message){
		addMessage(id, message, FacesMessage.SEVERITY_INFO);
	}

	public static void addMessage(String id, String message, FacesMessage.Severity type){
		getContext().addMessage(id, new FacesMessage(type, message, null));
	}
}
