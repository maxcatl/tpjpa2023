package fr.istic.rest;

import fr.istic.dao.SpringProfesseurDAO;
import fr.istic.domain.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class SpringProfesseurRessource
{

    @Autowired
    private SpringProfesseurDAO bdd;

    @GetMapping("/professeur")
    public List<Professeur> getListProfesseur()
    {
        return bdd.findAll();
    }

    @GetMapping("/professeur/{id}")
    public Professeur getProfesseur(@PathVariable("id") long id)
    {
        Professeur p = bdd.findById(id);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun éléve trouvé avec l'id " + id);
        return p;
    }

    @GetMapping("/professeur/email/{email}")
    public Professeur getProfesseurByEmail(@PathVariable("email") String email)
    {
        Professeur p = bdd.findByEmail(email);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun professeur trouvé avec l'email " + email);
        return p;



    }

    @PutMapping("/professeur")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUpdateProfesseur(@RequestBody Professeur professeur)
    {
        if (professeur == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Le professeur ne peut être null");

        try
        {
            bdd.save(professeur);
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

    @DeleteMapping("/professeur/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfesseurById(@PathVariable("id") long id)
    {
        bdd.deleteById(id);
    }
}
