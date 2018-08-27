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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.betechs.mns.stock.entites.emp_article.Employe;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poste.findAll", query = "SELECT p FROM Poste p")
    , @NamedQuery(name = "Poste.findByCodePoste", query = "SELECT p FROM Poste p WHERE p.codePoste = :codePoste")
    , @NamedQuery(name = "Poste.findByNomPoste", query = "SELECT p FROM Poste p WHERE p.nomPoste = :nomPoste")})
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_poste", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "Le code doit être un chiffre entier positif.")
    private Short codePoste;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50, message = "Le titre du poste ne doit pas excéder 50 caractères.")
    @Column(name = "nom_poste", nullable = false, length = 50)
    private String nomPoste;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description_poste", nullable = false, length = 65535)
    private String descriptionPoste;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "posteEmploye")
    private Collection<Employe> employeCollection;

    public Poste() {
    }

    public Poste(Short codePoste) {
        this.codePoste = codePoste;
    }

    public Poste(Short codePoste, String nomPoste, String descriptionPoste) {
        this.codePoste = codePoste;
        this.nomPoste = nomPoste;
        this.descriptionPoste = descriptionPoste;
    }

    public Short getCodePoste() {
        return codePoste;
    }

    public void setCodePoste(Short codePoste) {
        this.codePoste = codePoste;
    }

    public String getNomPoste() {
        return nomPoste;
    }

    public void setNomPoste(String nomPoste) {
        this.nomPoste = nomPoste;
    }

    public String getDescriptionPoste() {
        return descriptionPoste;
    }

    public void setDescriptionPoste(String descriptionPoste) {
        this.descriptionPoste = descriptionPoste;
    }

    @XmlTransient
    public Collection<Employe> getEmployeCollection() {
        return employeCollection;
    }

    public void setEmployeCollection(Collection<Employe> employeCollection) {
        this.employeCollection = employeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codePoste != null ? codePoste.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Poste)) {
            return false;
        }
        Poste other = (Poste) object;
        if ((this.codePoste == null && other.codePoste != null) || (this.codePoste != null && !this.codePoste.equals(other.codePoste))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Poste[ codePoste=" + codePoste + " ]";
        return codePoste + " - " + nomPoste;
    }

}
