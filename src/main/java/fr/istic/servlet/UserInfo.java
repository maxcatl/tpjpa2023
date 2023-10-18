package fr.istic.servlet;

import fr.istic.dao.EleveDao;
import fr.istic.dao.ProfesseurDao;
import fr.istic.domain.Eleve;
import fr.istic.domain.Professeur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "userinfo", urlPatterns = "/userInfo")
public class UserInfo extends HttpServlet
{
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String accountType = req.getParameter("AccountType");
        String name = req.getParameter("name");
        String firstname = req.getParameter("firstname");
        String email = req.getParameter("email");
        String specialite = req.getParameter("spécialité");
        String formation = req.getParameter("formation");

        out.println("<!DOCTYPE html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Ajout d'un utilisateur</title>\n" +
                "</head>\n" +
                "<html>\n" +
                "<body>");

        out.println("<h1>Récapitulatif des informations</h1>\n" +
                "<ul>\n" +
                "<li>type de compte : " + accountType + "\n" +
                "<li>Nom : " + name + "\n" +
                "<li>Prenom : " + firstname + "\n" +
                "<li>Adresse mail : " + email + "\n");

        if (accountType.equals("professeur"))
            out.println("<li>spécialité : " + specialite + "\n");
        else if (accountType.equals("eleve"))
            out.println("<li>Formation : " + formation + "\n");

        out.println("</ul>\n");

        final String endHTML = "</body>\n</html>";
        if (accountType.isBlank() || (!accountType.equals("eleve") && !accountType.equals("professeur")))
        {
            out.println("<h3>Le type de compte n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (name == null || name.isBlank())
        {
            out.println("<h3>Le nom n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (firstname == null || firstname.isBlank())
        {
            out.println("<h3>Le prénom n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (email == null || email.isBlank())
        {
            out.println("<h3>L'adresse mail n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (accountType.equals("professeur") && (specialite == null || specialite.isBlank()))
        {
            out.println("<h3>la spécialité n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (accountType.equals("eleve") && (formation == null || formation.isBlank()))
        {
            out.println("<h3>La formation n'est pas valide</h3>\n" + endHTML);
            return;
        }

        if (accountType.equals("professeur"))
        {
            ProfesseurDao professeurDao = new ProfesseurDao();
            if (professeurDao.save(new Professeur(name, firstname, email, specialite)))
                out.println("<h3>Utilisateur ajouté à la base de donnée</h3><br/>\n" +
                        "<a href=\"index.html\">Retour à la page d'accueil</a>\n" + endHTML);
            else
                out.println("<h3>Erreur : L'utilisateur n'a pas pu être ajouté à la base</h3><br/>\n" +
                        "<a href=\"index.html\">Retour à la page d'accueil</a>\n" + endHTML);
        }
        else
        {
            EleveDao eleveDao = new EleveDao();
            if (eleveDao.save(new Eleve(name, firstname, email, formation)))
                out.println("<h3>Utilisateur ajouté à la base de donnée</h3><br/>\n" +
                        "<a href=\"index.html\">Retour à la page d'accueil</a>\n" + endHTML);
            else
                out.println("<h3>Erreur : L'utilisateur n'a pas pu être ajouté à la base</h3><br/>\n" +
                        "<a href=\"index.html\">Retour à la page d'accueil</a>\n" + endHTML);
        }

        out.flush();


    }
}
