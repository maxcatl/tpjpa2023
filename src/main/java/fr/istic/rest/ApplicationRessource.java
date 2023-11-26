package fr.istic.rest;

import fr.istic.dao.SpringRDVDAO;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@RestController
public class ApplicationRessource
{
    @Autowired
    SpringRDVDAO rdvDao;

    @PutMapping("/loadSampleData")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "ADMIN - créer un jeu de données de base")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création des données")
    public void loadSampleData(JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("ADMIN"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be an ADMIN to access this ressource");

        try
        {
            Eleve maxime = new Eleve("Le Gal", "Maxime", "maxime.legal@atos.net", "ILA");
            Eleve ilan = new Eleve("Huché", "Ilan", "ilan.huche@etudiant.univ-rennes.fr", "ILA");

            Professeur olivier = new Professeur("Barais", "Olivier", "olvier.barais@univ-rennes.fr", "Spring");
            Professeur noel = new Professeur("Plouzeau", "Noel", "noel.plouzeau@univ-rennes.fr", "Java");

            rdvDao.save(new RDV(olivier, maxime, new Date(), new Date(System.currentTimeMillis() + 5400000), "istic"));
            rdvDao.save(new RDV(noel, ilan, new Date(), new Date(System.currentTimeMillis() + 3600000), "i54"));
            rdvDao.save(new RDV(noel, maxime, new Date(), new Date(System.currentTimeMillis() + 1800000), "e108"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "une erreur est survenue lors de la création des données");
        }
    }

}