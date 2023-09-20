package rest;

import DAO.DAO_BDD;
import jakarta.ws.rs.*;
import jpa.Client;
import jpa.Professional;
import jpa.RDV;
import jpa.User;
import java.util.List;
import java.util.logging.Logger;

@Path("res")
@Produces({"application/xml", "application/json", "text/html"})
public class API
{

    @GET
    @Path("/user")
    public List<User> getListUser()
    {
        DAO_BDD bdd = new DAO_BDD();
        return bdd.listUsers();
    }

    @GET
    @Path("/user/{id}")
    public User getUser(@PathParam("id") long id)
    {
        DAO_BDD bdd = new DAO_BDD();
        return bdd.getUser(id);
    }

    @GET
    @Path("/rdv/{id}")
    public RDV getRdv(@PathParam("id") long id)
    {
        DAO_BDD bdd = new DAO_BDD();
        return bdd.getRdv(id);
    }

    @GET
    @Path("/rdv")
    public List<RDV> getListRdv()
    {
        DAO_BDD bdd = new DAO_BDD();
        return bdd.listRdvs();
    }

    @PUT
    @Path("/addProfessional")
    @Consumes({"application/json", "application/xml"})
    public void addPro(Professional pro)
    {
        DAO_BDD bdd = new DAO_BDD();
        if (pro != null)
            bdd.addPro(pro);
    }

    @PUT
    @Path("/addClient")
    @Consumes({"application/json", "application/xml"})
    public void addClient(Client client)
    {
        DAO_BDD bdd = new DAO_BDD();
        if (client != null)
            bdd.addClient(client);
    }

    @PUT
    @Path("/addRdv")
    @Consumes({"application/json", "application/xml"})
    public void addRdv(RDV rdv)
    {
        DAO_BDD bdd = new DAO_BDD();
        if (rdv != null)
            bdd.addRDV(rdv);
    }

    @DELETE
    @Path("/professional/{id}")
    public void deleteProfessional(@PathParam("id") long id)
    {
        DAO_BDD bdd = new DAO_BDD();
        bdd.deleteProfessional(id);
    }

    @DELETE
    @Path("/client/{id}")
    public void deleteClient(@PathParam("id") long id)
    {
        DAO_BDD bdd = new DAO_BDD();
        bdd.deleteClient(id);
    }

    @DELETE
    @Path("/rdv/{id}")
    public void deleteRdv(@PathParam("id") long id)
    {
        DAO_BDD bdd = new DAO_BDD();
        bdd.deleteRdv(id);
    }
}
