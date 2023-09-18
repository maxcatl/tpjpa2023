package jpa;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name = "client")
@Entity
public class Client extends User
{
//    private List<RDV> rdvs = new ArrayList<>();
//
//    @OneToMany (mappedBy = "client")
//    public List<RDV> getRdvs()
//    {
//        return rdvs;
//    }
//
//    public void setRdvs(List<RDV> newRdvs)
//    {
//        rdvs = newRdvs == null ? new ArrayList<>() : newRdvs;
//    }


    public Client(){}

    public Client(String name, String firstname)
    {
        super(name, firstname);
    }

//    public Client(String name, String firstname, List<RDV> rdvs)
//    {
//        super(name, firstname);
//        setRdvs(rdvs);
//    }

    @Override
    public String toString()
    {
        return "Client " + getId() + " : " + getFirstname() + " " + getName();
    }
}
