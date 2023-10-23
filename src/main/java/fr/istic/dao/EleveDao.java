package fr.istic.dao;

import fr.istic.domain.Eleve;

public class EleveDao extends AbstractJpaDao<Long, Eleve>
{
    public EleveDao()
    {
        super (Eleve.class);
    }

    public Eleve findByEmail(String email)
    {
        return entityManager.createQuery("select e from Eleve e where e.email = \"" + email + "\"", clazz).getSingleResult();
    }
}