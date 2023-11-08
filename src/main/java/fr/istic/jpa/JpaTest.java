package fr.istic.jpa;


import fr.istic.dao.ProfesseurDao;
import fr.istic.dao.RDVDao;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;

import java.util.Date;

public class JpaTest {

	/**
	 * @param args no argument needed
	 */
	public static void main(String[] args) {
		//2 élèves, 1 professeur, 2 RDVs
		Eleve e = new Eleve("Le Gal", "Maxime", "maxime.le-gal1@etudiant.univ-rennes.fr", "ILA");
		Professeur p = new Professeur("Plouzeau", "Noel", "noel.plouzeau@univ-rennes.fr", "Java");

		RDV r = new RDV(p, e, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 90 * 60 * 1000), "istic");
		RDVDao rdvDao = new RDVDao();
		rdvDao.save(r);


		ProfesseurDao professeurDao = new ProfesseurDao();
		Professeur p1 = professeurDao.findOne(Integer.toUnsignedLong(2));
		Eleve e2 = new Eleve("Huché", "Ilan", "ilan.huche@etudiant.univ-rennes.fr", "ILA");

		rdvDao.save(new RDV(p1, e2, new Date(), new Date(System.currentTimeMillis()+3600000), "ici"));

	}

}