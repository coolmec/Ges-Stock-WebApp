/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.emp_article;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author JAFFAR
 */
@Embeddable
public class ArticlePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code d'article doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le code d'article doit être un chiffre entier positif.")
    private short codeArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_famille", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code famille doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le code famille doit être un chiffre entier positif.")
    private short codeFamille;

    public ArticlePK() {
    }

    public ArticlePK(short codeArticle, short codeFamille) {
        this.codeArticle = codeArticle;
        this.codeFamille = codeFamille;
    }

    public short getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(short codeArticle) {
        this.codeArticle = codeArticle;
    }

    public short getCodeFamille() {
        return codeFamille;
    }

    public void setCodeFamille(short codeFamille) {
        this.codeFamille = codeFamille;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codeArticle;
        hash += (int) codeFamille;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArticlePK)) {
            return false;
        }
        ArticlePK other = (ArticlePK) object;
        if (this.codeArticle != other.codeArticle) {
            return false;
        }
        if (this.codeFamille != other.codeFamille) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.ArticlePK[ codeArticle=" + codeArticle + ", codeFamille=" + codeFamille + " ]";
        return codeArticle + "." + codeFamille;
    }

}
