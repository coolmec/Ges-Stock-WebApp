package net.betechs.mns.stock.entites.entre_bon_sortie;

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
import net.betechs.stock.entites.dep_fam_poste_liv.DepartementFacade;
import net.betechs.stock.entites.emp_article.EmployeFacade;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil.PersistAction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("bonsortieController")
@SessionScoped
public class BonsortieController implements Serializable {

    @EJB
    private BonsortieFacade ejbFacade;
    @EJB
    private DepartementFacade ejbDepartementFacade;
    @EJB
    private EmployeFacade ejbEmployeFacade;
    private List<Bonsortie> items = null, filteredItems, selectedBonSorties;
    private Bonsortie selected, tocreate;
    private final String singleView = "BonSortieViewDialog", multipleView = "BonSortiesViewDialog";
    private final String singleEdit = "BonSortieEditDialog", multipleEdit = "BonSortiesEditDialog";
    private final String singleDelete = "BonSortieDeleteDialog", multipleDelete = "BonSortiesDeleteDialog";
    private UploadedFile file;

    public BonsortieController() {
    }

    public Bonsortie getTocreate() {
        if (tocreate == null) {
            tocreate = new Bonsortie();
        }
        return tocreate;
    }

    public void setTocreate(Bonsortie tocreate) {
        this.tocreate = tocreate;
    }

    public Bonsortie getSelected() {
        return selected;
    }

    public void setSelected(Bonsortie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private BonsortieFacade getFacade() {
        return ejbFacade;
    }

    public Bonsortie prepareCreate() {
        selected = new Bonsortie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("BonsortieCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            tocreate = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    //Méthode pour la création de bons de sortie à partir d'un fichier .csv de structure numeroBonSortie;dateBonSortie;codeDepartement;matriculeEmploye
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
                    net.betechs.entites.dep_fam_poste_liv.util.JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
                }

                // Parcours de la liste de lignes de bons de sortie récupérés, récupération et insertion des champs dans un nouveau bon, et création du bon en BDD
                Bonsortie bonsortie;
                for (String line : lines) {

                    String[] champs = line.split(";"); //Récupération des champs de chaque ligne d'article

                    //Affectation de valeurs aux différents champs
                    bonsortie = new Bonsortie(champs[0], Date.valueOf(champs[1]));
                    bonsortie.setCodeDepartement(ejbDepartementFacade.find(champs[2]));
                    bonsortie.setMatriculeEmploye(ejbEmployeFacade.find(champs[3]));

                    this.setTocreate(bonsortie);
                    create();//Création de la ligne de bon de sortie

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            net.betechs.stock.entites.emp_article.util.JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("BonsortieUpdated"));
        selected = null;
        items = null;
    }

    public void updateSelectedBonSortie() {
        this.selected = selectedBonSorties.get(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("BonsortieUpdated"));
        selectedBonSorties = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void updateSelectedBonSorties() {
        for (Bonsortie bonsortie : selectedBonSorties) {
            this.selected = bonsortie;
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("BonsortieUpdated"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedBonSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedBonSorties = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("BonsortieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedBonSortie() {
        this.selected = selectedBonSorties.get(0);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("BonsortieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedBonSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedBonSorties() {
        for (Bonsortie bonsortie : selectedBonSorties) {
            this.selected = bonsortie;
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("BonsortieDeleted"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedBonSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Bonsortie> getItems() {
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
                    if (getFacade().find(tocreate.getNumeroBonSortie()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("BonsortieAlreadyExist"), null);
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
        if (selectedBonSorties.size() < 1) {
            setSelectedBonSorties(null);
        }
    }

    public String viewLink() {
        if (selectedBonSorties == null) {
            return multipleView;
        }
        return selectedBonSorties.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedBonSorties == null) {
            return multipleEdit;
        }
        return selectedBonSorties.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedBonSorties == null) {
            return multipleDelete;
        }
        return selectedBonSorties.size() > 1 ? multipleDelete : singleDelete;
    }

    public Bonsortie getBonsortie(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Bonsortie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Bonsortie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Bonsortie> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Bonsortie> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Bonsortie> getSelectedBonSorties() {
        return selectedBonSorties;
    }

    public void setSelectedBonSorties(List<Bonsortie> selectedBonSorties) {
        this.selectedBonSorties = selectedBonSorties;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public DepartementFacade getEjbDepartementFacade() {
        return ejbDepartementFacade;
    }

    public EmployeFacade getEjbEmployeFacade() {
        return ejbEmployeFacade;
    }

    @FacesConverter(forClass = Bonsortie.class)
    public static class BonsortieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BonsortieController controller = (BonsortieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bonsortieController");
            return controller.getBonsortie(getKey(value));
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
            if (object instanceof Bonsortie) {
                Bonsortie o = (Bonsortie) object;
                return getStringKey(o.getNumeroBonSortie());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Bonsortie.class.getName()});
                return null;
            }
        }

    }

}
