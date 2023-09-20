package DAO;

import jakarta.persistence.EntityManager;
import jpa.*;
import org.hibernate.PersistentObjectException;

import java.util.List;

public class DAO_BDD
{
    private EntityManager manager;

    public DAO_BDD()
    {
        manager = EntityManagerHelper.getEntityManager();
    }

    public boolean addClient(Client client)
    {
        if (client.getName() != null && !client.getName().isBlank() && client.getFirstname() != null && !client.getFirstname().isBlank())
        {
            manager.getTransaction().begin();
            manager.persist(client);
            manager.getTransaction().commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addPro(Professional pro)
    {
        if (pro.getName() != null && !pro.getName().isBlank() && pro.getFirstname() != null && !pro.getFirstname().isBlank())
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
        if (rdv.getClient() != null && rdv.getProfessional() != null && rdv.getDate() != null)
        {
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
        }
        return false;
    }

    public List<Professional> listProfessionals()
    {
        return manager.createQuery("Select a From Professional a", Professional.class).getResultList();
    }

    public List<Client> listClients()
    {
        return manager.createQuery("select a from Client a", Client.class).getResultList();
    }

    public List<RDV> listRdvs()
    {
        return manager.createQuery("select a from RDV a", RDV.class).getResultList();
    }

    public Professional getProfessional(long id)
    {
        return manager.createQuery("select a from Professional a where a.id = " + id, Professional.class).getSingleResult();
    }

    public Client getClient(long id)
    {
        return manager.createQuery("select a from Client a where a.id = " + id, Client.class).getSingleResult();
    }

    public RDV getRdv(long id)
    {
        return manager.createQuery("select a from RDV a where a.id = " + id, RDV.class).getSingleResult();
    }

    public List<User> listUsers()
    {
        return manager.createQuery("select a from User a", User.class).getResultList();
    }

    public User getUser(long id)
    {
        return manager.createQuery("select a from User a where a.id = " + id, User.class).getSingleResult();
    }

    public void deleteProfessional(long id)
    {
        manager.getTransaction().begin();
        manager.createQuery("delete from Professional u where u.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteClient(long id)
    {
        manager.getTransaction().begin();
        manager.createQuery("delete from Client c where c.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteRdv(long id)
    {

        manager.getTransaction().begin();
        manager.createQuery("delete from RDV r where r.id = " + id).executeUpdate();
        manager.getTransaction().commit();
    }
}
