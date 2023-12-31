package ro.any.c12153.shared.aad;

import java.security.SecureRandom;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ro.any.c12153.opexal.bkg.AppSingleton;

/**
 *
 * @author C12153
 */
@Named(value = "aadhelp")
@RequestScoped
public class AADHelperBean {
    
    private @Inject ExternalContext context;
    
    public String getCallback(){
        String rezultat;
        HttpServletRequest request = (HttpServletRequest) this.context.getRequest();
        switch (request.getServerName()){
            case "localhost":
            case "10.76.41.30":
                rezultat = "http://".concat(request.getServerName())
                        .concat(request.getContextPath())
                        .concat(":8080")
                        .concat(context.getInitParameter("ro.any.c12153.AAD_REDIRECT_URI"));
                break;
            default:
                rezultat = "https://".concat(request.getServerName())
                        .concat(request.getContextPath())
                        .concat(context.getInitParameter("ro.any.c12153.AAD_REDIRECT_URI"));
        }
        return rezultat;
    }
    
    public String getNonce(){
        String text = null;
        HttpSession session = (HttpSession) this.context.getSession(false);
        
        if (session != null){
            text = new SecureRandom().ints(97, 122)
                    .limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            
            session.setAttribute("aad_nonce", text);
        }
        return text;
    }
    
    public String getClientId(){
        return this.context
                .getInitParameter(AppSingleton.CHILD_APP ? "ro.any.c12153.CHILD_AAD_CLIENT_ID" : "ro.any.c12153.PARENT_AAD_CLIENT_ID");
    }
}
