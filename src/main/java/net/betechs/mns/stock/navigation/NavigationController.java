/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JAFFAR
 */
@Named("navigationController")
@SessionScoped
public class NavigationController implements Serializable {

    private String navigation = ResourceBundle.getBundle("/Bundle").getString("AccueilNavigation");
    private List<Boolean> rendereds = new ArrayList<>();

    public NavigationController() {
        for (int i = 0; i < 12; i++) {
            rendereds.add(false);
            rendereds.set(0, Boolean.TRUE);

        }

    }

    public void navigate(String navigation) {

        switch (navigation) {
            case "article":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("ArticleNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(1, Boolean.TRUE);
            case "bonsortie":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("BonsortieNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(2, Boolean.TRUE);
            case "departement":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("DepartementNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(3, Boolean.TRUE);
            case "employe":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("EmployeNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(4, Boolean.TRUE);
            case "entree":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("EntreeNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(5, Boolean.TRUE);
            case "famille":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("FamilleNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(6, Boolean.TRUE);
            case "livraison":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("LivraisonNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(7, Boolean.TRUE);
            case "poste":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("PosteNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(8, Boolean.TRUE);
            case "privilege":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("PrivilegeNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(9, Boolean.TRUE);
            case "sortie":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("SortieNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(10, Boolean.TRUE);
            case "utilisateur":
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("UtilisateurNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(11, Boolean.TRUE);
            default:
                this.navigation = ResourceBundle.getBundle("/Bundle").getString("AccueilNavigation");
                for (Boolean render : rendereds) {
                    render = false;
                }
                rendereds.set(0, Boolean.TRUE);
        }
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public List<Boolean> getRendereds() {
        return rendereds;
    }

    public void setRendereds(List<Boolean> rendereds) {
        this.rendereds = rendereds;
    }

}
