package fr.istic.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement(name = "professeur")
public class Professeur extends Utilisateur
{
    private String specialite;
    private List<RDV> rdvs = new ArrayList<>();

    public Professeur(){}

    public Professeur(String name, String firstname, String email, String specialite)
    {
        super(name, firstname, email);
        this.specialite = specialite;
    }

    @Override
    public String toString()
    {
        return "Professeur : " + getPrenom() + " " + getNom() + " - " + getEmail();
    }

    @OneToMany(mappedBy = "professeur")
    @XmlTransient
    public List<RDV> getRdvs()
    {
        return rdvs;
    }

    public Professeur setRdvs(List<RDV> rdvs)
    {
        if (rdvs == null)
            throw new IllegalArgumentException("The given list of rdvs is null");

        this.rdvs = rdvs;
        return this;
    }

    public Professeur addRdv(RDV rdv)
    {
        if (rdv == null)
            throw new IllegalArgumentException("The given rdv is null");

        if (rdvs == null)
            rdvs = new ArrayList<>();

        rdvs.add(rdv);
        return this;
    }

    public Professeur removeRdv(RDV rdv)
    {
        rdvs.remove(rdv);
        return this;
    }

    public String getSpecialite()
    {
        return specialite;
    }

    public Professeur setSpecialite(String specialite)
    {
        this.specialite = specialite;
        return this;
    }
}