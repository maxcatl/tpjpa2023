package DAO;

import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import fr.istic.domain.Utilisateur;
import jakarta.persistence.EntityManager;
import fr.istic.jpa.*;
import org.hibernate.PersistentObjectException;

import java.util.List;

public class DAO_BDD
{
    private EntityManager manager;

    public DAO_BDD()
    {
        manager = EntityManagerHelper.getEntityManager();
    }

    public boolean addClient(Eleve eleve)
    {
        if (eleve.getNom() != null && !eleve.getNom().isBlank() && eleve.getPrenom() != null && !eleve.getPrenom().isBlank())
        {
            manager.getTransaction().begin();
            manager.persist(eleve);
            manager.getTransaction().commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addPro(Professeur pro)
    {
        if (pro.getNom() != null && !pro.getNom().isBlank() && pro.getPrenom() != null && !pro.getPrenom().isBlank())
        {
            manager.getTransaction().begin();
            manager.persist(pro);
            manager.getTransaction().commit();
            return true;
        }
        else
            return false;
    }

    public boolean addRDV(RDV rdv)
    {
//        if (rdv.getEleve() != null && rdv.getProfesseur() != null && rdv.getDate() != null)
//        {
            try
            {
                manager.getTransaction().begin();
                manager.persist(rdv);
                manager.getTransaction().commit();
            }
            catch (PersistentObjectException e)
            {
                manager.merge(rdv);
                manager.persist(rdv);
                manager.getTransaction().commit();
            }
            return true;
//        }
//        return false;
    }

    public List<Professeur> listProfessionals()
    {
        return manager.createQuery("Select a From Professeur a", Professeur.class).getResultList();
    }

    public List<Eleve> listClients()
    {
        return manager.createQuery("select a from Eleve a", Eleve.class).getResultList();
    }

    public List<RDV> listRdvs()
    {
        return manager.createQuery("select a from RDV a", RDV.class).getResultList();
    }

    public Professeur getProfessional(long id)
    {
        return manager.createQuery("select a from Professeur a where a.id = " + id, Professeur.class).getSingleResult();
    }

    public Eleve getClient(long id)
    {
        return manager.createQuery("select a from Eleve a where a.id = " + id, Eleve.class).getSingleResult();
    }

    public RDV getRdv(long id)
    {
        return manager.createQuery("select a from RDV a where a.id = " + id, RDV.class).getSingleResult();
    }

    public List<Utilisateur> listUsers()
    {
        return manager.createQuery("select a from Utilisateur a", Utilisateur.class).getResultList();
    }

    public Utilisateur getUser(long id)
    {
        return manager.createQuery("select a from Utilisateur a where a.id = " + id, Utilisateur.class).getSingleResult();
    }

    public void deleteProfessional(long id)
    {
        manager.getTransaction().begin();
        manager.createQuery("delete from Professeur u where u.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteClient(long id)
    {
        manager.getTransaction().begin();
        manager.createQuery("delete from Eleve c where c.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteRdv(long id)
    {

        manager.getTransaction().begin();
        manager.createQuery("delete from RDV r where r.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }
}
