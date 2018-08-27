package net.betechs.stock.entites.dep_fam_poste_liv;

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
import net.betechs.entites.dep_fam_poste_liv.util.JsfUtil;
import net.betechs.entites.dep_fam_poste_liv.util.JsfUtil.PersistAction;

@Named("departementController")
@SessionScoped
public class DepartementController implements Serializable {

    @EJB
    private net.betechs.stock.entites.dep_fam_poste_liv.DepartementFacade ejbFacade;
    private List<Departement> items = null,filteredItems;
    private Departement selected;
    private Departement tocreate;

    public Departement getTocreate() {
        if (tocreate == null) {
            tocreate = new Departement();
        }
        return tocreate;
    }

    public void setTocreate(Departement tocreate) {
        this.tocreate = tocreate;
    }

    public DepartementController() {
    }

    public Departement getSelected() {
        return selected;
    }

    public void setSelected(Departement selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DepartementFacade getFacade() {
        return ejbFacade;
    }

    public Departement prepareCreate() {
        selected = new Departement();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DepartementCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selected = null;
            tocreate = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DepartementUpdated"));
        selected = null;
        items = null;
    }

    public void cancel() {
        selected = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DepartementDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Departement> getItems() {
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
                List<Departement> famille = getFacade().findAll();
                Boolean existNomDepartement = false;
                Short code = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Departement famille1 : famille) {
                        if (famille1.getNomDepartement().equals(selected.getNomDepartement())) {
                            code = famille1.getCodeDepartement();
                            existNomDepartement = true;
                        }
                    }
                    if (existNomDepartement == true && !code.equals(selected.getCodeDepartement())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TitreDepartementAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Departement famille1 : famille) {
                        if (famille1.getNomDepartement().equals(tocreate.getNomDepartement())) {
                            existNomDepartement = true;
                        }
                    }
                    if (getFacade().find(tocreate.getCodeDepartement()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DepartementAlreadyExist"), null);
                    } else if (existNomDepartement == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TitreDepartementAlreadyExist"), null);
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

    public Departement getDepartement(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Departement> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Departement> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Departement> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Departement> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @FacesConverter(forClass = Departement.class)
    public static class DepartementControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartementController controller = (DepartementController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departementController");
            return controller.getDepartement(getKey(value));
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
                return null;
            }
            if (object instanceof Departement) {
                Departement o = (Departement) object;
                return getStringKey(o.getCodeDepartement());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Departement.class.getName()});
                return null;
            }
        }

    }

}
