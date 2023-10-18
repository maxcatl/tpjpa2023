package fr.istic.servlet;

import fr.istic.dao.EleveDao;
import fr.istic.dao.ProfesseurDao;
import fr.istic.dao.RDVDao;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import fr.istic.domain.RDV;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "get informations", urlPatterns = "/getInformations")
public class GetInformations extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("html/getInfos.html"); PrintWriter out = resp.getWriter();)
        {
            if (stream != null)
            {
                Scanner scanner = new Scanner(stream);
                while(scanner.hasNext())
                {
                    out.println(scanner.next());
                }

                out.flush();
                return;

            }
            out.println("<!DOCTYPE html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Erreur : fichier manquant</title>\n</head>\n<html>\n<body>");
            out.println("<p>Le fichier source \"/html/getInfos.html\" n'a pas pu être trouvé</p>\n");
            out.println("<a href=\"index.html\">Retour à la page d'accueil</a>");
            out.println("</body></html>");

            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");

        try (PrintWriter out = resp.getWriter())
        {
            out.println("<!DOCTYPE html>\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title>Informations de la BDD</title>\n" +
                    "</head>\n" +
                    "<html>\n" +
                    "<body>");
            String endHTML = "<a href=\"/getInformations\">Retour à la page de recherche</a><br/>\n<a href=\"index.html\">Retour à la page d'accueil</a></body></html>";

            String resourceType = req.getParameter("resourceType");
            String resourceQuantity = req.getParameter("resourceQuantity");
            String resourceId = req.getParameter("resourceId");

            if (resourceType == null || resourceType.isBlank())
            {
                out.println("<h2>Erreur : le type de ressource n'a pas été sélectionné</h2>\n");
                out.flush();
                return;
            }
            if (resourceQuantity == null || resourceQuantity.isBlank())
            {
                out.println("<h2>Erreur: la quantité de ressources sélectionnées n'a pas été sélectionnée</h2>\n" + endHTML);
                out.flush();
                return;
            }

            if (resourceQuantity.equals("byId") && (resourceId == null || resourceId.isBlank()))
            {
                out.println("<h2>Erreur : La sélection par id a été demandée mais aucun id n'a été fourni</h2>\n" + endHTML);
                out.flush();
                return;
            }


            if (resourceQuantity.equals("all"))
            {
                switch (resourceType)
                {
                    case "eleve":
                        EleveDao eleveDao = new EleveDao();
                        List<Eleve> eleves = eleveDao.findAll();
                        out.println("<h3>Liste des élèves trouvés (" + eleves.size() + ") :</h3>\n<ul>\n");
                        for (Eleve eleve : eleves)
                            out.println("<li>" + eleve.toString() + "</li>\n");
                        out.println("</ul>\n");
                        break;
                    case "professeur":
                        ProfesseurDao professeurDao = new ProfesseurDao();
                        List<Professeur> professeurs = professeurDao.findAll();
                        out.println("<h3>Liste des rdvs trouvés (" + professeurs.size() + ") :</h3>\n<ul>\n");
                        for (Professeur professeur : professeurs)
                            out.println("<li>" + professeur.toString() + "</li>\n");
                        out.println("</ul>\n");
                        break;
                    case "rdv":
                        RDVDao rdvDao = new RDVDao();
                        List<RDV> rdvs = rdvDao.findAll();
                        out.println("<h3>Liste des rendez-vous trouvés (" + rdvs.size() + ") :</h3>\n<ul>\n");
                        for (RDV rdv : rdvs)
                            out.println("<li>" + rdv.toString() + "</li>\n");
                        out.println("</ul>\n");
                        break;
                    default:
                        out.println("<h2>Erreur : le type de ressource demandé n'est pas valide (" + resourceType + ")</h2>\n" + endHTML);
                        break;
                }
            }
            else if (resourceQuantity.equals("byId"))
            {
                switch (resourceType)
                {
                    case "eleve":
                        EleveDao eleveDao = new EleveDao();
                        Eleve eleve = eleveDao.findOne(Long.parseLong(resourceId));
                        if (eleve != null)
                            out.println("<h3>Élève trouvé : " + eleve + "</h3>\n");
                        else
                            out.println("<h3>Aucun élève ne correspond à l'id " + resourceId + "</h3>");
                        break;
                    case "professeur":
                        ProfesseurDao professeurDao = new ProfesseurDao();
                        Professeur professeur = professeurDao.findOne(Long.parseLong(resourceId));
                        if (professeur != null)
                            out.println("<h3>Professeur trouvé : " + professeur + "</h3>\n");
                        else
                            out.println("<h3>Aucun professeur ne correspond à l'id " + resourceId + "</h3>");
                        break;
                    case "rdv":
                        RDVDao rdvDao = new RDVDao();
                        RDV rdv = rdvDao.findOne(Long.parseLong(resourceId));
                        if (rdv != null)
                            out.println("<h3>Rendez-vous trouvé : " + rdv + "</h3>\n");
                        else
                            out.println("<h3>Aucun rendez-vous ne correspond à l'id " + resourceId + "</h3>");
                        break;
                    default:
                        out.println("<h2>Erreur : le type de ressource demandé n'est pas valide (" + resourceType + ")</h2>\n" + endHTML);
                        break;
                }
            }
            else
            {
                out.println("<h2>Erreur : la quantité de ressource demandée n'est pas disponible");
            }

            out.println(endHTML);
            out.flush();
        }
    }
}
