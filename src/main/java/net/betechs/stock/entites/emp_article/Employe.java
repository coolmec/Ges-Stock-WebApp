/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.stock.entites.emp_article;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
import net.betechs.stock.entites.dep_fam_poste_liv.Departement;
import net.betechs.stock.entites.dep_fam_poste_liv.Poste;
import net.betechs.stock.entites.entre_bon_sortie.Bonsortie;
import net.betechs.stock.entites.util_privilege.Utilisateur;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employe.findAll", query = "SELECT e FROM Employe e")
    , @NamedQuery(name = "Employe.findByMatriculeEmploye", query = "SELECT e FROM Employe e WHERE e.matriculeEmploye = :matriculeEmploye")
    , @NamedQuery(name = "Employe.findByNomEmploye", query = "SELECT e FROM Employe e WHERE e.nomEmploye = :nomEmploye")
    , @NamedQuery(name = "Employe.findByPrenomEmploye", query = "SELECT e FROM Employe e WHERE e.prenomEmploye = :prenomEmploye")
    , @NamedQuery(name = "Employe.findByTelephone1Employe", query = "SELECT e FROM Employe e WHERE e.telephone1Employe = :telephone1Employe")
    , @NamedQuery(name = "Employe.findByTelephone2Employe", query = "SELECT e FROM Employe e WHERE e.telephone2Employe = :telephone2Employe")})
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricule_employe", nullable = false)
    @Digits(integer = 5, fraction = 0, message = "Le matricule doit être un nombre entier positif de 5 chiffres maximum.")
    @Min(value = 1, message = "Le matricule doit être un chiffre entier positif.")
    private Short matriculeEmploye;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30, message = "Le nom ne doit pas excéder 30 caractères.")
    @Column(name = "nom_employe", nullable = false, length = 30)
    private String nomEmploye;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50, message = "Le prénom ne doit pas excéder 50 caractères.")
    @Column(name = "prenom_employe", nullable = false, length = 50)
    private String prenomEmploye;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "adresse_employe", nullable = false, length = 65535)
    private String adresseEmploye;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30, message = "Le telephone1 ne doit pas excéder 30 caractères.")
    @Column(name = "telephone1_employe", nullable = false, length = 30)
    private String telephone1Employe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30, message = "Le telephone2 ne doit pas excéder 30 caractères.")
    @Column(name = "telephone2_employe", nullable = false, length = 30)
    private String telephone2Employe;
    @Lob
    @Size(max = 65535)
    @Column(length = 65535)
    private String commentaires;
    @JoinColumn(name = "departement_employe", referencedColumnName = "code_departement", nullable = false)
    @ManyToOne(optional = false)
    private Departement departementEmploye;
    @JoinColumn(name = "poste_employe", referencedColumnName = "code_poste", nullable = false)
    @ManyToOne(optional = false)
    private Poste posteEmploye;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculeEmploye")
    private Collection<Utilisateur> utilisateurCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculeEmploye")
    private Collection<Bonsortie> bonsortieCollection;

    public Employe() {
    }

    public Employe(Short matriculeEmploye) {
        this.matriculeEmploye = matriculeEmploye;
    }

    public Employe(Short matriculeEmploye, String nomEmploye, String prenomEmploye, String adresseEmploye, String telephone1Employe, String telephone2Employe) {
        this.matriculeEmploye = matriculeEmploye;
        this.nomEmploye = nomEmploye;
        this.prenomEmploye = prenomEmploye;
        this.adresseEmploye = adresseEmploye;
        this.telephone1Employe = telephone1Employe;
        this.telephone2Employe = telephone2Employe;
    }

    public Short getMatriculeEmploye() {
        return matriculeEmploye;
    }

    public void setMatriculeEmploye(Short matriculeEmploye) {
        this.matriculeEmploye = matriculeEmploye;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getPrenomEmploye() {
        return prenomEmploye;
    }

    public void setPrenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
    }

    public String getAdresseEmploye() {
        return adresseEmploye;
    }

    public void setAdresseEmploye(String adresseEmploye) {
        this.adresseEmploye = adresseEmploye;
    }

    public String getTelephone1Employe() {
        return telephone1Employe;
    }

    public void setTelephone1Employe(String telephone1Employe) {
        this.telephone1Employe = telephone1Employe;
    }

    public String getTelephone2Employe() {
        return telephone2Employe;
    }

    public void setTelephone2Employe(String telephone2Employe) {
        this.telephone2Employe = telephone2Employe;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Departement getDepartementEmploye() {
        return departementEmploye;
    }

    public void setDepartementEmploye(Departement departementEmploye) {
        this.departementEmploye = departementEmploye;
    }

    public Poste getPosteEmploye() {
        return posteEmploye;
    }

    public void setPosteEmploye(Poste posteEmploye) {
        this.posteEmploye = posteEmploye;
    }

    @XmlTransient
    public Collection<Utilisateur> getUtilisateurCollection() {
        return utilisateurCollection;
    }

    public void setUtilisateurCollection(Collection<Utilisateur> utilisateurCollection) {
        this.utilisateurCollection = utilisateurCollection;
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
        hash += (matriculeEmploye != null ? matriculeEmploye.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employe)) {
            return false;
        }
        Employe other = (Employe) object;
        if ((this.matriculeEmploye == null && other.matriculeEmploye != null) || (this.matriculeEmploye != null && !this.matriculeEmploye.equals(other.matriculeEmploye))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Employe[ matriculeEmploye=" + matriculeEmploye + " ]";
        return nomEmploye + " " + prenomEmploye;
    }

}
