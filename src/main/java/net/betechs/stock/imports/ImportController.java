package net.betechs.stock.imports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JAFFAR
 */
@Named("importController")
@SessionScoped
public class ImportController implements Serializable {

    private String table = "";
    private List<String> tables = new ArrayList<>();
    private boolean disabled = true;

    ImportController() {

    }

    public String importDialog() {
        switch (this.table) {
            case "article":
                return "ImportArticles";
            case "employe":
                return "ImportEmployes";
            case "entree":
                return "ImportEntrees";
            case "sortie":
                return "ImportSorties";
            case "livraison":
                return "ImportLivraisons";
            case "bonsortie":
                return "ImportBonSorties";
            default:
                return "";
        }
    }

    public List<String> getTables() {
        if (tables.isEmpty()) {
            tables.add("article");
            tables.add("employe");
            tables.add("entree");
            tables.add("sortie");
            tables.add("livraison");
            tables.add("bonsortie");
        }
        return tables;
    }

    public void enabled() {
        this.disabled = this.table.equals("");
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getTable() {
        setDisabled(true);
        return table = "";
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
