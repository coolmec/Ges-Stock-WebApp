package net.betechs.mns.stock.entites.util_privilege;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import net.betechs.mns.stock.entites.util_privilege.util.JsfUtil;
import net.betechs.mns.stock.entites.util_privilege.util.JsfUtil.PersistAction;

@Named("privilegeController")
@SessionScoped
public class PrivilegeController implements Serializable {

    @EJB
    private net.betechs.mns.stock.entites.util_privilege.PrivilegeFacade ejbFacade;
    private List<Privilege> items = null,filteredItems;
    private Privilege selected, tocreate;

    public PrivilegeController() {
    }

    public Privilege getTocreate() {
        if (tocreate == null) {
            tocreate = new Privilege();
        }
        return tocreate;
    }

    public void setTocreate(Privilege tocreate) {
        this.tocreate = tocreate;
    }

    public Privilege getSelected() {
        return selected;
    }

    public void setSelected(Privilege selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PrivilegeFacade getFacade() {
        return ejbFacade;
    }

    public Privilege prepareCreate() {
        selected = new Privilege();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PrivilegeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            tocreate = null;
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PrivilegeUpdated"));
        selected = null;
        items = null;
    }

    public void cancel() {
        selected = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PrivilegeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Privilege> getItems() {
//        if (items == null) {
//            items = getFacade().findAll();
//        }
        items = null;
        items = getFacade().findAll();
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null || tocreate != null) {
            setEmbeddableKeys();
            try {
                List<Privilege> famille = getFacade().findAll();
                Boolean existNomPrivilege = false;
                Short code = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Privilege famille1 : famille) {
                        if (famille1.getLibellePrivilege().equals(selected.getLibellePrivilege())) {
                            code = famille1.getCodePrivilege();
                            existNomPrivilege = true;
                        }
                    }
                    if (existNomPrivilege == true && !code.equals(selected.getCodePrivilege())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LibellePrivilegeAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);

                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Privilege famille1 : famille) {
                        if (famille1.getLibellePrivilege().equals(tocreate.getLibellePrivilege())) {
                            existNomPrivilege = true;
                        }
                    }
                    if (getFacade().find(tocreate.getCodePrivilege()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PrivilegeAlreadyExist"), null);
                    } else if (existNomPrivilege == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LibellePrivilegeAlreadyExist"), null);
                    } else {
                        getFacade().create(tocreate);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                }
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(ex, msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Privilege getPrivilege(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Privilege> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Privilege> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Privilege> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Privilege> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @FacesConverter(forClass = Privilege.class)
    public static class PrivilegeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {

            }
            PrivilegeController controller = (PrivilegeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "privilegeController");
            return controller.getPrivilege(getKey(value));
        }

        java.lang.Short getKey(String value) {
            java.lang.Short key;
            key = Short.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Short value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {

            }
            if (object instanceof Privilege) {
                Privilege o = (Privilege) object;
                return getStringKey(o.getCodePrivilege());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Privilege.class.getName()});
                return null;
            }

        }

    }

}
