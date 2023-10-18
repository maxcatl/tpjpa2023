package fr.istic.servlet;

import fr.istic.dao.EleveDao;
import fr.istic.dao.ProfesseurDao;
import fr.istic.dao.RDVDao;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import fr.istic.domain.Eleve;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "Créer un rendez-vous", urlPatterns = "/createRDV")
public class CreateRDV extends HttpServlet
{

    private static final String END_HTML = "</body>\n</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        writer.println("<!DOCTYPE html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Créer un rendez-vous</title>\n</head>\n<html>\n<body>");

        EleveDao eleveDao = new EleveDao();
        List<Eleve> eleves = eleveDao.findAll();


        ProfesseurDao professeurDao = new ProfesseurDao();
        List<Professeur> professeurs = professeurDao.findAll();

        if (eleves == null || eleves.isEmpty())
        {
            writer.println("<p>Erreur : aucun élève trouvé. Veuillez en ajouter un avant de créer un rendez-vous.</p>\n");
            writer.println("<a href=\"index.html\">Retour à la page d'accueil</a>\n" + END_HTML);
            writer.flush();
            return;
        }
        if (professeurs == null || professeurs.isEmpty())
        {
            writer.println("<p>Erreur : aucun professeur trouvé. Veuillez en ajouter un avant de créer un rendez-vous.</p>\n");
            writer.println("<a href=\"index.html\">Retour à la page d'accueil</a>\n" + END_HTML);
            writer.flush();
            return;
        }

        writer.println("<form method=\"post\" action=\"/createRDV\"");

        //select eleve
        writer.println("<label for=\"selectEleve\">Sélectionner un élève</label><br/>\n" +
                "<select name=\"selectEleve\" id=\"selectEleve\">");
        for (Eleve eleve : eleves)
        {
            writer.println("<option value=\"" + eleve.getId() + "\">" + eleve.getPrenom() + " " + eleve.getNom() + "</options>");
        }
        writer.println("</select><br/>");


        //select professeur
        writer.println("<label for=\"selectProfesseur\">Sélectionner un Professeur</label><br/>\n" +
                "<select name=\"selectProfesseur\" id=\"selectProfesseur\">");
        for (Professeur professeur : professeurs)
        {
            writer.println("<option value=\"" + professeur.getId() + "\">" + professeur.getPrenom() + " " + professeur.getNom() + "</options>");
        }
        writer.println("</select><br/>");



        //input dates
        writer.println("<label for=\"inputDateDebut\">Sélectionner une date de début pour le rendez-vous</label><br/>");
        writer.println("<input type=\"datetime-local\" name=\"inputDateDebut\" id=\"inputDateDebut\"/><br/>");

        writer.println("<label for=\"inputDateFin>\">Sélectionner une date de fin pour le rendez-vous</label><br/>");
        writer.println("<input type=\"datetime-local\" name=\"inputDateFin\" id=\"inputDateFin\"/><br/>");

        //input lieu
        writer.println("<label for=\"inputLieu>\">Sélectionner un lieu pour le rendez-vous</label><br/>");
        writer.println("<input type=\"text\" name=\"inputLieu\" id=\"inputLieu\"/><br/>");

        writer.println("<input type=\"submit\" value=\"Créer le rendez-vous\"/>\n" +
                "</form>\n</body>\n</html>");

        writer.flush();


    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        writer.println("<html>\n<body>");

        String idProfesseur = request.getParameter("selectProfesseur");
        String idEleve = request.getParameter("selectEleve");
        String dateDebutString = request.getParameter("inputDateDebut");
        String dateFinString = request.getParameter("inputDateFin");
        String lieu = request.getParameter("inputLieu");

        if (idProfesseur == null || idProfesseur.isBlank())
        {
            writer.println("<h3>Aucun professeur sélectionné</h3>" + END_HTML);
            writer.flush();
            return;
        }
        if (idEleve == null || idEleve.isBlank())
        {
            writer.println("<h3>Aucun élève sélectionné</h3>" + END_HTML);
            writer.flush();
            return;
        }
        if (dateDebutString == null || dateDebutString.isBlank())
        {
            writer.println("<h3>Aucune date de début sélectionnée</h3>" + END_HTML);
            writer.flush();
            return;
        }
        if (dateFinString == null || dateFinString.isBlank())
        {
            writer.println("<h3>Aucune date de fin sélectionnée</h3>" + END_HTML);
            writer.flush();
            return;
        }
        if (lieu == null || lieu.isBlank())
        {
            writer.println("<h3>Aucun lieu sélectionné</h3>" + END_HTML);
            writer.flush();
            return;
        }

        Date dateDebut;
        try {
            dateDebutString = dateDebutString.replace("T", " ");
            log(dateDebutString);
            dateDebut = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateDebutString);
            log(dateDebut.toString());
        } catch (ParseException e) {
            writer.println("<h3>La date de début n'a pas pu être analysée</h3>");
            writer.flush();
            log(e.toString());
            return;
        }

        Date dateFin;
        try {
            dateFinString = dateFinString.replace("T", " ");
            log(dateFinString);
            dateFin = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFinString);
            log(dateFin.toString());
        } catch (ParseException e) {
            writer.println("<h3>La date de fin n'a pas pu être analysée</h3>");
            writer.flush();
            log(e.toString());
            return;
        }


        RDVDao rdvDao = new RDVDao();
        ProfesseurDao professeurDao = new ProfesseurDao();
        EleveDao eleveDao = new EleveDao();

        Professeur professeur = professeurDao.findOne(Long.parseLong(idProfesseur));
        Eleve eleve = eleveDao.findOne(Long.parseLong(idEleve));

        rdvDao.save(new RDV(professeur, eleve, dateDebut, dateFin, lieu));

        writer.println("<h2>Rendez-vous created :</h2>\n" +
                "<p>Professeur : " + professeur.getPrenom() + " " + professeur.getNom() + "</p>\n" +
                "<p>Eleve : " + eleve.getPrenom() + " " + eleve.getNom() + "</p>\n" +
                "<p>Date de début : " + dateDebut + "</p>\n" +
                "<p>Date de fin : " + dateFin + "</p>\n" +
                "<p>Lieu : " + lieu + "</p>" +
                "<a href=\"index.html\">Retour à la page d'accueil</a>\n" + END_HTML);
    }
}
