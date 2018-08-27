package net.betechs.stock.entites.entre_bon_sortie;

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
import javax.inject.Inject;
import javax.inject.Named;
import net.betechs.stock.entites.emp_article.Article;
import net.betechs.stock.entites.emp_article.ArticleController;
import net.betechs.stock.entites.emp_article.ArticleFacade;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil.PersistAction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("sortieController")
@SessionScoped
public class SortieController implements Serializable {

    @EJB
    private net.betechs.stock.entites.entre_bon_sortie.SortieFacade ejbFacade;
    @EJB
    private ArticleFacade ejbArticleFacade;
    @EJB
    private BonsortieFacade ejbBonsortieFacade;
    @Inject
    private ArticleController articleController;
    private List<Sortie> items = null, filteredItems, selectedSorties, toupdates;
    private Sortie selected, tocreate, toupdate;
    private String creation = "";
    private final String singleView = "SortieViewDialog", multipleView = "SortiesViewDialog";
    private final String singleEdit = "SortieEditDialog", multipleEdit = "SortiesEditDialog";
    private final String singleDelete = "SortieDeleteDialog", multipleDelete = "SortiesDeleteDialog";
    private UploadedFile file;

    public SortieController() {
    }

    public Sortie getToupdate() {
        return toupdate;
    }

    public void setToupdate(Sortie toupdate) {
        this.toupdate = getFacade().find(toupdate.getIdSortie());
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public Sortie getTocreate() {
        if (tocreate == null) {
            if (items.size() > 0) {
                tocreate = items.get(items.size() - 1); //Récupération de la dernière sortie créée
                tocreate.setIdSortie(items.get(items.size() - 1).getIdSortie() + 1);
            } else {
                tocreate = new Sortie();
                tocreate.setIdSortie(1);
            }
        }
        return tocreate;
    }

    public void setTocreate(Sortie tocreate) {
        this.tocreate = tocreate;
    }

    public Sortie getSelected() {
        return selected;
    }

    public void setSelected(Sortie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SortieFacade getFacade() {
        return ejbFacade;
    }

    public Sortie prepareCreate() {
        selected = new Sortie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SortieCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.            
        }
    }

    //Méthode pour la création de sorties de stock à partir d'un fichier .csv de structure idSortie;qteArticle;codeArticle;numeroBonSortie
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
                    net.betechs.stock.entites.dep_fam_poste_liv.util.JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
                }

                // Parcours de la liste de lignes de sorties récupérées, récupération et insertion des champs dans une nouvellle sortie, et création de la sortie en BDD
                Sortie sortie;
                for (String line : lines) {

                    String[] champs = line.split(";"); //Récupération des champs de chaque ligne de sortie

                    //Affectation de valeurs aux différents champs
                    sortie = new Sortie(Integer.valueOf(champs[0]), Short.parseShort(champs[1]));
                    sortie.setArticle(ejbArticleFacade.find(champs[2]));
                    sortie.setNumeroBonSortie(ejbBonsortieFacade.find(champs[3]));

                    this.setTocreate(sortie);
                    create();//Création de la ligne de sortie

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            net.betechs.stock.entites.emp_article.util.JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SortieUpdated"));
        items = null;
    }

    public void updateSelectedSortie() {
        this.setSelected(selectedSorties.get(0));
        this.setToupdate(toupdates.get(0));
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SortieUpdated"));
        articleController.updateFromUpdateSortie(this);
        selectedSorties = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void updateSelectedSorties() {
        for (int i = 0; i < selectedSorties.size(); i++) {
            this.setSelected(selectedSorties.get(i));
            this.setToupdate(toupdates.get(i));
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SortieUpdated"));
            articleController.updateFromUpdateSortie(this);
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedSorties = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SortieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedSortie() {
        this.selected = selectedSorties.get(0);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SortieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedSorties() {
        for (Sortie sortie : selectedSorties) {
            this.selected = sortie;
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SortieDeleted"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedSorties = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Sortie> getItems() {
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
                List<Sortie> sortie = getFacade().findAll();
                int nbArticles = 0;
                Boolean existNomSortie = false;
                List<Article> articles = new ArrayList<>();
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Sortie sortie1 : sortie) {
                        if (sortie1.getNumeroBonSortie().equals(selected.getNumeroBonSortie())) {
                            articles.add(sortie1.getArticle());
                            existNomSortie = true;
                            nbArticles = nbArticles + sortie1.getQteArticle();
                        }
                    }
                    if (existNomSortie == true && articles.contains(selected.getArticle()) && !toupdate.getArticle().equals(selected.getArticle())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieArticleAlreadyExist"), null);
                        creation = "failure";
                    } else if (existNomSortie == true && ((int) selected.getNumeroBonSortie().getNbArticles() < (nbArticles - toupdate.getQteArticle() + selected.getQteArticle()))) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieQteArticleIsTooHigh"), null);
                        creation = "failure";
                    } else if ((toupdate.getArticle().equals(selected.getArticle()) && (selected.getArticle().getQteEnStock() + toupdate.getQteArticle()) < selected.getQteArticle()) || (!toupdate.getArticle().equals(selected.getArticle()) && selected.getArticle().getQteEnStock() < selected.getQteArticle())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieQteArticleIsTooMuch"), null);
                        creation = "failure";
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                        creation = "success";
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                    creation = "success";
                } else {
                    for (Sortie sortie1 : sortie) {
                        if (sortie1.getNumeroBonSortie().equals(tocreate.getNumeroBonSortie())) {
                            articles.add(sortie1.getArticle());
                            existNomSortie = true;
                            nbArticles = nbArticles + sortie1.getQteArticle();
                        }
                    }
                    if (getFacade().find(tocreate.getIdSortie()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieAlreadyExist"), null);
                        creation = "failure";
                    } else if (existNomSortie == true && articles.contains(tocreate.getArticle())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieArticleAlreadyExist"), null);
                        creation = "failure";
                    } else if (tocreate.getArticle().getQteEnStock() < tocreate.getQteArticle()) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieQteArticleIsTooMuch"), null);
                        creation = "failure";
                    } else if (existNomSortie == true && ((int) tocreate.getNumeroBonSortie().getNbArticles() < (nbArticles + tocreate.getQteArticle()))) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SortieQteArticleIsTooHigh"), null);
                        creation = "failure";
                    } else {
                        getFacade().create(tocreate);
                        JsfUtil.addSuccessMessage(successMessage, null);
                        creation = "success";
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
        if (selectedSorties.size() < 1) {
            setSelectedSorties(null);
        }
    }

    public String viewLink() {
        if (selectedSorties == null) {
            return multipleView;
        }
        return selectedSorties.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedSorties == null) {
            return multipleEdit;
        }
        return selectedSorties.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedSorties == null) {
            return multipleDelete;
        }
        return selectedSorties.size() > 1 ? multipleDelete : singleDelete;
    }

    public Sortie getSortie(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Sortie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Sortie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Sortie> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Sortie> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Sortie> getSelectedSorties() {
        return selectedSorties;
    }

    public void setSelectedSorties(List<Sortie> selectedSorties) {
        this.selectedSorties = selectedSorties;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ArticleFacade getEjbArticleFacade() {
        return ejbArticleFacade;
    }

    public BonsortieFacade getEjbBonsortieFacade() {
        return ejbBonsortieFacade;
    }

    public List<Sortie> getToupdates() {
        return toupdates;
    }

    public void setToupdates(List<Sortie> toupdates) {
        this.toupdates = toupdates;
    }

    @FacesConverter(forClass = Sortie.class)
    public static class SortieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SortieController controller = (SortieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sortieController");
            return controller.getSortie(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Sortie) {
                Sortie o = (Sortie) object;
                return getStringKey(o.getIdSortie());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Sortie.class.getName()});
                return null;
            }
        }

    }

}
