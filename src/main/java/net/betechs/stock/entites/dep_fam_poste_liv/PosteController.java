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

@Named("posteController")
@SessionScoped
public class PosteController implements Serializable {

    @EJB
    private PosteFacade ejbFacade;
    private List<Poste> items = null,filteredItems;
    private Poste selected, tocreate;

    public PosteController() {
    }

    public Poste getTocreate() {
        if (tocreate == null) {
            tocreate = new Poste();
        }
        return tocreate;
    }

    public void setTocreate(Poste tocreate) {
        this.tocreate = tocreate;
    }

    public Poste getSelected() {
        return selected;
    }

    public void setSelected(Poste selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PosteFacade getFacade() {
        return ejbFacade;
    }

    public Poste prepareCreate() {
        selected = new Poste();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PosteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selected = null;
            tocreate = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PosteUpdated"));
        selected = null;
        items = null;
    }

    public void cancel() {
        selected = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PosteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }

    }

    public List<Poste> getItems() {
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
                List<Poste> famille = getFacade().findAll();
                Boolean existNomPoste = false;
                Short code = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Poste famille1 : famille) {
                        if (famille1.getNomPoste().equals(selected.getNomPoste())) {
                            code = famille1.getCodePoste();
                            existNomPoste = true;
                        }
                    }
                    if (existNomPoste == true && !code.equals(selected.getCodePoste())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TitrePosteAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Poste famille1 : famille) {
                        if (famille1.getNomPoste().equals(tocreate.getNomPoste())) {
                            existNomPoste = true;
                        }
                    }
                    if (getFacade().find(tocreate.getCodePoste()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PosteAlreadyExist"), null);
                    } else if (existNomPoste == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TitrePosteAlreadyExist"), null);
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

    public Poste getPoste(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Poste> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Poste> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Poste> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Poste> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @FacesConverter(forClass = Poste.class)
    public static class PosteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PosteController controller = (PosteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "posteController");
            return controller.getPoste(getKey(value));
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
            if (object instanceof Poste) {
                Poste o = (Poste) object;
                return getStringKey(o.getCodePoste());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Poste.class.getName()});
                return null;
            }
        }

    }

}
