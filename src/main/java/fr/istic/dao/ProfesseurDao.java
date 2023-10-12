package fr.istic.dao;

import fr.istic.domain.Professeur;

public class ProfesseurDao extends AbstractJpaDao<Long, Professeur>
{
    public ProfesseurDao()
    {
        super(Professeur.class);
    }
}
