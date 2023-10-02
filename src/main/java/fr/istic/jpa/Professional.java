package fr.istic.jpa;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "professional")
@Entity
public class Professional extends User
{
//    private List<RDV> rdvs = new ArrayList<>();

//    @OneToMany (mappedBy = "professional")
//    public List<RDV> getRdvs()
//    {
//        return rdvs;
//    }
//
//    public void setRdvs(List<RDV> newRdvs)
//    {
//        rdvs = newRdvs == null ? new ArrayList<>() : newRdvs;
//    }


    public Professional(){}

    public Professional(String name, String firstname)
    {
        super(name, firstname);
    }

//    public Professional(String name, String firstname, List<RDV> rdvs)
//    {
//        super(name, firstname);
//        setRdvs(rdvs);
//    }

    @Override
    public String toString()
    {
        return "Professional " + getId() + " : " + getFirstname() + getName();
    }
}
