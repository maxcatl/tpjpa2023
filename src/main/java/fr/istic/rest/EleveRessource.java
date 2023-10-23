package fr.istic.rest;

import fr.istic.dao.EleveDao;
import fr.istic.domain.Eleve;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;

import java.util.List;

@Path("eleve")
@Produces({"application/xml", "application/json"})
public class EleveRessource
{

    @GET
    @Path("/")
    public List<Eleve> getListEleve()
    {
        EleveDao bdd = new EleveDao();
        return bdd.findAll();
    }

    @GET
    @Path("/{id}")
    public Eleve getEleve(@PathParam("id") long id)
    {
        EleveDao bdd = new EleveDao();
        Eleve e = bdd.findOne(id);
        if (e == null)
            throw new NotFoundException();
        return e;
    }

    @GET
    @Path("/email/{email}")
    public Eleve getEleveByEmail(@PathParam("email") String email)
    {
        EleveDao bdd = new EleveDao();
        try
        {
            return bdd.findByEmail(email);
        }
        catch (NoResultException e)
        {
            throw new NotFoundException("No eleve with email " + email + " found !");
        }

    }

    @PUT
    @Path("/")
    @Consumes({"application/json", "application/xml"})
    public void addUpdateEleve(Eleve eleve) throws Exception
    {

        if (eleve != null)
        {
            EleveDao bdd = new EleveDao();

            //save
            if (eleve.getId() == 0)
            {
                if (!bdd.save(eleve))
                {
                    try
                    {
                        bdd.findByEmail(eleve.getEmail());
                        throw new Exception("Duplicate entry for Eleve with email " + eleve.getEmail());
                    }
                    catch (NoResultException e)
                    {
                        throw new Exception("An error occurred while saving");
                    }
                }
            }

            //update
            bdd.update(eleve);
        }

        else
            throw new NullPointerException("Eleve can't be null");
    }

    @DELETE
    @Path("/{id}")
    public void deleteEleveById(@PathParam("id") long id)
    {
        EleveDao bdd = new EleveDao();
        bdd.deleteById(id);
    }
}