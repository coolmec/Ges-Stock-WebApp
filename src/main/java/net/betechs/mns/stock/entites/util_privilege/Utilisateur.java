/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.util_privilege;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.betechs.stock.entites.emp_article.Employe;

/**
 *
 * @author JAFFAR
 */
@Entity
@Table(catalog = "mns_stock", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"login"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u")
    , @NamedQuery(name = "Utilisateur.findByIdUtilisateur", query = "SELECT u FROM Utilisateur u WHERE u.idUtilisateur = :idUtilisateur")
//    , @NamedQuery(name = "Utilisateur.findByNomUtilisateur", query = "SELECT u FROM Utilisateur u WHERE u.nomUtilisateur = :nomUtilisateur")
//    , @NamedQuery(name = "Utilisateur.findByPrenomUtilisateur", query = "SELECT u FROM Utilisateur u WHERE u.prenomUtilisateur = :prenomUtilisateur")
    , @NamedQuery(name = "Utilisateur.findByLogin", query = "SELECT u FROM Utilisateur u WHERE u.login = :login")
    , @NamedQuery(name = "Utilisateur.findByPassword", query = "SELECT u FROM Utilisateur u WHERE u.password = :password")
    , @NamedQuery(name = "Utilisateur.findByTheme", query = "SELECT u FROM Utilisateur u WHERE u.theme = :theme")})
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Digits(integer = 3, fraction = 0, message = "L'ID doit être un nombre entier positif de 3 chiffres maximum.")
    @Min(value = 1, message = "L'ID doit être un chiffre entier positif.")
    @Column(name = "id_utilisateur", nullable = false)
    private Short idUtilisateur;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 30, message = "Le nom ne doit pas excéder 30 caractères.")
//    @Column(name = "nom_utilisateur", nullable = false, length = 30)
//    private String nomUtilisateur;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 50, message = "Le prénom ne doit pas excéder 50 caractères.")
//    @Column(name = "prenom_utilisateur", nullable = false, length = 50)
//    private String prenomUtilisateur;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20, message = "Le login ne doit pas excéder 20 caractères.")
    @Column(nullable = false, length = 20)
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 130, message = "Le mot de passe ne doit pas excéder 20 caractères.")
    @Column(nullable = false, length = 130)
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(nullable = false, length = 20)
    private String theme;
    @JoinColumn(name = "code_privilege_utilisateur", referencedColumnName = "code_privilege", nullable = false)
    @ManyToOne(optional = false)
    private Privilege codePrivilegeUtilisateur;
    @JoinColumn(name = "matricule_employe", referencedColumnName = "matricule_employe", nullable = false)
    @ManyToOne(optional = false)
    private Employe matriculeEmploye;

    public Utilisateur() {
    }

    public Utilisateur(Short idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Utilisateur(Short idUtilisateur, String nomUtilisateur, String prenomUtilisateur, String login, String password, String theme) {
        this.idUtilisateur = idUtilisateur;
//        this.nomUtilisateur = nomUtilisateur;
//        this.prenomUtilisateur = prenomUtilisateur;
        this.login = login;
        this.password = password;
        this.theme = theme;
    }

    public Short getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Short idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

//    public String getNomUtilisateur() {
//        return nomUtilisateur;
//    }
//
//    public void setNomUtilisateur(String nomUtilisateur) {
//        this.nomUtilisateur = nomUtilisateur;
//    }
//
//    public String getPrenomUtilisateur() {
//        return prenomUtilisateur;
//    }
//
//    public void setPrenomUtilisateur(String prenomUtilisateur) {
//        this.prenomUtilisateur = prenomUtilisateur;
//    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Privilege getCodePrivilegeUtilisateur() {
        return codePrivilegeUtilisateur;
    }

    public void setCodePrivilegeUtilisateur(Privilege codePrivilegeUtilisateur) {
        this.codePrivilegeUtilisateur = codePrivilegeUtilisateur;
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
        hash += (idUtilisateur != null ? idUtilisateur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.idUtilisateur == null && other.idUtilisateur != null) || (this.idUtilisateur != null && !this.idUtilisateur.equals(other.idUtilisateur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "us.pastec.mns.stock.entites.dep_fam_poste_liv.Utilisateur[ idUtilisateur=" + idUtilisateur + " ]";
        return login;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}
