package fr.istic.rest;

import fr.istic.dao.ProfesseurDao;
import fr.istic.domain.Professeur;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;

import java.util.List;

@Path("professeur")
@Produces({"application/xml", "application/json"})
public class ProfesseurRessource
{

    @GET
    @Path("/")
    public List<Professeur> getListProfesseur()
    {
        ProfesseurDao bdd = new ProfesseurDao();
        return bdd.findAll();
    }

    @GET
    @Path("/{id}")
    public Professeur getProfesseur(@PathParam("id") long id)
    {
        ProfesseurDao bdd = new ProfesseurDao();
        Professeur p = bdd.findOne(id);
        if (p == null)
            throw new NotFoundException();

        return p;
    }

    @GET
    @Path("/email/{email}")
    public Professeur getProfesseurByEmail(@PathParam("email") String email)
    {
        ProfesseurDao bdd = new ProfesseurDao();
        try
        {
            return bdd.findByEmail(email);
        }
        catch (NoResultException e)
        {
            throw new NotFoundException("No professeur with email " + email + " found !");
        }

    }

    @PUT
    @Path("/")
    @Consumes({"application/json", "application/xml"})
    public void addUpdateProfesseur(Professeur professeur) throws Exception
    {

        if (professeur != null)
        {
            ProfesseurDao bdd = new ProfesseurDao();

            //save
            if (professeur.getId() == 0)
            {
                if (!bdd.save(professeur))
                {
                    try
                    {
                        bdd.findByEmail(professeur.getEmail());
                        throw new Exception("Duplicate entry for Professeur with email " + professeur.getEmail());
                    }
                    catch (NoResultException e)
                    {
                        throw new Exception("An error occurred while saving");
                    }
                }
            }

            //update
            bdd.update(professeur);
        }

        else
            throw new NullPointerException("professeur can't be null");
    }

    @DELETE
    @Path("/{id}")
    public void deleteProfesseurById(@PathParam("id") long id)
    {
        ProfesseurDao bdd = new ProfesseurDao();
        bdd.deleteById(id);
    }
}
