package fr.istic.rest;

import fr.istic.DAO.DAO_BDD;
import fr.istic.DAO.SpringRDVDAO;
import fr.istic.DAO.SpringUserDAO;
import fr.istic.jpa.Client;
import fr.istic.jpa.Professional;
import fr.istic.jpa.RDV;
import fr.istic.jpa.User;
import java.util.List;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class API
{
    Logger logger = Logger.getLogger("API");
    @Autowired
    private SpringUserDAO userdao;
    @Autowired
    private SpringRDVDAO rdvdao;

    @GetMapping("/user")
    @ResponseBody
    public List<User> getListUser()
    {
        return userdao.findAll();
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") long id)
    {
        return userdao.findById(id);
    }


    @GetMapping("/rdv/{id}")
    @ResponseBody
    public RDV getRdv(@PathVariable("id") long id)
    {
        return rdvdao.findById(id);
    }

    @GetMapping("/rdv")
    @ResponseBody
    public List<RDV> getListRdv()
    {
        return rdvdao.findAll();
    }

    @PutMapping("/professional")
    @ResponseBody
    public void addPro(@RequestBody Professional pro)
    {
        if (pro != null)
            userdao.save(pro);
    }

    @PutMapping("/client")
    @ResponseBody
    public void addClient(@RequestBody Client client)
    {
        if (client != null)
            userdao.save(client);
    }


    @PutMapping("/rdv")
    @ResponseBody
    public void addRdv(@RequestBody RDV rdv)
    {
        if (rdv != null)
            rdvdao.save(rdv);
    }

    @DeleteMapping("/professional/{id}")
    @ResponseBody
    public void deleteProfessional(@PathVariable("id") long id)
    {
        userdao.deleteById(id);
    }

    @DeleteMapping("/client/{id}")
    @ResponseBody
    public void deleteClient(@PathVariable("id") long id)
    {
        userdao.deleteById(id);
    }

    @DeleteMapping("/rdv/{id}")
    @ResponseBody
    public void deleteRdv(@PathVariable("id") long id)
    {
        rdvdao.deleteById(id);
    }
}
