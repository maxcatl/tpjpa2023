package fr.istic.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//@XmlRootElement (name = "client")
@Entity
public class Eleve extends Utilisateur
{
    private String classe;
    private List<RDV> rdvs = new ArrayList<>();

    public Eleve(){}

    public Eleve(String name, String firstname, String email, String classe)
    {
        super(name, firstname, email);
        this.classe = classe;
    }

    @Override
    public String toString()
    {
        return "Eleve : " + getPrenom() + " " + getNom() + " - " + getEmail();
    }

    @OneToMany(mappedBy = "eleve")
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

    public String getClasse()
    {
        return classe;
    }

    public Eleve setClasse(String classe)
    {
        this.classe = classe;
        return this;
    }
}
