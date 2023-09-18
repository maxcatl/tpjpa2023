package jpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class RDV
{
    private Long id;
    private Date date;
    private Professional professional;
    private Client client;

    @Id
    @GeneratedValue
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
    public Professional getProfessional()
    {
        return professional;
    }

    public void setProfessional(Professional professional)
    {
        this.professional = professional;
    }

    @ManyToOne (cascade = CascadeType.PERSIST)
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
