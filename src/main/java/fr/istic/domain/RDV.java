package fr.istic.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

@Entity
//@XmlRootElement (name = "rendez-vous")
public class RDV implements Serializable
{
    private Long id;
    private Date dateDebut;
    private Date dateFin;
    private long duree;
    private Professeur professeur;
    private Eleve eleve;
    private String lieu;

    public RDV(){}

    public RDV(Professeur professeur, Eleve eleve, Date dateDebut, Date dateFin, String lieu)
    {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        calculerDuree();
        setProfesseur(professeur);
        setEleve(eleve);
        this.lieu = lieu;
    }

    //region getters/setters
    @Id
    @GeneratedValue(generator="native")
    @GenericGenerator(name = "native")
//    @XmlElement(name = "id")
    public Long getId()
    {
        return id;
    }

    public RDV setId(Long id)
    {
        this.id = id;
        return this;
    }

    //    @XmlElement(name = "date")
    public Date getDateDebut()
    {
        return dateDebut;
    }

    public RDV setDateDebut(Date date)
    {
        this.dateDebut = date;
        calculerDuree();
        return this;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public RDV setDateFin(Date date)
    {
        dateFin = date;
        calculerDuree();
        return this;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
//    @XmlElement(name = "professeur")
    public Professeur getProfesseur()
    {
        return professeur;
    }

    public RDV setProfesseur(Professeur professeur)
    {
        if (this.professeur != null)
            this.professeur.removeRdv(this);

        this.professeur = professeur;

        this.professeur.addRdv(this);
        return this;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
//    @XmlElement(name = "eleve")
    public Eleve getEleve()
    {
        return eleve;
    }

    public RDV setEleve(Eleve eleve)
    {
        if (this.eleve != null)
            this.eleve.removeRdv(this);

        this.eleve = eleve;

        this.eleve.addRdv(this);
        return this;
    }

    public String getLieu()
    {
        return lieu;
    }

    public RDV setLieu(String lieu)
    {
        this.lieu = lieu;
        return this;
    }

    public long getDuree()
    {
        return duree;
    }

    public RDV setDuree(long duree)
    {
        calculerDuree();
        return this;
    }

    private void calculerDuree()
    {
        if (dateDebut == null || dateFin == null)
            this.duree = 0;
        else
            this.duree = Duration.between(dateDebut.toInstant(), dateFin.toInstant()).toMinutes();
    }
    //endregion

    @Override
    public String toString()
    {
        return "Rendez-vous : " + getProfesseur() + " - " + getEleve() + " / " + getDateDebut() + " - " + getDateFin() + " / " + getLieu();
    }
}
