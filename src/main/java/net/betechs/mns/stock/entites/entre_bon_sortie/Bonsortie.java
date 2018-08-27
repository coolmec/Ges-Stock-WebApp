/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.entre_bon_sortie;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import net.betechs.mns.stock.entites.dep_fam_poste_liv.Departement;
import net.betechs.mns.stock.entites.emp_article.Employe;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bonsortie.findAll", query = "SELECT b FROM Bonsortie b")
    , @NamedQuery(name = "Bonsortie.findByNumeroBonSortie", query = "SELECT b FROM Bonsortie b WHERE b.numeroBonSortie = :numeroBonSortie")
    , @NamedQuery(name = "Bonsortie.findByDateSortie", query = "SELECT b FROM Bonsortie b WHERE b.dateSortie = :dateSortie")
    , @NamedQuery(name = "Bonsortie.findByNbArticles", query = "SELECT b FROM Bonsortie b WHERE b.nbArticles = :nbArticles")})
public class Bonsortie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15, message = "Le numéro du bon ne doit pas excéder 15 caractères.")
    @Column(name = "numero_bon_sortie", nullable = false, length = 15)
    private String numeroBonSortie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_sortie", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSortie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nb_articles", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le nombre d'article doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le nombre d'article doit être un chiffre entier positif supérieur à zéro.")
    private short nbArticles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numeroBonSortie")
    private Collection<Sortie> sortieCollection;
    @JoinColumn(name = "code_departement", referencedColumnName = "code_departement", nullable = false)
    @ManyToOne(optional = false)
    private Departement codeDepartement;
    @JoinColumn(name = "matricule_employe", referencedColumnName = "matricule_employe", nullable = false)
    @ManyToOne(optional = false)
    private Employe matriculeEmploye;

    public Bonsortie() {
    }

    public Bonsortie(String numeroBonSortie) {
        this.numeroBonSortie = numeroBonSortie;
    }

    public Bonsortie(String numeroBonSortie, Date dateSortie) {
        this.numeroBonSortie = numeroBonSortie;
        this.dateSortie = dateSortie;
    }

    public Bonsortie(String numeroBonSortie, Date dateSortie, short nbArticles) {
        this.numeroBonSortie = numeroBonSortie;
        this.dateSortie = dateSortie;
        this.nbArticles = nbArticles;
    }

    public String getNumeroBonSortie() {
        return numeroBonSortie;
    }

    public void setNumeroBonSortie(String numeroBonSortie) {
        this.numeroBonSortie = numeroBonSortie;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    public short getNbArticles() {
        return nbArticles;
    }

    public void setNbArticles(short nbArticles) {
        this.nbArticles = nbArticles;
    }

    @XmlTransient
    public Collection<Sortie> getSortieCollection() {
        return sortieCollection;
    }

    public void setSortieCollection(Collection<Sortie> sortieCollection) {
        this.sortieCollection = sortieCollection;
    }

    public Departement getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(Departement codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public Employe getMatriculeEmploye() {
        return matriculeEmploye;
    }

    public void setMatriculeEmploye(Employe matriculeEmploye) {
        this.matriculeEmploye = matriculeEmploye;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroBonSortie != null ? numeroBonSortie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bonsortie)) {
            return false;
        }
        Bonsortie other = (Bonsortie) object;
        if ((this.numeroBonSortie == null && other.numeroBonSortie != null) || (this.numeroBonSortie != null && !this.numeroBonSortie.equals(other.numeroBonSortie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Bonsortie[ numeroBonSortie=" + numeroBonSortie + " ]";
        return "Bon de sortie n° " + numeroBonSortie;
    }
}
