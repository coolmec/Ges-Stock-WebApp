package net.betechs.stock.entites.emp_article;

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
import net.betechs.stock.entites.dep_fam_poste_liv.FamilleController;
import net.betechs.stock.entites.emp_article.util.JsfUtil;
import net.betechs.stock.entites.emp_article.util.JsfUtil.PersistAction;
import net.betechs.mns.stock.entites.entre_bon_sortie.Entree;
import net.betechs.mns.stock.entites.entre_bon_sortie.EntreeController;
import net.betechs.mns.stock.entites.entre_bon_sortie.SortieController;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("articleController")
@SessionScoped
public class ArticleController implements Serializable {

    @EJB
    private ArticleFacade ejbFacade;
    @Inject
    private FamilleController familleController;

    private List<Article> items, filteredItems, selectedArticles;
    private Article selected, tocreate;
    private FilterValue filterValue = new FilterValue();
    private final String singleView = "ArticleViewDialog", multipleView = "ArticlesViewDialog";
    private final String singleEdit = "ArticleEditDialog", multipleEdit = "ArticlesEditDialog";
    private final String singleDelete = "ArticleDeleteDialog", multipleDelete = "ArticlesDeleteDialog";
    UploadedFile file;

    public class FilterValue {

        private String nom, description, famille, code, puTtcNormal, qteEnStock, dernierPuTtc;

        FilterValue() {
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFamille() {
            return famille;
        }

        public void setFamille(String famille) {
            this.famille = famille;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPuTtcNormal() {
            return puTtcNormal;
        }

        public void setPuTtcNormal(String puTtcNormal) {
            this.puTtcNormal = puTtcNormal;
        }

        public String getQteEnStock() {
            return qteEnStock;
        }

        public void setQteEnStock(String qteEnStock) {
            this.qteEnStock = qteEnStock;
        }

        public String getDernierPuTtc() {
            return dernierPuTtc;
        }

        public void setDernierPuTtc(String dernierPuTtc) {
            this.dernierPuTtc = dernierPuTtc;
        }
    }

    public ArticleController() {
    }

    public FilterValue getFilterValue() {
        return this.filterValue;
    }

    public void setFilterValue(FilterValue filterValue) {
        this.filterValue = filterValue;
    }

    public Article getTocreate() {
        if (tocreate == null) {
            tocreate = new Article();
            tocreate.setArticlePK(new ArticlePK());
        }
        return tocreate;
    }

    public void setTocreate(Article tocreate) {
        this.tocreate = tocreate;
    }

    public Article getSelected() {
        return selected;
    }

    public void setSelected(Article selected) {
        this.selected = selected;
    }

    public List<Article> getItems() {
//        if (items == null) {
//            items = getFacade().findAll();
//        }
        items = null;
        items = getFacade().findAll();
        return items;
    }

    protected void setEmbeddableKeys() {
        if (tocreate != null && selected == null && getSelectedArticles().isEmpty()) {
            tocreate.getArticlePK().setCodeFamille(tocreate.getFamille().getCodeFamille());
        } else if (selected != null) {
            selected.getArticlePK().setCodeFamille(selected.getFamille().getCodeFamille());
        } else {
            for (Article article : selectedArticles) {
                article.getArticlePK().setCodeFamille(article.getFamille().getCodeFamille());
            }
        }
    }

    protected void initializeEmbeddableKey() {
//        selected.setArticlePK(new us.pastec.mns.stock.entites.emp_article.ArticlePK());
//        tocreate.setArticlePK(new us.pastec.mns.stock.entites.emp_article.ArticlePK());
    }

    private ArticleFacade getFacade() {
        return ejbFacade;
    }

    public Article prepareCreate() {
        selected = new Article();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selectedArticles = null;
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ArticleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            selected = null;
            tocreate = null;
        }
    }

    //Méthode pour la création d'articles à partir d'un fichier .csv de structure codeArticle;codeFamille;nomArticle;description;qteEnStock;puTtcNormal;dernierPuTtc
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
                Article articl;
                for (String article : lines) {

                    String[] champs = article.split(";"); //Récupération des champs de chaque ligne d'article

                    //Affectation de valeurs aux différents champs
                    articl = new Article(Short.parseShort(champs[0]), Short.parseShort(champs[1]));
                    articl.setFamille(familleController.getFamille(Short.valueOf(champs[1])));
                    articl.setNomArticle(champs[2]);
                    articl.setDescriptionArticle(champs[3]);
                    articl.setQteEnStock(Short.parseShort(champs[4]));
                    articl.setPuTtcNormal(BigDecimal.valueOf(Double.parseDouble(champs[5])));
                    articl.setDernierPuTtc(BigDecimal.valueOf(Double.parseDouble(champs[6])));

                    this.setTocreate(articl);
                    create();//Création de la ligne d'article

                }
            } else {
            }

        } catch (IOException | NumberFormatException iOException) {
            JsfUtil.addErrorMessage(iOException, ResourceBundle.getBundle("/Bundle").getString("ImportError"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ArticleUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void updateSelectedArticle() {
        this.selected = selectedArticles.get(0);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ArticleUpdated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection  
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void updateSelectedArticles() {
        for (Article article : selectedArticles) {
            this.selected = article;
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ArticleUpdated"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void cancel() {
        selected = null;
        selectedArticles = null;
        items = null;
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ArticleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedArticle() {
        this.selected = selectedArticles.get(0);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ArticleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroySelectedArticles() {
        for (Article article : selectedArticles) {
            this.selected = article;
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ArticleDeleted"));
        }
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            selectedArticles = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null || tocreate != null) {
            setEmbeddableKeys();
            try {
                List<Article> articles = getFacade().findAll();
                Boolean existNomArticle = false;
                Short codeArticle, codeFamille;
                codeArticle = codeFamille = (short) 0;
                if (persistAction != PersistAction.DELETE & persistAction != PersistAction.CREATE) {
                    for (Article article1 : articles) {
                        if (article1.getNomArticle().equals(selected.getNomArticle())) {
                            codeArticle = article1.getArticlePK().getCodeArticle();
                            codeFamille = article1.getArticlePK().getCodeFamille();
                            existNomArticle = true;
                        }
                    }
                    if ((existNomArticle == true && !codeArticle.equals(selected.getArticlePK().getCodeArticle())) || (existNomArticle == true && !codeFamille.equals(selected.getArticlePK().getCodeFamille()))) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomArticleAlreadyExist"), null);
                    } else {
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage(successMessage, null);
                    }
                } else if (persistAction != PersistAction.CREATE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage, null);
                } else {
                    for (Article article1 : articles) {
                        if (article1.getNomArticle().equals(tocreate.getNomArticle())) {
                            existNomArticle = true;
                        }
                    }
                    if (getFacade().find(tocreate.getArticlePK()) != null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ArticleAlreadyExist"), null);
                    } else if (existNomArticle == true) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NomArticleAlreadyExist"), null);
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

    public void updateFromCreateEntree(EntreeController entreeController) {
        if (entreeController.getCreation().equals("success")) {
            this.selected = entreeController.getTocreate().getArticle();
            this.selected.setQteEnStock((short) (this.selected.getQteEnStock() + entreeController.getTocreate().getQteArticle()));
            this.selected.setDernierPuTtc(entreeController.getTocreate().getPuTtcArticle());
            update();
        }
    }

    public void updateFromDeleteEntree(EntreeController entreeController) {
        if (entreeController.getCreation().equals("success")) {
            this.selected = entreeController.getSelected().getArticle();
            this.selected.setQteEnStock((short) (this.selected.getQteEnStock() - entreeController.getSelected().getQteArticle()));
            update();
            entreeController.setSelected(null);
        }
    }

    public void updateFromUpdateEntree(EntreeController entreeController) {
        if (entreeController.getCreation().equals("success")) {
            if (entreeController.getToupdate().getArticle().equals(entreeController.getSelected().getArticle())) {
                this.setSelected(entreeController.getSelected().getArticle());       //Affectation de l'article lié à l'entrée à la variable "selected" pour update     
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() - entreeController.getToupdate().getQteArticle() + entreeController.getSelected().getQteArticle())); //On récupère la qté en stock courante, on y rajoute la qte dans la sortie avant modif, puis on soustrait la nouvelle qte
                List<Entree> listEntrees = new ArrayList<>();
                for (Entree entree : entreeController.getItems()) {
                    if (this.selected.equals(entree.getArticle())) {
                        listEntrees.add(entree);
                    }
                }
                if (listEntrees.get(listEntrees.size() - 1).equals(entreeController.getSelected())) {
                    this.selected.setDernierPuTtc(entreeController.getSelected().getPuTtcArticle());
                }
                update();
                entreeController.setSelected(null);
            } else {
                this.selected = entreeController.getToupdate().getArticle();
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() - entreeController.getToupdate().getQteArticle()));
                List<Entree> listEntrees = new ArrayList<>();
                for (Entree entree : entreeController.getItems()) {
                    if (this.selected.equals(entree.getArticle())) {
                        listEntrees.add(entree);
                    }
                }
                if (listEntrees.size() > 0) {
                    this.selected.setDernierPuTtc(listEntrees.get(listEntrees.size() - 1).getPuTtcArticle());
                }
                update();
                this.setSelected(entreeController.getSelected().getArticle());       //Affectation de l'article lié à l'entrée à la variable "selected" pour update     
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() + entreeController.getSelected().getQteArticle())); //On récupère la qté en stock courante, on y retranche la qte dans la sortie avant modif, puis on rajoute la nouvelle qte
                for (Entree entree : entreeController.getItems()) {
                    if (this.selected.equals(entree.getArticle())) {
                        listEntrees.add(entree);
                    }
                }
                if (listEntrees.size() > 0) {
                    this.selected.setDernierPuTtc(listEntrees.get(listEntrees.size() - 1).getPuTtcArticle());
                }
                update();
                entreeController.setSelected(null);
            }
        }
    }

    public void updateFromCreateSortie(SortieController sortieController) {
        if (sortieController.getCreation().equals("success")) {
            this.selected = sortieController.getTocreate().getArticle();
            this.selected.setQteEnStock((short) (this.selected.getQteEnStock() - sortieController.getTocreate().getQteArticle()));
            update();
            sortieController.setTocreate(null);
        }
    }

    public void updateFromDeleteSortie(SortieController sortieController) {
        if (sortieController.getCreation().equals("success")) {
            this.selected = sortieController.getSelected().getArticle();
            this.selected.setQteEnStock((short) (this.selected.getQteEnStock() + sortieController.getSelected().getQteArticle()));
            update();
            sortieController.setSelected(null);
        }
    }

    public void updateFromUpdateSortie(SortieController sortieController) {
        if (sortieController.getCreation().equals("success")) {
            if (sortieController.getToupdate().getArticle().equals(sortieController.getSelected().getArticle())) {
                this.setSelected(sortieController.getSelected().getArticle());       //Affectation de l'article lié à la sortie à la variable "selected" pour update     
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() + sortieController.getToupdate().getQteArticle() - sortieController.getSelected().getQteArticle())); //On récupère la qté en stock courante, on y rajoute la qte dans la sortie avant modif, puis on soustrait la nouvelle qte
                update();
                sortieController.setSelected(null);
            } else {
                this.selected = sortieController.getToupdate().getArticle();
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() + sortieController.getToupdate().getQteArticle()));
                update();
                this.setSelected(sortieController.getSelected().getArticle());       //Affectation de l'article lié à la sortie à la variable "selected" pour update     
                this.selected.setQteEnStock((short) (this.selected.getQteEnStock() - sortieController.getSelected().getQteArticle())); //On récupère la qté en stock courante, on y rajoute la qte dans la sortie avant modif, puis on soustrait la nouvelle qte
                update();
                sortieController.setSelected(null);
            }
        }
    }

    public void onRowUnselect() {
        if (selectedArticles.size() < 1) {
            setSelectedArticles(null);
        }
    }

    public String viewLink() {
        if (selectedArticles == null) {
            return multipleView;
        }
        return selectedArticles.size() > 1 ? multipleView : singleView;
    }

    public String editLink() {
        if (selectedArticles == null) {
            return multipleEdit;
        }
        return selectedArticles.size() > 1 ? multipleEdit : singleEdit;
    }

    public String deleteLink() {
        if (selectedArticles == null) {
            return multipleDelete;
        }
        return selectedArticles.size() > 1 ? multipleDelete : singleDelete;
    }

    public net.betechs.stock.entites.emp_article.ArticleFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(net.betechs.stock.entites.emp_article.ArticleFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Article> getSelectedArticles() {
        return selectedArticles == null ? new ArrayList<Article>() : selectedArticles;
    }

    public void setSelectedArticles(List<Article> selectedArticles) {
        this.selectedArticles = selectedArticles;
    }

    public Article getArticle(net.betechs.stock.entites.emp_article.ArticlePK id) {
        return getFacade().find(id);
    }

    public List<Article> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Article> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Article> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Article> filteredItems) {
        this.filteredItems = filteredItems;
    }

    @FacesConverter(forClass = Article.class)
    public static class ArticleControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArticleController controller = (ArticleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "articleController");
            return controller.getArticle(getKey(value));
        }

        net.betechs.stock.entites.emp_article.ArticlePK getKey(String value) {
            net.betechs.stock.entites.emp_article.ArticlePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new net.betechs.stock.entites.emp_article.ArticlePK();
            key.setCodeArticle(Short.parseShort(values[0]));
            key.setCodeFamille(Short.parseShort(values[1]));
            return key;
        }

        String getStringKey(net.betechs.stock.entites.emp_article.ArticlePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodeArticle());
            sb.append(SEPARATOR);
            sb.append(value.getCodeFamille());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Article) {
                Article o = (Article) object;
                return getStringKey(o.getArticlePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Article.class.getName()});
                return null;
            }
        }

    }

}
