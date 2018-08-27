/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.util_privilege;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Privilege.findAll", query = "SELECT p FROM Privilege p")
    , @NamedQuery(name = "Privilege.findByCodePrivilege", query = "SELECT p FROM Privilege p WHERE p.codePrivilege = :codePrivilege")
    , @NamedQuery(name = "Privilege.findByLibellePrivilege", query = "SELECT p FROM Privilege p WHERE p.libellePrivilege = :libellePrivilege")})
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code_privilege", nullable = false)
    @Digits(integer = 3, fraction = 0, message = "Le code doit être un nombre entier positif de 3 chiffres maximum.")
    private Short codePrivilege;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15, message = "Le libelle ne doit pas excéder 15 caractères.")
    @Column(name = "libelle_privilege", nullable = false, length = 15)
    private String libellePrivilege;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_privilege", length = 65535)
    private String descriptionPrivilege;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codePrivilegeUtilisateur")
    private Collection<Utilisateur> utilisateurCollection;

    public Privilege() {
    }

    public Privilege(Short codePrivilege) {
        this.codePrivilege = codePrivilege;
    }

    public Privilege(Short codePrivilege, String libellePrivilege) {
        this.codePrivilege = codePrivilege;
        this.libellePrivilege = libellePrivilege;
    }

    public Short getCodePrivilege() {
        return codePrivilege;
    }

    public void setCodePrivilege(Short codePrivilege) {
        this.codePrivilege = codePrivilege;
    }

    public String getLibellePrivilege() {
        return libellePrivilege;
    }

    public void setLibellePrivilege(String libellePrivilege) {
        this.libellePrivilege = libellePrivilege;
    }

    public String getDescriptionPrivilege() {
        return descriptionPrivilege;
    }

    public void setDescriptionPrivilege(String descriptionPrivilege) {
        this.descriptionPrivilege = descriptionPrivilege;
    }

    @XmlTransient
    public Collection<Utilisateur> getUtilisateurCollection() {
        return utilisateurCollection;
    }

    public void setUtilisateurCollection(Collection<Utilisateur> utilisateurCollection) {
        this.utilisateurCollection = utilisateurCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codePrivilege != null ? codePrivilege.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        if ((this.codePrivilege == null && other.codePrivilege != null) || (this.codePrivilege != null && !this.codePrivilege.equals(other.codePrivilege))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Privilege[ codePrivilege=" + codePrivilege + " ]";
        return libellePrivilege;
    }

}
