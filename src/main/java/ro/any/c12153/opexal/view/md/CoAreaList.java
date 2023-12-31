package ro.any.c12153.opexal.view.md;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ro.any.c12153.opexal.entities.CoArea;
import ro.any.c12153.opexal.services.CoAreaServ;
import ro.any.c12153.shared.App;
import ro.any.c12153.shared.SelectItemView;
import ro.any.c12153.shared.SelectTableView;
import ro.any.c12153.shared.beans.CurrentLocale;
import ro.any.c12153.shared.beans.CurrentUser;
import ro.any.c12153.shared.beans.DialogController;
import ro.any.c12153.shared.entities.User;

/**
 *
 * @author C12153
 */
@Named(value = "coarealist")
@ViewScoped
public class CoAreaList implements Serializable, SelectTableView<CoArea>{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CoAreaList.class.getName());

    private @Inject @CurrentUser User cuser;
    private @Inject @CurrentLocale Locale clocale;
    
    private @Inject DialogController dialog;
    private @Inject SelectItemView<CoArea> item;
    private List<CoArea> list;
    private CoArea selected;
    
    public void datainit(){
        try {
            this.list = CoAreaServ.getAll(cuser.getUname());
        } catch (Exception ex) {
            App.log(LOG, Level.SEVERE, cuser.getUname(), ex);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, App.getBeanMess("title.coarea.listinit", clocale), ex.getMessage()));
        }
    }
    
    @Override
    public void newItem(){
        this.item.setSelected(new CoArea());
        this.item.initLists();
    }
    
    @Override
    public void passSelected(boolean initLists){
        try {
            if (this.selected == null){
                this.item.setSelected(null);
            } else {
                this.item.setSelected(new CoArea(this.selected.getJson()));
                if (initLists) this.item.initLists();
            }
        } catch (Exception ex) {
            App.log(LOG, Level.SEVERE, cuser.getUname(), ex);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, App.getBeanMess("title.sel.init", clocale), ex.getMessage()));
        }
    }
    
    @Override
    public void clear(){
       this.dialog.clear();
       this.item.clear();
    }

    @Override
    public String getInitError() {
        return null;
    }
    
    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<CoArea> getList() {
        return list;
    }

    public CoArea getSelected() {
        return selected;
    }

    @Override
    public void setSelected(CoArea selected) {
        this.selected = selected;
    }
}
