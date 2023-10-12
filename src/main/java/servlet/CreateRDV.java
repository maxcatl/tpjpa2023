package servlet;

import DAO.DAO_BDD;
import fr.istic.domain.Professeur;
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

@WebServlet(name = "Create a rendez-vous", urlPatterns = "/createRDV")
public class CreateRDV extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        writer.println("<html>\n<body>");

        DAO_BDD bdd = new DAO_BDD();
        List<Eleve> eleves = bdd.listClients();
        List<Professeur> professeurs = bdd.listProfessionals();

        if (eleves == null || eleves.isEmpty())
        {
            writer.println("<p>Erreur : aucun client trouvé</p>");
            writer.println("</body>\n</html>");
            writer.flush();
            return;
        }
        if (professeurs == null || professeurs.isEmpty())
        {
            writer.println("<p>Erreur : aucun professionel trouvé</p>");
            writer.println("</body>\n</html>");
            writer.flush();
            return;
        }

        writer.println("<form method=\"post\" action=\"/createRDV\"");

        //select client
        writer.println("<label for=\"selectClient\">Select a client</label><br/>\n" +
                "<select name=\"selectClient\" id=\"selectClient\">");
        for (Eleve eleve : eleves)
        {
            writer.println("<option value=\"\">" + eleve.getPrenom() + " " + eleve.getNom() + "</options>");
        }
        writer.println("</select><br/>");


        //select professional
        writer.println("<label for=\"selectProfessional\">Select a Professeur</label><br/>\n" +
                "<select name=\"selectProfessional\" id=\"selectProfessional\">");
        for (Professeur professeur : professeurs)
        {
            writer.println("<option value=\"\">" + professeur.getPrenom() + " " + professeur.getNom() + "</options>");
        }
        writer.println("</select><br/>");



        //select date
        writer.println("<label for=\"inputDate\">Select a date for the rendez-vous</label><br/>");
        writer.println("<input type=\"date\" name=\"inputDate\" id=\"inputDate\"/><br/>");

        writer.println("<input type=\"submit\" value=\"Create rendez-vous\"/>\n" +
                "</form>\n</body>\n</html>");

        writer.flush();


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        writer.println("<html>\n<body>");

        String professionalId = request.getParameter("selectProfessional");
        String clientId = request.getParameter("selectClient");
        String dateString = request.getParameter("inputDate");

        if (professionalId == null || professionalId.isBlank())
        {
            writer.println("<h3>No professeur selected</h3>");
            writer.flush();
            return;
        }
        if (clientId == null || clientId.isBlank())
        {
            writer.println("<h3>No eleve selected</h3>");
            writer.flush();
            return;
        }
        if (dateString == null || dateString.isBlank())
        {
            writer.println("<h3>No date selected</h3>");
            writer.flush();
            return;
        }

        DAO_BDD bdd = new DAO_BDD();
        log(dateString);
        Date date = new Date();

        try {
            log(dateString);
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            log(date.toString());
        } catch (ParseException e) {
            writer.println("<h3>The date couldn't be parsed</h3>");
            writer.flush();
            return;
        }


        Professeur professeur = bdd.getProfessional(Long.parseLong(professionalId));
        Eleve eleve = bdd.getClient(Long.parseLong(clientId));

//        bdd.addRDV(new RDV(date, professeur, eleve));

        writer.println("<h2>Rendez-vous created :</h2>\n" +
                "<p>Professeur : " + professeur.getPrenom() + " " + professeur.getNom() + "</p>\n" +
                "<p>Eleve : " + eleve.getPrenom() + " " + eleve.getNom() + "</p>\n" +
                "<p>date : " + date + "</p>");

        writer.println("</body>\n</html>");
    }
}
