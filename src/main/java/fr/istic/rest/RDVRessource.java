package fr.istic.rest;

import fr.istic.dao.RDVDao;
import fr.istic.domain.RDV;
import jakarta.ws.rs.*;

import java.util.List;

@Path("rdv")
@Produces({"application/xml", "application/json"})
public class RDVRessource
{

    @GET
    @Path("/")
    public List<RDV> getListRDV()
    {
        RDVDao bdd = new RDVDao();
        return bdd.findAll();
    }

    @GET
    @Path("/{id}")
    public RDV getRDV(@PathParam("id") long id)
    {
        RDVDao bdd = new RDVDao();
        RDV r = bdd.findOne(id);
        if (r == null)
            throw new NotFoundException();

        return r;
    }

    @GET
    @Path("/withEleve/{id}")
    public List<RDV> getRDVsWithEleve(@PathParam("id") long id)
    {
        RDVDao bdd = new RDVDao();
        List<RDV> rdvs = bdd.findAllWithEleve(id);
        if (rdvs.isEmpty())
            throw new NotFoundException();
        return rdvs;
    }

    @GET
    @Path("/withProfesseur/{id}")
    public List<RDV> getRDVsWithProfesseur(@PathParam("id") long id)
    {
        RDVDao bdd = new RDVDao();
        List<RDV> rdvs = bdd.findAllWithProfesseur(id);
        if (rdvs.isEmpty())
            throw new NotFoundException();
        return rdvs;
    }

//    @GET
//    @Path("/email/{email}")
//    public Eleve getEleveByEmail(@PathParam("email") String email)
//    {
//        EleveDao bdd = new EleveDao();
//        try
//        {
//            return bdd.findByEmail(email);
//        }
//        catch (NoResultException e)
//        {
//            throw new NotFoundException("No eleve with email " + email + " found !");
//        }
//
//    }

    @PUT
    @Path("/")
    @Consumes({"application/json", "application/xml"})
    public void addUpdateRDV(RDV rdv) throws Exception
    {
        if (rdv != null)
        {
            RDVDao bdd = new RDVDao();

            //save
            if (rdv.getId() == null || rdv.getId() != 0)
            {
                bdd.update(rdv);
                return;
            }
            if (!bdd.save(rdv))
                throw new Exception("An error occurred while saving");
        }

        else
            throw new NullPointerException("rdv can't be null");
    }

    @DELETE
    @Path("/{id}")
    public void deleteRDVById(@PathParam("id") long id)
    {
        RDVDao bdd = new RDVDao();
        bdd.deleteById(id);
    }
}
