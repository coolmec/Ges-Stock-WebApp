/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.stock.entites.dep_fam_poste_liv;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.betechs.mns.stock.entites.entre_bon_sortie.Entree;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Livraison.findAll", query = "SELECT l FROM Livraison l")
    , @NamedQuery(name = "Livraison.findByNumeroBl", query = "SELECT l FROM Livraison l WHERE l.numeroBl = :numeroBl")
    , @NamedQuery(name = "Livraison.findByNumeroFacture", query = "SELECT l FROM Livraison l WHERE l.numeroFacture = :numeroFacture")
    , @NamedQuery(name = "Livraison.findByNumeroBc", query = "SELECT l FROM Livraison l WHERE l.numeroBc = :numeroBc")
    , @NamedQuery(name = "Livraison.findByDateLivraison", query = "SELECT l FROM Livraison l WHERE l.dateLivraison = :dateLivraison")
    , @NamedQuery(name = "Livraison.findByNbArticles", query = "SELECT l FROM Livraison l WHERE l.nbArticles = :nbArticles")
    , @NamedQuery(name = "Livraison.findByMontantFacture", query = "SELECT l FROM Livraison l WHERE l.montantFacture = :montantFacture")})
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20, message = "Le numéro de bordereau de livraison ne doit pas excéder 20 caractères.")
    @Column(name = "numero_bl", nullable = false, length = 20)
    private String numeroBl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20, message = "Le numéro de facture ne doit pas excéder 20 caractères.")
    @Column(name = "numero_facture", nullable = false, length = 20)
    private String numeroFacture;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20, message = "Le numéro de bon de commande ne doit pas excéder 20 caractères.")
    @Column(name = "numero_bc", nullable = false, length = 20)
    private String numeroBc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_livraison", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraison;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_articles", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le nombre d'article doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le nombre d'article doit être un chiffre entier positif supérieur à zéro.")
    private short nbArticles;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_facture", precision = 10, scale = 3)
    private BigDecimal montantFacture;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numeroBl")
    private Collection<Entree> entreeCollection;

    public Livraison() {
    }

    public Livraison(String numeroBl) {
        this.numeroBl = numeroBl;
    }

    public Livraison(String numeroBl, String numeroFacture, String numeroBc, Date dateLivraison, short nbArticles) {
        this.numeroBl = numeroBl;
        this.numeroFacture = numeroFacture;
        this.numeroBc = numeroBc;
        this.dateLivraison = dateLivraison;
        this.nbArticles = nbArticles;
    }

    public String getNumeroBl() {
        return numeroBl;
    }

    public void setNumeroBl(String numeroBl) {
        this.numeroBl = numeroBl;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getNumeroBc() {
        return numeroBc;
    }

    public void setNumeroBc(String numeroBc) {
        this.numeroBc = numeroBc;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public short getNbArticles() {
        return nbArticles;
    }

    public void setNbArticles(short nbArticles) {
        this.nbArticles = nbArticles;
    }

    public BigDecimal getMontantFacture() {
        return montantFacture;
    }

    public void setMontantFacture(BigDecimal montantFacture) {
        this.montantFacture = montantFacture;
    }

    @XmlTransient
    public Collection<Entree> getEntreeCollection() {
        return entreeCollection;
    }

    public void setEntreeCollection(Collection<Entree> entreeCollection) {
        this.entreeCollection = entreeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroBl != null ? numeroBl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Livraison)) {
            return false;
        }
        Livraison other = (Livraison) object;
        if ((this.numeroBl == null && other.numeroBl != null) || (this.numeroBl != null && !this.numeroBl.equals(other.numeroBl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Livraison[ numeroBl=" + numeroBl + " ]";
        return "Bon de livraison n° " + numeroBl;
    }

}
