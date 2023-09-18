package rest;

import DAO.DAO_BDD;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jpa.User;

import java.util.List;

@Path("API")
@Produces({"application/json", "application/xml"})
public class API
{
    @GET
    @Path("/users")
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
}
