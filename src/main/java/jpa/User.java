package jpa;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "User")
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User
{
    private long id;
    private String name;
    private String firstname;

    private List<RDV> rdvs = new ArrayList<>();

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlElement(name = "firstname")
    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String toString()
    {
        return ("Utilisateur " + id + " : " + firstname + " " + name);
    }

    public User(){}

    public User(String name, String firstname)
    {
        this.name = name;
        this.firstname = firstname;
    }
}
