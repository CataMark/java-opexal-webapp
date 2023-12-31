package ro.any.c12153.opexal.view.md;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ro.any.c12153.opexal.services.PerioadaServ;
import ro.any.c12153.shared.App;
import ro.any.c12153.shared.Utils;
import ro.any.c12153.shared.beans.CurrentUser;
import ro.any.c12153.shared.entities.User;

/**
 *
 * @author C12153
 */
@Named(value = "periodprompt")
@ViewScoped
public class PerioadaPrompt implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(PerioadaPrompt.class.getName());
    
    private @Inject @CurrentUser User cuser;
    private String initError;
    private List<Short> ani;
    private String selected;
    
    @PostConstruct
    private void init(){
        try {
            //initializare query parameters
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            Optional<String> an = Optional.ofNullable(params.get("an"));
            if (an.isPresent()) this.selected = Utils.paramDecode(an.get());
            
            this.ani = PerioadaServ.getAni(cuser.getUname());
            
        } catch (Exception ex) {
            App.log(LOG, Level.SEVERE, cuser.getUname(), ex);
            this.initError = ex.getMessage();
        }
    }
    
    public String navigate(String page){
        String rezultat = page + "?faces-redirect=true";
        try {
            rezultat += (this.selected == null ? "" : "&an=" + Utils.paramEncode(this.selected));            
        } catch (Exception ex) {
            App.log(LOG, Level.SEVERE, cuser.getUname(), ex);
        }
        return rezultat;
    }

    public String getInitError() {
        return initError;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<Short> getAni() {
        return ani;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
