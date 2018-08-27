/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.dep_fam_poste_liv;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.betechs.mns.stock.entites.emp_article.Article;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_famille"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Famille.findAll", query = "SELECT f FROM Famille f")
    , @NamedQuery(name = "Famille.findByCodeFamille", query = "SELECT f FROM Famille f WHERE f.codeFamille = :codeFamille")
    , @NamedQuery(name = "Famille.findByNomFamille", query = "SELECT f FROM Famille f WHERE f.nomFamille = :nomFamille")})
public class Famille implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_famille", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le code doit être un chiffre entier positif.")
    private Short codeFamille;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30, message = "Le nom ne doit pas excéder 30 caractères.")
    @Column(name = "nom_famille", nullable = false, length = 30)
    private String nomFamille;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_famille", length = 65535)
    private String descriptionFamille;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "famille")
    private Collection<Article> articleCollection;

    public Famille() {
    }

    public Famille(Short codeFamille) {
        this.codeFamille = codeFamille;
    }

    public Famille(Short codeFamille, String nomFamille) {
        this.codeFamille = codeFamille;
        this.nomFamille = nomFamille;
    }

    public Short getCodeFamille() {
        return codeFamille;
    }

    public void setCodeFamille(Short codeFamille) {
        this.codeFamille = codeFamille;
    }

    public String getNomFamille() {
        return nomFamille;
    }

    public void setNomFamille(String nomFamille) {
        this.nomFamille = nomFamille;
    }

    public String getDescriptionFamille() {
        return descriptionFamille;
    }

    public void setDescriptionFamille(String descriptionFamille) {
        this.descriptionFamille = descriptionFamille;
    }

    @XmlTransient
    public Collection<Article> getArticleCollection() {
        return articleCollection;
    }

    public void setArticleCollection(Collection<Article> articleCollection) {
        this.articleCollection = articleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeFamille != null ? codeFamille.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Famille)) {
            return false;
        }
        Famille other = (Famille) object;
        if ((this.codeFamille == null && other.codeFamille != null) || (this.codeFamille != null && !this.codeFamille.equals(other.codeFamille))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Famille[ codeFamille=" + codeFamille + " ]";
        return codeFamille + " - " + nomFamille;
    }

}
