package fr.istic.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement(name = "eleve")
public class Eleve extends Utilisateur
{
    private String formation;
    private List<RDV> rdvs = new ArrayList<>();

    public Eleve(){}

    public Eleve(String name, String firstname, String email, String formation)
    {
        super(name, firstname, email);
        this.formation = formation;
    }

    @Override
    public String toString()
    {
        return "Eleve : " + getPrenom() + " " + getNom() + " - " + getEmail();
    }

    @OneToMany(mappedBy = "eleve")
    @XmlTransient
    public List<RDV> getRdvs()
    {
        return rdvs;
    }

    public Eleve setRdvs(List<RDV> rdvs)
    {
        if (rdvs == null)
            throw new IllegalArgumentException("The given list of rdvs is null");

        this.rdvs = rdvs;
        return this;
    }

    public Eleve addRdv(RDV rdv)
    {
        if (rdv == null)
            throw new IllegalArgumentException("The given rdv is null");

        if (rdvs == null)
            rdvs = new ArrayList<>();

        rdvs.add(rdv);
        return this;
    }

    public Eleve removeRdv(RDV rdv)
    {
        rdvs.remove(rdv);
        return this;
    }

    public String getFormation()
    {
        return formation;
    }

    public Eleve setFormation(String formation)
    {
        this.formation = formation;
        return this;
    }
}