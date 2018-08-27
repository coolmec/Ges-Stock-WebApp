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

@Named("familleController")
@SessionScoped
public class FamilleController implements Serializable {

    @EJB
    private FamilleFacade ejbFacade;
    private List<Famille> items = null, filteredItems;
    private Famille selected, tocreate;

    public FamilleController() {
    }

    public Famille getTocreate() {
        if (tocreate == null) {
            tocreate = new Famille();
        }
        return tocreate;
    }

    public void setTocreate(Famille tocreate) {
        this.tocreate = tocreate;
    }

    public Famille getSelected() {
        return selected;
    }

    public void setSelected(Famille selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FamilleFacade getFacade() {
        return ejbFacade;
    }

    public Famille prepareCreate() {
        selected = new Famille();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FamilleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selected = null;
            tocreate = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FamilleUpdated"));
        selected = null;
        items = null;
    }

    public void cancel() {
        selected = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FamilleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }

    }

    public List<Famille> getItems() {
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
                List<Famille> famille = getFacade().findAll();
                Boolean existNomFamille = false;
                Short code = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Famille famille1 : famille) {
                        if (famille1.getNomFamille().equals(selected.getNomFamille())) {
                            code = famille1.getCodeFamille();
                            existNomFamille = true;
                        }
                    }
                    if (existNomFamille == true && !code.equals(selected.getCodeFamille())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomFamilleAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Famille famille1 : famille) {
                        if (famille1.getNomFamille().equals(tocreate.getNomFamille())) {
                            existNomFamille = true;
                        }
                    }
                    if (getFacade().find(tocreate.getCodeFamille()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("FamilleAlreadyExist"), null);
                    } else if (existNomFamille == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomFamilleAlreadyExist"), null);
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

    public Famille getFamille(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Famille> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Famille> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Famille> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Famille> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @FacesConverter(forClass = Famille.class)
    public static class FamilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FamilleController controller = (FamilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "familleController");
            return controller.getFamille(getKey(value));
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
            if (object instanceof Famille) {
                Famille o = (Famille) object;
                return getStringKey(o.getCodeFamille());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Famille.class.getName()});
                return null;
            }
        }

    }

}
