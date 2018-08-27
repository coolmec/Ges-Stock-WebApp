/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.emp_article;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
import net.betechs.mns.stock.entites.dep_fam_poste_liv.Famille;
import net.betechs.mns.stock.entites.entre_bon_sortie.Entree;
import net.betechs.mns.stock.entites.entre_bon_sortie.Sortie;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_article"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a")
    , @NamedQuery(name = "Article.findByCodeArticle", query = "SELECT a FROM Article a WHERE a.articlePK.codeArticle = :codeArticle")
    , @NamedQuery(name = "Article.findByCodeFamille", query = "SELECT a FROM Article a WHERE a.articlePK.codeFamille = :codeFamille")
    , @NamedQuery(name = "Article.findByNomArticle", query = "SELECT a FROM Article a WHERE a.nomArticle = :nomArticle")
    , @NamedQuery(name = "Article.findByQteEnStock", query = "SELECT a FROM Article a WHERE a.qteEnStock = :qteEnStock")
    , @NamedQuery(name = "Article.findByPuTtcNormal", query = "SELECT a FROM Article a WHERE a.puTtcNormal = :puTtcNormal")
    , @NamedQuery(name = "Article.findByDernierPuTtc", query = "SELECT a FROM Article a WHERE a.dernierPuTtc = :dernierPuTtc")})
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArticlePK articlePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40, message = "Le nom ne doit pas excéder 40 caractères.")
    @Column(name = "nom_article", nullable = false, length = 30)
    private String nomArticle;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_article", length = 65535)
    private String descriptionArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte_en_stock", nullable = false)
    @Digits(integer = 5, fraction = 0, message = "Le code doit être un nombre entier positif de 5 chiffres maximum.")
    @Min(value = 0, message = "La quantité en stock doit être un chiffre entier positif.")
    private short qteEnStock;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pu_ttc_normal", precision = 9, scale = 3)
    @Digits(integer = 9, fraction = 3, message = "Le puTtcNormal doit être un nombre décimal positif de 12 chiffres maximum dont 03 décimales.")
    @Min(value = 0, message = "Le puTtcNormal doit être un chiffre entier ou décimal positif.")
    private BigDecimal puTtcNormal;
    @Column(name = "dernier_pu_ttc", precision = 9, scale = 3)
    @Digits(integer = 9, fraction = 3, message = "Le dernierPuTtc doit être un nombre décimal positif de 12 chiffres maximum dont 03 décimales.")
    @Min(value = 0, message = "Le dernierPuTtc doit être un chiffre entier ou décimal positif.")
    private BigDecimal dernierPuTtc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private Collection<Sortie> sortieCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private Collection<Entree> entreeCollection;
    @JoinColumn(name = "code_famille", referencedColumnName = "code_famille", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Famille famille;

    public Article() {
    }

    public Article(ArticlePK articlePK) {
        this.articlePK = articlePK;
    }

    public Article(ArticlePK articlePK, String nomArticle, short qteEnStock) {
        this.articlePK = articlePK;
        this.nomArticle = nomArticle;
        this.qteEnStock = qteEnStock;
    }

    public Article(short codeArticle, short codeFamille) {
        this.articlePK = new ArticlePK(codeArticle, codeFamille);
    }

    public ArticlePK getArticlePK() {
        return articlePK;
    }

    public void setArticlePK(ArticlePK articlePK) {
        this.articlePK = articlePK;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getDescriptionArticle() {
        return descriptionArticle;
    }

    public void setDescriptionArticle(String descriptionArticle) {
        this.descriptionArticle = descriptionArticle;
    }

    public short getQteEnStock() {
        return qteEnStock;
    }

    public void setQteEnStock(short qteEnStock) {
        this.qteEnStock = qteEnStock;
    }

    public BigDecimal getPuTtcNormal() {
        return puTtcNormal;
    }

    public void setPuTtcNormal(BigDecimal puTtcNormal) {
        this.puTtcNormal = puTtcNormal;
    }

    public BigDecimal getDernierPuTtc() {
        return dernierPuTtc;
    }

    public void setDernierPuTtc(BigDecimal dernierPuTtc) {
        this.dernierPuTtc = dernierPuTtc;
    }

    @XmlTransient
    public Collection<Sortie> getSortieCollection() {
        return sortieCollection;
    }

    public void setSortieCollection(Collection<Sortie> sortieCollection) {
        this.sortieCollection = sortieCollection;
    }

    @XmlTransient
    public Collection<Entree> getEntreeCollection() {
        return entreeCollection;
    }

    public void setEntreeCollection(Collection<Entree> entreeCollection) {
        this.entreeCollection = entreeCollection;
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (articlePK != null ? articlePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.articlePK == null && other.articlePK != null) || (this.articlePK != null && !this.articlePK.equals(other.articlePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Article[ articlePK=" + articlePK + " ]";
        return articlePK + " - " + nomArticle;
    }

}
