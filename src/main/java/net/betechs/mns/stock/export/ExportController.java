package net.betechs.mns.stock.imports;

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
@Named("exportController")
@SessionScoped
public class ExportController implements Serializable {

    private String table = "";
    private List<String> tables = new ArrayList<>();
    private boolean disabled = true;

    ExportController() {

    }

    public String exportDialog() {
        switch (this.table) {
            case "article":
                return "ExportArticles";
            case "employe":
                return "ExportEmployes";
            case "entree":
                return "ExportEntrees";
            case "sortie":
                return "ExportSorties";
            case "livraison":
                return "ExportLivraisons";
            case "bonsortie":
                return "ExportBonSorties";
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
        return table;
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
