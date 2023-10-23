package fr.istic.dao;

import fr.istic.domain.RDV;

import java.util.List;

public class RDVDao extends AbstractJpaDao<Long, RDV>
{
    public RDVDao()
    {
        super(RDV.class);
    }

    public List<RDV> findAllWithEleve(long id)
    {
        return entityManager.createQuery("select r from RDV r where r.eleve.id = " + id , RDV.class).getResultList();
    }

    public List<RDV> findAllWithProfesseur(long id)
    {
        return entityManager.createQuery("select r from RDV r where r.professeur.id = " + id , RDV.class).getResultList();
    }
}
