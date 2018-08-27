package net.betechs.stock.entites.emp_article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
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
import net.betechs.stock.entites.dep_fam_poste_liv.PosteFacade;
import net.betechs.stock.entites.emp_article.util.JsfUtil;
import net.betechs.stock.entites.emp_article.util.JsfUtil.PersistAction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("employeController")
@SessionScoped
public class EmployeController implements Serializable {

    @EJB
    private EmployeFacade ejbFacade;
    @EJB
    private DepartementFacade ejbDepartementFacade;

    public EmployeFacade getEjbFacade() {
        return ejbFacade;
    }

    public DepartementFacade getEjbDepartementFacade() {
        return ejbDepartementFacade;
    }
    @EJB
    private PosteFacade ejbPosteFacade;
    private List<Employe> items = null, filteredItems, selectedEmployes;
    private Employe selected, tocreate;
    private final String singleView = "EmployeViewDialog", multipleView = "EmployesViewDialog";
    private final String singleEdit = "EmployeEditDialog", multipleEdit = "EmployesEditDialog";
    private final String singleDelete = "EmployeDeleteDialog", multipleDelete = "EmployesDeleteDialog";
    private UploadedFile file;

    public Employe getTocreate() {
        if (tocreate == null) {
            tocreate = new Employe();
        }
        return tocreate;
    }

    public void setTocreate(Employe tocreate) {
        this.tocreate = tocreate;
    }

    public EmployeController() {
    }

    public Employe getSelected() {
        return selected;
    }

    public void setSelected(Employe selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EmployeFacade getFacade() {
        return ejbFacade;
    }

    public Employe prepareCreate() {
        selected = new Employe();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EmployeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.            
        }
    }

    //Méthode pour la création d'employes à partir d'un fichier .csv de structure matriculeEmploye;nomEmploye;prenomEmploye;adresseEmploye;tel1;tel2;commentaires;départementEmploye;posteEmploye
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

                // Parcours de la liste de lignes d'employes récupérés, récupération et insertion des champs dans un nouvel employe, et création de l'employe en BDD
                Employe employe;
                for (String line : lines) {

                    String[] champs = line.split(";"); //Récupération des champs de chaque ligne d'article

                    //Affectation de valeurs aux différents champs
                    employe = new Employe(Short.parseShort(champs[0]), champs[1], champs[2], champs[3], champs[4], champs[5]);
                    employe.setCommentaires(champs[6]);
                    employe.setDepartementEmploye(ejbDepartementFacade.find(champs[7]));
                    employe.setPosteEmploye(ejbPosteFacade.find(champs[8]));

                    this.setTocreate(employe);
                    create();//Création de la ligne d'employe

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            net.betechs.stock.entites.emp_article.util.JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeUpdated"));
        selected = null;
        items = null;
    }

    public void updateSelectedEmploye() {
        this.selected = selectedEmployes.get(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeUpdated"));
        selectedEmployes = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void updateSelectedEmployes() {
        for (Employe employe : selectedEmployes) {
            this.selected = employe;
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeUpdated"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEmployes = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedEmployes = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedEmploye() {
        this.selected = selectedEmployes.get(0);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEmployes = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedEmployes() {
        for (Employe employe : selectedEmployes) {
            this.selected = employe;
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeDeleted"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEmployes = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Employe> getItems() {
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
                List<Employe> employe = getFacade().findAll();
                Boolean existNomEmploye = false;
                List<String> prenom = new ArrayList<>();
                Short matricule = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Employe employe1 : employe) {
                        if (employe1.getNomEmploye().equals(selected.getNomEmploye())) {
                            prenom.add(employe1.getPrenomEmploye());
                            existNomEmploye = true;
                        }
                        if (employe1.getNomEmploye().equals(selected.getNomEmploye()) && employe1.getPrenomEmploye().equals(selected.getPrenomEmploye())) {
                            matricule = employe1.getMatriculeEmploye();
                        }
                    }
                    if (existNomEmploye == true && !matricule.equals(selected.getMatriculeEmploye()) && prenom.contains(selected.getPrenomEmploye())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomEmployeAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Employe employe1 : employe) {
                        if (employe1.getNomEmploye().equals(tocreate.getNomEmploye())) {
                            prenom.add(employe1.getPrenomEmploye());
                            existNomEmploye = true;
                        }
                    }
                    if (getFacade().find(tocreate.getMatriculeEmploye()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EmployeAlreadyExist"), null);
                    } else if (existNomEmploye == true && prenom.contains(tocreate.getPrenomEmploye())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomEmployeAlreadyExist"), null);
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
        if (selectedEmployes.size() < 1) {
            setSelectedEmployes(null);
        }
    }

    public String viewLink() {
        if (selectedEmployes == null) {
            return multipleView;
        }
        return selectedEmployes.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedEmployes == null) {
            return multipleEdit;
        }
        return selectedEmployes.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedEmployes == null) {
            return multipleDelete;
        }
        return selectedEmployes.size() > 1 ? multipleDelete : singleDelete;
    }

    public Employe getEmploye(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Employe> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Employe> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Employe> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Employe> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Employe> getSelectedEmployes() {
        return selectedEmployes;
    }

    public void setSelectedEmployes(List<Employe> selectedEmployes) {
        this.selectedEmployes = selectedEmployes;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @FacesConverter(forClass = Employe.class)
    public static class EmployeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmployeController controller = (EmployeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "employeController");
            return controller.getEmploye(getKey(value));
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
            if (object instanceof Employe) {
                Employe o = (Employe) object;
                return getStringKey(o.getMatriculeEmploye());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Employe.class.getName()});
                return null;
            }
        }

    }

}
