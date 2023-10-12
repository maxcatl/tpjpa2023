package fr.istic.jpa;


import fr.istic.dao.EleveDao;
import fr.istic.dao.ProfesseurDao;
import fr.istic.dao.RDVDao;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import jakarta.persistence.RollbackException;

import java.util.Date;
import java.util.logging.Logger;

public class JpaTest {
	private static final Logger logger = Logger.getLogger("JPATest");

	/**
	 * @param args no argument needed
	 */
	public static void main(String[] args) {
		//Ajout d'un rdv avec un élève et un professeur
		Eleve e = new Eleve("Le Gal", "Maxime", "maxime.le-gal@etudiant.univ-rennes.fr", "ILA");
		Professeur p = new Professeur("Plouzeau", "Noel", "noel.plouzeau@univ-rennes.fr", "Java");

		RDV r = new RDV(p, e, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 90 * 60 * 1000), "istic");
		RDVDao rdvDao = new RDVDao();
		rdvDao.save(r);


		//récupération de l'élève
		EleveDao eleveDao = new EleveDao();
		Eleve e1 = eleveDao.findOne(Integer.toUnsignedLong(1));
		logger.info(e1::toString);
		logger.info(()->e1.getRdvs().toString());

		//récupération du professeur
		ProfesseurDao professeurDao = new ProfesseurDao();
		Professeur p1 = professeurDao.findOne(Integer.toUnsignedLong(2));
		logger.info(p1::toString);
		logger.info(()->p1.getRdvs().toString());


		//tentative de sauvegarde d'un élève avec un email déjà utilisé
		if (eleveDao.save(new Eleve("Maxime", "Le Gal", "maxime.le-gal@etudiant.univ-rennes.fr", "IL")))
			logger.info("L'entité existe déjà");


		//tentative de modification d'un email en mettant un email déjà utilisé
		Eleve e2 = new Eleve("Huché", "Ilan", "ilan.huche@etudiant.univ-rennes.fr", "ILA");
		eleveDao.save(e2);
		try
		{
			eleveDao.update((Eleve) e1.setEmail("ilan.huche@etudiant.univ-rennes.fr"));
		}
		catch (RollbackException err)
		{
			logger.info("un élève avec une même adresse mail existe déjà");
		}

		//modification d'un email valide
		eleveDao.update((Eleve) e1.setEmail("maxime.le-gal1@etudiant.univ-rennes.fr"));


		//changement d'élève dans un rdv
		rdvDao.update(r.setEleve(e2));

		//suppression d'un élève utilisé dans un rdv
		eleveDao.deleteById(Integer.toUnsignedLong(4));

		//suppressions
		eleveDao.deleteById(Integer.toUnsignedLong(1));
		rdvDao.deleteById(Integer.toUnsignedLong(1));
		professeurDao.deleteById(Integer.toUnsignedLong(2));
	}

}
