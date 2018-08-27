package net.betechs.mns.stock.entites.util_privilege;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import net.betechs.stock.entites.util_privilege.util.JsfUtil;
import net.betechs.stock.entites.util_privilege.util.JsfUtil.PersistAction;

@Named("utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {
    
    @EJB
    private net.betechs.mns.stock.entites.util_privilege.UtilisateurFacade ejbFacade;
    private List<Utilisateur> items = null, filteredItems;
    private Utilisateur selected, tocreate;
    
    public UtilisateurController() {
    }
    
    public Utilisateur getTocreate() {
        if (tocreate == null) {
            if (items.size() > 0) {
                tocreate = items.get(items.size() - 1); //Récupération du dernier utilisateur créé
                tocreate.setIdUtilisateur((short) (items.get(items.size() - 1).getIdUtilisateur() + 1));
            } else {
                tocreate = new Utilisateur();
                tocreate.setIdUtilisateur((short) 1);
            }
        }
        return tocreate;
    }
    
    public void setTocreate(Utilisateur tocreate) {
        this.tocreate = tocreate;
    }
    
    public Utilisateur getSelected() {
        return selected;
    }
    
    public void setSelected(Utilisateur selected) {
        this.selected = selected;
    }
    
    protected void setEmbeddableKeys() {
    }
    
    protected void initializeEmbeddableKey() {
    }
    
    private UtilisateurFacade getFacade() {
        return ejbFacade;
    }
    
    public Utilisateur prepareCreate() {
        selected = new Utilisateur();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.            
        }
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurUpdated"));
        selected = null;
        items = null;
    }
    
    public void cancel() {
        selected = null;
        items = null;
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public List<Utilisateur> getItems() {
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
                List<Utilisateur> utilisateur = getFacade().findAll();
                Boolean existLoginUtilisateur = false;
                Short code = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Utilisateur utilisateur1 : utilisateur) {
                        if (utilisateur1.getLogin().equals(selected.getLogin())) {
                            code = utilisateur1.getIdUtilisateur();
                            existLoginUtilisateur = true;
                        }
                    }
                    if (existLoginUtilisateur == true && !code.equals(selected.getIdUtilisateur())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LoginUtilisateurAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Utilisateur utilisateur1 : utilisateur) {
                        if (utilisateur1.getLogin().equals(tocreate.getLogin())) {
                            existLoginUtilisateur = true;
                        }
                    }
                    if (getFacade().find(tocreate.getIdUtilisateur()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UtilisateurAlreadyExist"), null);
                    } else if (existLoginUtilisateur == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LoginUtilisateurAlreadyExist"), null);
                    } else {
                        tocreate.setPassword(get_SHA_512_SecurePassword(tocreate.getPassword(), tocreate.getPassword().substring(2, 4)));
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
    
    public String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    public Utilisateur getUtilisateur(java.lang.Short id) {
        return getFacade().find(id);
    }
    
    public List<Utilisateur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }
    
    public List<Utilisateur> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public List<Utilisateur> getFilteredItems() {
        return filteredItems;
    }
    
    public void setFilteredItems(List<Utilisateur> filteredItems) {
        this.filteredItems = filteredItems;
    }
    
    @FacesConverter(forClass = Utilisateur.class)
    public static class UtilisateurControllerConverter implements Converter {
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilisateurController controller = (UtilisateurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilisateurController");
            return controller.getUtilisateur(getKey(value));
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
            if (object instanceof Utilisateur) {
                Utilisateur o = (Utilisateur) object;
                return getStringKey(o.getIdUtilisateur());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Utilisateur.class.getName()});
                return null;
            }
        }
        
    }
    
}
