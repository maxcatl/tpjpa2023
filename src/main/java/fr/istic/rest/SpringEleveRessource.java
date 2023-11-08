package fr.istic.rest;

import fr.istic.dao.SpringEleveDAO;
import fr.istic.domain.Eleve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SpringEleveRessource
{

    @Autowired
    private SpringEleveDAO bdd;

    @GetMapping("/eleve")
    public List<Eleve> getListEleve()
    {
        return bdd.findAll();
    }

    @GetMapping("/eleve/{id}")
    public Eleve getEleve(@PathVariable("id") long id)
    {
        Eleve e = bdd.findById(id);
        if (e == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun éléve trouvé avec l'id " + id);
        return e;
    }

    @GetMapping("/eleve/email/{email}")
    public Eleve getEleveByEmail(@PathVariable("email") String email)
    {
        Eleve found = bdd.findByEmail(email);
        if (found != null)
            return found;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun élève trouvé avec l'email " + email);

    }

    @PutMapping("/eleve")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUpdateEleve(@RequestBody Eleve eleve)
    {
        if (eleve == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "L'élève ne peut être null");

        try
        {
            bdd.save(eleve);
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate entry"))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Un utilisateur avec une même adresse mail existe déjà");
            if (e.getMessage().contains("not-null property"))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Des valeurs données sont null");

            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue");
        }

    }

    @DeleteMapping("/eleve/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ResponseBody
    public void deleteEleveById(@PathVariable("id") long id)
    {
        bdd.deleteById(id);
    }
}
