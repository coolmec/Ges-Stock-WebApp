/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.stock.entites.dep_fam_poste_liv;

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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.betechs.stock.entites.emp_article.Employe;
import net.betechs.mns.stock.entites.entre_bon_sortie.Bonsortie;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departement.findAll", query = "SELECT d FROM Departement d")
    , @NamedQuery(name = "Departement.findByCodeDepartement", query = "SELECT d FROM Departement d WHERE d.codeDepartement = :codeDepartement")
    , @NamedQuery(name = "Departement.findByNomDepartement", query = "SELECT d FROM Departement d WHERE d.nomDepartement = :nomDepartement")})
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_departement", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le code doit être un chiffre entier positif.")
    private Short codeDepartement;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30, message = "Le titre ne doit pas excéder 30 caractères.")
    @Column(name = "nom_departement", nullable = false, length = 30)
    private String nomDepartement;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_departement", length = 65535)
    private String descriptionDepartement;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departementEmploye")
    private Collection<Employe> employeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codeDepartement")
    private Collection<Bonsortie> bonsortieCollection;

    public Departement() {
    }

    public Departement(Short codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public Departement(Short codeDepartement, String nomDepartement) {
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
    }

    public Short getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(Short codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public String getDescriptionDepartement() {
        return descriptionDepartement;
    }

    public void setDescriptionDepartement(String descriptionDepartement) {
        this.descriptionDepartement = descriptionDepartement;
    }

    @XmlTransient
    public Collection<Employe> getEmployeCollection() {
        return employeCollection;
    }

    public void setEmployeCollection(Collection<Employe> employeCollection) {
        this.employeCollection = employeCollection;
    }

    @XmlTransient
    public Collection<Bonsortie> getBonsortieCollection() {
        return bonsortieCollection;
    }

    public void setBonsortieCollection(Collection<Bonsortie> bonsortieCollection) {
        this.bonsortieCollection = bonsortieCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeDepartement != null ? codeDepartement.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departement)) {
            return false;
        }
        Departement other = (Departement) object;
        if ((this.codeDepartement == null && other.codeDepartement != null) || (this.codeDepartement != null && !this.codeDepartement.equals(other.codeDepartement))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Departement[ codeDepartement=" + codeDepartement + " ]";
        return nomDepartement;
    }

}
