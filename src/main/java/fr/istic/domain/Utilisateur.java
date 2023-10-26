package fr.istic.domain;

import jakarta.persistence.*;

import java.io.Serializable;


//@XmlRootElement(name = "Utilisateur")
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Utilisateur implements Serializable
{
    private long id;
    private String nom;
    private String prenom;
    private String email;

//    private List<RDV> rdvs = Collections.emptyList();

    protected Utilisateur(){}

    protected Utilisateur(String nom, String prenom, String email)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    //region getters/setters

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
//    @XmlElement(name = "id")
    public long getId()
    {
        return id;
    }

    public Utilisateur setId(long id)
    {
        this.id = id;
        return this;
    }

    //    @XmlElement(name = "name")
    public String getNom()
    {
        return nom;
    }

    public Utilisateur setNom(String nom)
    {
        this.nom = nom;
        return this;
    }

    //    @XmlElement(name = "firstname")
    public String getPrenom()
    {
        return prenom;
    }

    public Utilisateur setPrenom(String prenom)
    {
        this.prenom = prenom;
        return this;
    }

    @Column(unique = true)
    public String getEmail()
    {
        return email;
    }

    public Utilisateur setEmail(String email)
    {
        this.email = email;
        return this;
    }
    //endregion

    public String toString()
    {
        return ("Utilisateur " + id + " : " + prenom + " " + nom);
    }
}