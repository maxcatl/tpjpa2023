package fr.istic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;


@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Utilisateur implements Serializable
{
    private long id;
    @NotNull
    private String nom;
    @NotNull
    private String prenom;
    @NotNull
    private String email;

//    private List<RDV> rdvs = Collections.emptyList();

    protected Utilisateur()
    {
    }

    protected Utilisateur(String nom, String prenom, String email)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    //region getters/setters

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public long getId()
    {
        return id;
    }

    public Utilisateur setId(long id)
    {
        this.id = id;
        return this;
    }

    @NotNull
    @Column(nullable = false)
    public String getNom()
    {
        return nom;
    }

    public Utilisateur setNom(String nom)
    {
        this.nom = nom;
        return this;
    }

    @NotNull
    @Column(nullable = false)
    public String getPrenom()
    {
        return prenom;
    }

    public Utilisateur setPrenom(String prenom)
    {
        this.prenom = prenom;
        return this;
    }

    @Column(unique = true, nullable = false)
    @NotNull
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