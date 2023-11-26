package fr.istic.rest;

import fr.istic.dao.SpringEleveDAO;
import fr.istic.dao.SpringProfesseurDAO;
import fr.istic.dao.SpringRDVDAO;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
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
public class SpringRDVRessource
{

    @Autowired
    private SpringRDVDAO rdvDao;
    @Autowired
    private SpringEleveDAO eleveDAO;
    @Autowired
    private SpringProfesseurDAO professeurDAO;

    @GetMapping("/rdv")
    @Operation(summary = "USER - retourne tous les rendez-vous enregistrés")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public List<RDV> getListRDV(JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        return rdvDao.findAll();
    }

    @GetMapping("/rdv/{id}")
    @Operation(summary = "USER - retourne le rendez-vous enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun rendez-vous trouvé avec l'id donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public RDV getRDV(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        RDV p = rdvDao.findById(id);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rendez-vous trouvé avec l'id " + id);
        return p;
    }

    @GetMapping("/rdv/professeur/{idProfesseur}")
    @Operation(summary = "USER - retourne tous les rendez-vous enregistrés avec le professeur donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun rendez-vous trouvé avec le professeur donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public List<RDV> getRDVWithProfesseur(@PathVariable("idProfesseur") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        List<RDV> rdvs = rdvDao.findAllWithProfesseur(id);
        if (rdvs.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rdv trouvé pour le professeur avec l'id " + id);
        return rdvs;
    }

    @GetMapping("/rdv/eleve/{idEleve}")
    @Operation(summary = "USER - retourne tous les rendez-vous enregistrés avec l'élève donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "404", description = "si aucun rendez-vous trouvé avec l'élève donné")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la récupération des données")
    public List<RDV> getRDVWithEleve(@PathVariable("idEleve") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("USER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a USER to access this ressource");

        List<RDV> rdvs = rdvDao.findAllWithEleve(id);
        if (rdvs.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rdv trouvé pour l'élève avec l'id " + id);
        return rdvs;
    }

    @PutMapping("/rdv")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "TEACHER - crée ou met un jour le rendez-vous donné")
    @ApiResponse(responseCode = "400", description = "si le rdv donné est null, ou si le professeur ou l'élève est null")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la création des données")
    public void addUpdateRDV(@RequestBody RDV rdv, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("TEACHER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a TEACHER to access this ressource");

        if (rdv == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Le rendez-vous ne peut être null");

        if (rdv.getEleve() == null || rdv.getProfesseur() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'élève ou le professeur est null");

        //tentative de récupération d'un élève existant
        Eleve eleveInitial = rdv.getEleve();
        Eleve eleveRdv = null;
        if (eleveInitial.getId() != 0)
            eleveRdv = eleveDAO.findById(eleveInitial.getId());
        else if (eleveInitial.getEmail() != null && !eleveInitial.getEmail().isBlank())
            eleveRdv = eleveDAO.findByEmail(eleveInitial.getEmail());

        //élève non trouvé en base, création de l'élève
        if (eleveRdv == null)
            eleveRdv = new Eleve(eleveInitial.getNom(), eleveInitial.getPrenom(), eleveInitial.getEmail(), eleveInitial.getFormation());

        if (isNullOrBlank(eleveRdv.getNom()) || isNullOrBlank(eleveRdv.getPrenom()) || isNullOrBlank(eleveRdv.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'élève' n'est pas valide");


        //tentative de récupération d'un professeur existant
        Professeur professeurInitial = rdv.getProfesseur();
        Professeur professeurRdv = null;
        if (professeurInitial.getId() != 0)
            professeurRdv = professeurDAO.findById(professeurInitial.getId());
        else if (professeurInitial.getEmail() != null && !professeurInitial.getEmail().isBlank())
            professeurRdv = professeurDAO.findByEmail(professeurInitial.getEmail());

        //professeur non trouvé en base, création du professeur
        if (professeurRdv == null)
            professeurRdv = new Professeur(professeurInitial.getNom(), professeurInitial.getPrenom(), professeurInitial.getEmail(), professeurInitial.getSpecialite());
        
        if (isNullOrBlank(professeurRdv.getNom()) || isNullOrBlank(professeurRdv.getPrenom()) || isNullOrBlank(professeurRdv.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "le professeur n'est pas valide");


        //assignation des nouvelles valeurs pour élève et professeur
        rdv.setProfesseur(professeurRdv);
        rdv.setEleve(eleveRdv);
        rdv.setDuree(0);
        try
        {
            rdvDao.save(rdv);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "le rendez-vous n'a pas pu être créé");
        }

    }
    
    private boolean isNullOrBlank(String s)
    {
        return s == null || s.isBlank();
    }
    

    @DeleteMapping("/rdv/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "TEACHER - supprime le rendez-vous enregistré avec l'id donné")
    @ApiResponse(responseCode = "401", description = "si token non valide ou privilèges insuffisants")
    @ApiResponse(responseCode = "500", description = "Erreur interne lors de la suppression des données")
    public void deleteRDVById(@PathVariable("id") long id, JwtAuthenticationToken auth)
    {
        if (!((Map<String, Collection<String>>)auth.getToken().getClaim("realm_access")).get("roles").contains("TEACHER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you must be a TEACHER to access this ressource");

        rdvDao.deleteById(id);
    }
}