package fr.istic.rest;

import fr.istic.dao.SpringProfesseurDAO;
import fr.istic.domain.Professeur;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class SpringProfesseurRessource
{

    @Autowired
    private SpringProfesseurDAO bdd;

    @GetMapping("/professeur")
    @Operation(summary = "USER - retourne tous les professeurs enregistrés")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public List<Professeur> getListProfesseur(JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        return bdd.findAll();
    }

    @GetMapping("/professeur/{id}")
    @Operation(summary = "USER - retourne le professeur enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun professeur trouvé avec l'id donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public Professeur getProfesseur(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        Professeur p = bdd.findById(id);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun éléve trouvé avec l'id " + id);
        return p;
    }

    @GetMapping("/professeur/email/{email}")
    @Operation(summary = "USER - retourne le professeur enregistré avec l'email donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun professeur trouvé avec l'email donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public Professeur getProfesseurByEmail(@PathVariable("email") String email, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        Professeur p = bdd.findByEmail(email);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun professeur trouvé avec l'email " + email);
        return p;



    }

    @PutMapping("/professeur")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "TEACHER - crée ou met un jour le professeur donné")
    @ApiResponse(responseCode = "400", description = "si le professeur donné est null")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création des données")
    public void addUpdateProfesseur(@RequestBody Professeur professeur, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("TEACHER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a TEACHER or an ADMIN to access this ressource");

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
    @Operation(summary = "ADMIN - supprime le professeur enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la suppression des données")
    public void deleteProfesseurById(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("ADMIN"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be an ADMIN to access this ressource");

        bdd.deleteById(id);
    }
}