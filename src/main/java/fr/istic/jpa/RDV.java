package fr.istic.jpa;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@Entity
@XmlRootElement (name = "rendez-vous")
public class RDV
{
    private Long id;
    private Date date;
    private Professional professional;
    private Client client;

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @XmlElement(name = "date")
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
    @XmlElement(name = "professional")
    public Professional getProfessional()
    {
        return professional;
    }

    public void setProfessional(Professional professional)
    {
        this.professional = professional;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
    @XmlElement(name = "client")
    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public RDV(){}

    public RDV(Date date, Professional professional, Client client)
    {
        this.date = date;
        this.professional = professional;
        this.client = client;
    }
}
