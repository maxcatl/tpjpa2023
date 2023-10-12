package fr.istic.dao;

import fr.istic.domain.Eleve;

public class EleveDao extends AbstractJpaDao<Long, Eleve>
{
    public EleveDao()
    {
        super (Eleve.class);
    }
}