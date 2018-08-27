package net.betechs.stock.connexion;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import net.betechs.stock.entites.dep_fam_poste_liv.util.JsfUtil;
import net.betechs.stock.entites.util_privilege.Utilisateur;
import net.betechs.stock.entites.util_privilege.UtilisateurController;
import net.betechs.stock.entites.util_privilege.UtilisateurFacade;
import org.primefaces.cache.CacheProvider;
import org.primefaces.context.RequestContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JAFFAR
 */
@Named("connexionController")
@SessionScoped
public class ConnexionController implements Serializable {

    private static final String WELCOME_PAGE = "bienvenue.xhtml?faces-redirect=true src=accueil.xhtml",
            CONNECTION_PAGE = "welcome.xhtml?faces-redirect=false",
            DISCONECTION_REDIRECT_PAGE = "/faces/welcome.xhtml?faces-redirect=true",
            CONNEXION_CONTROLLER = "connexionController",
            CACHE_REGION = "testcache",
            CACHE_KEY = "menu",
            CONNEXION_SUCCEED_MESSAGE_KEY = "ConnexionSucceed",
            CONNEXION_FAILED_MESSAGE_KEY = "ConnexionFailed";
    private String login = "", password = "", password2 = "", actualPassword = "";
    private Utilisateur utilisateur;
    private boolean connected, render;
    private Theme selectedTheme, theme;
    private List<Theme> themes;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }
    @EJB
    private UtilisateurFacade ejbUtilisateurFacade;
    @Inject
    private UtilisateurController utilisateurController;
    @Inject
    private ThemeService service;

    ConnexionController() {

    }

    public String connect() {
        for (Utilisateur user : ejbUtilisateurFacade.findAll()) {
            if (user.getLogin().equals(this.login)) {
                this.password2 = user.getPassword();
                this.utilisateur = user;
            }
        }
        if (this.password2.equals(utilisateurController.get_SHA_512_SecurePassword(password, password.substring(2, 4)))) {
            connected = true;
            render = this.utilisateur.getCodePrivilegeUtilisateur().getCodePrivilege().equals((short) 1);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString(CONNEXION_SUCCEED_MESSAGE_KEY), null);
            cacheEraser();
            return WELCOME_PAGE;
        } else {
            connected = false;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString(CONNEXION_FAILED_MESSAGE_KEY), null);
            return CONNECTION_PAGE;
        }
    }

    public String disconnect() {
        setLogin(null);
        setPassword(null);
        setPassword2(null);
        setConnected(false);
        setUtilisateur(new Utilisateur());
        cacheEraser();
        return DISCONECTION_REDIRECT_PAGE;
    }

    public void update() {
        if (this.password2.equals(utilisateurController.get_SHA_512_SecurePassword(actualPassword, actualPassword.substring(2, 4)))) {
            this.utilisateur.setPassword(utilisateurController.get_SHA_512_SecurePassword(password, password.substring(2, 4)));
            updateUser();
        }
    }

    public void setService(ThemeService service) {
        this.service = service;
    }

    public Theme getSelectedTheme() {
        if (utilisateur == null) {
            return themes.get(17);
        }
        for (Theme t : getThemes()) {
            if (t.getName().equals(utilisateur.getTheme())) {
                selectedTheme = t;
            }
        }
        return selectedTheme;
    }

    public void setSelectedTheme(Theme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    @FacesConverter(forClass = Theme.class)
    public static class ThemeConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConnexionController controller = (ConnexionController) context.getApplication().getELResolver().
                    getValue(context.getELContext(), null, CONNEXION_CONTROLLER);

            Theme th = null;
            for (Theme t : controller.getThemes()) {
                if (t.getName().equals(value)) {
                    th = t;
                }
            }
            return th;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Theme) {
                Theme t = (Theme) value;
                return t.getName();
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{value, value.getClass().getName(), Theme.class.getName()});
                return null;
            }
        }

    }

    public void changeTheme() {
        utilisateur.setTheme(theme.getName());
        updateUser();
    }

    private void updateUser() {
        utilisateurController.setSelected(this.utilisateur);
        utilisateurController.update();
    }

    private void cacheEraser() {
        CacheProvider cacheProvider = RequestContext.getCurrentInstance().getApplicationContext().getCacheProvider();
        cacheProvider.remove(CACHE_REGION, CACHE_KEY);
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UtilisateurFacade getEjbUtilisateurFacade() {
        return ejbUtilisateurFacade;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connexionStatus) {
        this.connected = connexionStatus;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public String getActualPassword() {
        return !"".equals(actualPassword) ? "" : actualPassword;
    }

    public void setActualPassword(String actualPassword) {
        this.actualPassword = actualPassword;
    }

}
