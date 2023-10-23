package fr.istic.dao;

import fr.istic.domain.Professeur;

public class ProfesseurDao extends AbstractJpaDao<Long, Professeur>
{
    public ProfesseurDao()
    {
        super(Professeur.class);
    }

    public Professeur findByEmail(String email)
    {
        return entityManager.createQuery("select p from Professeur p where p.email = \"" + email + "\"",clazz).getSingleResult();
    }
}
