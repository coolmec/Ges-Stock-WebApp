package net.betechs.mns.stock.entites.dep_fam_poste_liv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
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
import net.betechs.mns.stock.entites.dep_fam_poste_liv.util.JsfUtil;
import net.betechs.mns.stock.entites.dep_fam_poste_liv.util.JsfUtil.PersistAction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("livraisonController")
@SessionScoped
public class LivraisonController implements Serializable {

    @EJB
    private net.betechs.mns.stock.entites.dep_fam_poste_liv.LivraisonFacade ejbFacade;
    private List<Livraison> items = null, filteredItems, selectedLivraisons;
    private Livraison selected, tocreate;
    private final String singleView = "LivraisonViewDialog", multipleView = "LivraisonsViewDialog";
    private final String singleEdit = "LivraisonEditDialog", multipleEdit = "LivraisonsEditDialog";
    private final String singleDelete = "LivraisonDeleteDialog", multipleDelete = "LivraisonsDeleteDialog";
    private UploadedFile file;

    public LivraisonController() {
    }

    public Livraison getTocreate() {
        if (tocreate == null) {
            tocreate = new Livraison();
        }
        return tocreate;
    }

    public void setTocreate(Livraison tocreate) {
        this.tocreate = tocreate;
    }

    public Livraison getSelected() {
        return selected;
    }

    public void setSelected(Livraison selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LivraisonFacade getFacade() {
        return ejbFacade;
    }

    public Livraison prepareCreate() {
        selected = new Livraison();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            tocreate = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    //Méthode pour la création de livraisons à partir d'un fichier .csv de structure numeroBl;numeroFacture;numeroBc;nbArticles;montantFacture
    public void importFile(FileUploadEvent event) {
        this.file = event.getFile(); //Récupération du: fichier
        createFromImport();
    }

    public void createFromImport() {

        try {
            if (this.file != null) {
                //Récupération du contenu du fichier
                List<String> lines = new ArrayList<>(); //Déclaration d'une liste de String représentant les lignes du fichier
                Reader tmpReader = new InputStreamReader(this.file.getInputstream(), "utf-8");
                BufferedReader reader = new BufferedReader(tmpReader);
                try {
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        lines.add(line);
                    }
                } catch (IOException e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
                }

                // Parcours de la liste de lignes de livraisons récupérées, récupération et insertion des champs dans une nouvellle livraison, et création de la livraison en BDD
                Livraison livraison;
                for (String line : lines) {

                    String[] champs = line.split(";"); //Récupération des champs de chaque ligne d'article

                    //Affectation de valeurs aux différents champs
                    livraison = new Livraison(champs[0], champs[1], champs[2], Date.valueOf(champs[3]), Short.parseShort(champs[4]));

                    this.setTocreate(livraison);
                    create();//Création de la ligne d'livraison

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            net.betechs.mns.stock.entites.emp_article.util.JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonUpdated"));
        selected = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void updateSelectedLivraison() {
        this.selected = selectedLivraisons.get(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonUpdated"));
        selectedLivraisons = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void updateSelectedLivraisons() {
        for (Livraison entree : selectedLivraisons) {
            this.selected = entree;
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonUpdated"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedLivraisons = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedLivraisons = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LivraisonDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedLivraison() {
        this.selected = selectedLivraisons.get(0);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LivraisonDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedLivraisons = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedLivraisons() {
        for (Livraison livraison : selectedLivraisons) {
            this.selected = livraison;
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LivraisonDeleted"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedLivraisons = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Livraison> getItems() {
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
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    getFacade().edit(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    if (getFacade().find(tocreate.getNumeroBl()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LivraisonAlreadyExist"), null);
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

    public void onRowUnselect() {
        if (selectedLivraisons.size() < 1) {
            setSelectedLivraisons(null);
        }
    }

    public String viewLink() {
        if (selectedLivraisons == null) {
            return multipleView;
        }
        return selectedLivraisons.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedLivraisons == null) {
            return multipleEdit;
        }
        return selectedLivraisons.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedLivraisons == null) {
            return multipleDelete;
        }
        return selectedLivraisons.size() > 1 ? multipleDelete : singleDelete;
    }

    public Livraison getLivraison(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Livraison> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Livraison> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Livraison> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Livraison> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Livraison> getSelectedLivraisons() {
        return selectedLivraisons;
    }

    public void setSelectedLivraisons(List<Livraison> selectedLivraisons) {
        this.selectedLivraisons = selectedLivraisons;
    }

    @FacesConverter(forClass = Livraison.class)
    public static class LivraisonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LivraisonController controller = (LivraisonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "livraisonController");
            return controller.getLivraison(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Livraison) {
                Livraison o = (Livraison) object;
                return getStringKey(o.getNumeroBl());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Livraison.class.getName()});
                return null;
            }
        }

    }

}
