package net.betechs.mns.stock.entites.entre_bon_sortie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
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
import net.betechs.stock.entites.dep_fam_poste_liv.LivraisonFacade;
import net.betechs.stock.entites.emp_article.Article;
import net.betechs.stock.entites.emp_article.ArticleController;
import net.betechs.stock.entites.emp_article.ArticleFacade;
import net.betechs.stock.entites.emp_article.ArticlePK;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil;
import net.betechs.stock.entites.entre_bon_sortie.util.JsfUtil.PersistAction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("entreeController")
@SessionScoped
public class EntreeController implements Serializable {

    @EJB
    private EntreeFacade ejbFacade;
    @EJB
    private ArticleFacade ejbArticleFacade;
    @EJB
    private LivraisonFacade ejbLivraisonFacade;
    @Inject
    private ArticleController articleController;
    private List<Entree> items = null, filteredItems, selectedEntrees, toupdates;
    private Entree selected, tocreate, toupdate;
    private final String singleView = "EntreeViewDialog", multipleView = "EntreesViewDialog";
    private final String singleEdit = "EntreeEditDialog", multipleEdit = "EntreesEditDialog";
    private final String singleDelete = "EntreeDeleteDialog", multipleDelete = "EntreesDeleteDialog";
    UploadedFile file;
    private String creation;

    public EntreeController() {
    }

    public Entree getToupdate() {
        return toupdate;
    }

    public void setToupdate(Entree toupdate) {
        this.toupdate = getFacade().find(toupdate.getIdEntree());
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public Entree getTocreate() {
        if (tocreate == null) {
            if (items.size() > 0) {
                tocreate = items.get(items.size() - 1); //Récupération de la dernière sortie créée
                tocreate.setIdEntree(items.get(items.size() - 1).getIdEntree() + 1);
            } else {
                tocreate = new Entree();
                tocreate.setIdEntree(1);
            }
        }
        return tocreate;
    }

    public void setTocreate(Entree tocreate) {
        this.tocreate = tocreate;
    }

    public Entree getSelected() {
        return selected;
    }

    public void setSelected(Entree selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EntreeFacade getFacade() {
        return ejbFacade;
    }

    public Entree prepareCreate() {
        selected = new Entree();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EntreeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    //Méthode pour la création d'articles à partir d'un fichier .csv de structure idEntree;codeArticle;codeFamille;qteArticle;puTtcArticle;numeroBl
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

                // Parcours de la liste de lignes d'articles récupérées, récupération et insertion des champs dans un nouvel article, et création de l'article en BDD
                Entree entree;
                for (String article : lines) {

                    String[] champs = article.split(";"); //Récupération des champs de chaque ligne d'article

                    //Affectation de valeurs aux différents champs
                    entree = new Entree(Integer.valueOf(champs[0]), Short.parseShort(champs[3]), BigDecimal.valueOf(Double.parseDouble(champs[4])));
                    entree.setArticle(getEjbArticleFacade().find(champs[1]));
                    entree.setNumeroBl(getEjbLivraisonFacade().find(new ArticlePK(Short.parseShort(champs[1]), Short.parseShort(champs[2]))));

                    this.setTocreate(entree);
                    create();//Création de la ligne d'entree

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            net.betechs.stock.entites.emp_article.util.JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntreeUpdated"));
        items = null;
    }

    public void updateSelectedEntree() {
        this.setSelected(selectedEntrees.get(0));
        this.setToupdate(toupdates.get(0));
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntreeUpdated"));
        articleController.updateFromUpdateEntree(this);
        selectedEntrees = null; // Remove selection  
        items = null;
    }

    public void updateSelectedEntrees() {
        for (int i = 0; i < selectedEntrees.size(); i++) {
            this.setSelected(selectedEntrees.get(i));
            this.setToupdate(toupdates.get(i));
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EntreeUpdated"));
            articleController.updateFromUpdateEntree(this);
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEntrees = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedEntrees = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EntreeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedEntree() {
        this.setSelected(selectedEntrees.get(0));
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EntreeDeleted"));
        articleController.updateFromDeleteEntree(this);
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEntrees = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedEntrees() {
        for (Entree entree : selectedEntrees) {
            this.setSelected(entree);
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EntreeDeleted"));
            articleController.updateFromDeleteEntree(this);
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedEntrees = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Entree> getItems() {
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
                List<Entree> entree = getFacade().findAll();
                int nbArticles = 0;
                Boolean existNomEntree = false;
                List<Article> articles = new ArrayList<>();
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Entree entree1 : entree) {
                        if (entree1.getNumeroBl().equals(selected.getNumeroBl())) {
                            articles.add(entree1.getArticle());
                            existNomEntree = true;
                            nbArticles = nbArticles + entree1.getQteArticle();
                        }
                    }
                    if (existNomEntree == true && articles.contains(selected.getArticle()) && !toupdate.getArticle().equals(selected.getArticle())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeArticleAlreadyExist"), null);
                        creation = "failure";
                    } else if (existNomEntree == true && ((int) selected.getNumeroBl().getNbArticles() < (nbArticles - toupdate.getQteArticle() + selected.getQteArticle()))) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeQteArticleIsTooHigh"), null);
                        creation = "failure";
                    } else if ((toupdate.getArticle().equals(selected.getArticle()) && (selected.getArticle().getQteEnStock() + selected.getQteArticle() - toupdate.getQteArticle()) < 0) || (!toupdate.getArticle().equals(selected.getArticle()) && (toupdate.getArticle().getQteEnStock() - toupdate.getQteArticle()) < 0)) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeQteArticleIsTooMuch"), null);
                        creation = "failure";
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                        creation = "success";
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    if ((selected.getArticle().getQteEnStock() - selected.getQteArticle()) < 0) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeQteArticleIsTooMuch"), null);
                        creation = "failure";
                    } else {
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                        creation = "success";
                    }
                } else {
                    for (Entree entree1 : entree) {
                        if (entree1.getNumeroBl().equals(tocreate.getNumeroBl())) {
                            articles.add(entree1.getArticle());
                            existNomEntree = true;
                            nbArticles = nbArticles + entree1.getQteArticle();
                        }
                    }
                    if (getFacade().find(tocreate.getIdEntree()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeAlreadyExist"), null);
                        creation = "failure";
                    } else if (existNomEntree == true && articles.contains(tocreate.getArticle())) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeArticleAlreadyExist"), null);
                        creation = "failure";
                    } else if (existNomEntree == true && ((int) tocreate.getNumeroBl().getNbArticles() < (nbArticles + tocreate.getQteArticle()))) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EntreeQteArticleIsTooHigh"), null);
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
        if (selectedEntrees.size() < 1) {
            setSelectedEntrees(null);
        }
    }

    public String viewLink() {
        if (selectedEntrees == null) {
            return multipleView;
        }
        return selectedEntrees.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedEntrees == null) {
            return multipleEdit;
        }
        return selectedEntrees.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedEntrees == null) {
            return multipleDelete;
        }
        return selectedEntrees.size() > 1 ? multipleDelete : singleDelete;
    }

    public Entree getEntree(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Entree> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Entree> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Entree> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Entree> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Entree> getSelectedEntrees() {
        return selectedEntrees;
    }

    public void setSelectedEntrees(List<Entree> selectedEntrees) {
        this.selectedEntrees = selectedEntrees;
    }

    public ArticleFacade getEjbArticleFacade() {
        return ejbArticleFacade;
    }

    public LivraisonFacade getEjbLivraisonFacade() {
        return ejbLivraisonFacade;
    }

    public List<Entree> getToupdates() {
        return toupdates;
    }

    public void setToupdates(List<Entree> toupdates) {
        this.toupdates = toupdates;
    }

    @FacesConverter(forClass = Entree.class)
    public static class EntreeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EntreeController controller = (EntreeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "entreeController");
            return controller.getEntree(getKey(value));
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
            if (object instanceof Entree) {
                Entree o = (Entree) object;
                return getStringKey(o.getIdEntree());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Entree.class.getName()});
                return null;
            }
        }

    }

}
