package fr.istic.rest;

import fr.istic.dao.SpringRDVDAO;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@RestController
public class ApplicationRessource
{
    @Autowired
    SpringRDVDAO rdvDao;

    @PutMapping("/loadSampleData")
    @ResponseStatus(HttpStatus.CREATED)
    public void loadSampleData()
    {
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
