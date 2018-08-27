/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.stock.entites.entre_bon_sortie;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import net.betechs.stock.entites.dep_fam_poste_liv.Livraison;
import net.betechs.stock.entites.emp_article.Article;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entree.findAll", query = "SELECT e FROM Entree e")
    , @NamedQuery(name = "Entree.findByIdEntree", query = "SELECT e FROM Entree e WHERE e.idEntree = :idEntree")
    , @NamedQuery(name = "Entree.findByQteArticle", query = "SELECT e FROM Entree e WHERE e.qteArticle = :qteArticle")
    , @NamedQuery(name = "Entree.findByPuTtcArticle", query = "SELECT e FROM Entree e WHERE e.puTtcArticle = :puTtcArticle")})
public class Entree implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_entree", nullable = false)
    private Integer idEntree;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte_article", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Merci de saisir un chiffre entier.")
    @DecimalMin(value = "1", message = "La quantité doit être supérieure ou égale à 1.")
    private short qteArticle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pu_ttc_article", nullable = false, precision = 9, scale = 3)
    private BigDecimal puTtcArticle;
    @JoinColumns({
        @JoinColumn(name = "code_article", referencedColumnName = "code_article", nullable = false)
        , @JoinColumn(name = "code_famille_article", referencedColumnName = "code_famille", nullable = false)})
    @ManyToOne(optional = false)
    private Article article;
    @JoinColumn(name = "numero_bl", referencedColumnName = "numero_bl", nullable = false)
    @ManyToOne(optional = false)
    private Livraison numeroBl;

    public Entree() {
    }

    public Entree(Integer idEntree) {
        this.idEntree = idEntree;
    }

    public Entree(Integer idEntree, short qteArticle, BigDecimal puTtcArticle) {
        this.idEntree = idEntree;
        this.qteArticle = qteArticle;
        this.puTtcArticle = puTtcArticle;
    }

    public Integer getIdEntree() {
        return idEntree;
    }

    public void setIdEntree(Integer idEntree) {
        this.idEntree = idEntree;
    }

    public short getQteArticle() {
        return qteArticle;
    }

    public void setQteArticle(short qteArticle) {
        this.qteArticle = qteArticle;
    }

    public BigDecimal getPuTtcArticle() {
        return puTtcArticle;
    }

    public void setPuTtcArticle(BigDecimal puTtcArticle) {
        this.puTtcArticle = puTtcArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Livraison getNumeroBl() {
        return numeroBl;
    }

    public void setNumeroBl(Livraison numeroBl) {
        this.numeroBl = numeroBl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntree != null ? idEntree.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entree)) {
            return false;
        }
        Entree other = (Entree) object;
        if ((this.idEntree == null && other.idEntree != null) || (this.idEntree != null && !this.idEntree.equals(other.idEntree))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Entree[ idEntree=" + idEntree + " ]";
        return "Entrée de stock n° " + idEntree + " de la livraison n° " + numeroBl.getNumeroBl();
    }

}
