/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.entre_bon_sortie;

import java.io.Serializable;
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
import net.betechs.mns.stock.entites.emp_article.Article;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sortie.findAll", query = "SELECT s FROM Sortie s")
    , @NamedQuery(name = "Sortie.findByIdSortie", query = "SELECT s FROM Sortie s WHERE s.idSortie = :idSortie")
    , @NamedQuery(name = "Sortie.findByQteArticle", query = "SELECT s FROM Sortie s WHERE s.qteArticle = :qteArticle")})
public class Sortie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sortie", nullable = false)
    private Integer idSortie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte_article", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Merci de saisir un chiffre entier.")
    @DecimalMin(value = "1", message = "La quantité doit être supérieure ou égale à 1.")
    private short qteArticle;
    @JoinColumns({
        @JoinColumn(name = "code_article", referencedColumnName = "code_article", nullable = false)
        , @JoinColumn(name = "code_famille_article", referencedColumnName = "code_famille", nullable = false)})
    @ManyToOne(optional = false)
    private Article article;
    @JoinColumn(name = "numero_bon_sortie", referencedColumnName = "numero_bon_sortie", nullable = false)
    @ManyToOne(optional = false)
    private Bonsortie numeroBonSortie;

    public Sortie() {
    }

    public Sortie(Integer idSortie) {
        this.idSortie = idSortie;
    }

    public Sortie(Integer idSortie, short qteArticle) {
        this.idSortie = idSortie;
        this.qteArticle = qteArticle;
    }

    public Integer getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(Integer idSortie) {
        this.idSortie = idSortie;
    }

    public short getQteArticle() {
        return qteArticle;
    }

    public void setQteArticle(short qteArticle) {
        this.qteArticle = qteArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Bonsortie getNumeroBonSortie() {
        return numeroBonSortie;
    }

    public void setNumeroBonSortie(Bonsortie numeroBonSortie) {
        this.numeroBonSortie = numeroBonSortie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSortie != null ? idSortie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sortie)) {
            return false;
        }
        Sortie other = (Sortie) object;
        if ((this.idSortie == null && other.idSortie != null) || (this.idSortie != null && !this.idSortie.equals(other.idSortie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Sortie[ idSortie=" + idSortie + " ]";
        return "Sortie de stock n° " + idSortie + " du bon de sortie n° " + numeroBonSortie.getNumeroBonSortie();
    }

}
