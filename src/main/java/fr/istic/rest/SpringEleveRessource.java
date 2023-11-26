
package fr.istic.rest;

import fr.istic.dao.SpringEleveDAO;
import fr.istic.domain.Eleve;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class SpringEleveRessource
{
    @Autowired
    private SpringEleveDAO bdd;

    @GetMapping("/eleve")
    @Operation(summary = "USER - retourne tous les élèves enregistrés")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public List<Eleve> getListEleve(JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");
        return bdd.findAll();
    }

    @GetMapping("/eleve/{id}")
    @Operation(summary = "USER - retourne l'élève enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun élève trouvé avec l'id donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public Eleve getEleve(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        Eleve e = bdd.findById(id);
        if (e == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun éléve trouvé avec l'id " + id);
        return e;
    }

    @GetMapping("/eleve/email/{email}")
    @Operation(summary = "USER - retourne l'élève enregistré avec l'email donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun élève trouvé avec l'email donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public Eleve getEleveByEmail(@PathVariable("email") String email, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        Eleve found = bdd.findByEmail(email);
        if (found != null)
            return found;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun élève trouvé avec l'email " + email);

    }

    @PutMapping("/eleve")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "TEACHER - crée ou met un jour l'élève donné")
    @ApiResponse(responseCode = "400", description = "si l'élève donné est null")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création des données")
    public void addUpdateEleve(@RequestBody Eleve eleve, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("TEACHER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a TEACHER or an ADMIN to access this ressource");

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
    @Operation(summary = "ADMIN - supprime l'élève enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la suppression des données")
//    @ResponseBody
    public void deleteEleveById(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("ADMIN"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be an ADMIN to access this ressource");
        bdd.deleteById(id);
    }
}