package services;

import beans.ThirdPartyInvestorBeanRemote;
import exceptions.ApplicationLogicException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;

/**
 * REST Web Service
 *
 * @author yunfei
 */
@Path("stats")
@RequestScoped
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
@EJB
private ThirdPartyInvestorBeanRemote mInvestorBean;
    /**
     * Retrieves representation of an instance of services.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getXml() throws ApplicationLogicException {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        //int length=mInvestorBean.getHomeLoanIdList().size();
        String show="";
        show += "The total balance is:"+mInvestorBean.getTotalSavingsBalance();
        //show+=mInvestorBean.getTotalSavingsBalance();
        //String show=mInvestorBean.getHomeLoanIdList().get(0)+"";
        //String show=length+"";
        return show;
        //mInvestorBean.getHomeLoanIdList();
        //return "hello";
    }
    
    @GET
    @Path("/homeloan")
    public String getHomeloanIds() throws ApplicationLogicException
    {
        List list=mInvestorBean.getHomeLoanIdList();
        String show="display the HomeLoanIdList:";
        for(int i=0;i<list.size();i++){
            show+="\n"+list.get(i)+"\t";
        }
        return show;
    }
    
    @GET
    @Path("/homeloan/{id}") 
    public String getAccount(@PathParam("id") Long id) throws ApplicationLogicException
    {
        String title="The homeload borrowed amount is :";
        return title + mInvestorBean.getHomeLoanDetails(id).getAmountBorrowed().toString();
    }
    
    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/plain")
    public void putXml(String content) {
    }
}
