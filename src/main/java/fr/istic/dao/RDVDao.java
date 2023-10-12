package fr.istic.dao;

import fr.istic.domain.RDV;

public class RDVDao extends AbstractJpaDao<Long, RDV>
{
    public RDVDao()
    {
        super(RDV.class);
    }
}
