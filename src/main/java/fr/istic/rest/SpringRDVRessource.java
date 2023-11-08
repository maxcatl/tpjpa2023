package fr.istic.rest;

import fr.istic.dao.SpringEleveDAO;
import fr.istic.dao.SpringProfesseurDAO;
import fr.istic.dao.SpringRDVDAO;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public List<RDV> getListRDV()
    {
        return rdvDao.findAll();
    }

    @GetMapping("/rdv/{id}")
    public RDV getRDV(@PathVariable("id") long id)
    {
        RDV p = rdvDao.findById(id);
        if (p == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rendez-vous trouvé avec l'id " + id);
        return p;
    }

    @GetMapping("/rdv/professeur/{idProfesseur}")
    public List<RDV> getRDVWithProfesseur(@PathVariable("idProfesseur") long id)
    {
        List<RDV> rdvs = rdvDao.findAllWithProfesseur(id);
        if (rdvs.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rdv trouvé pour le professeur avec l'id " + id);
        return rdvs;
    }

    @GetMapping("/rdv/eleve/{idEleve}")
    public List<RDV> getRDVWithEleve(@PathVariable("idEleve") long id)
    {
        List<RDV> rdvs = rdvDao.findAllWithEleve(id);
        if (rdvs.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun rdv trouvé pour l'élève avec l'id " + id);
        return rdvs;
    }

    @PutMapping("/rdv")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUpdateRDV(@RequestBody RDV rdv)
    {
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
    public void deleteRDVById(@PathVariable("id") long id)
    {
        rdvDao.deleteById(id);
    }
}
