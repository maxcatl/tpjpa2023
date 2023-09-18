package servlet;

import DAO.DAO_BDD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpa.Client;
import jpa.Professional;
import jpa.RDV;
import jpa.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        List<Client> clients = bdd.listClients();
        List<Professional> professionals = bdd.listProfessionals();

        if (clients == null || clients.isEmpty())
        {
            writer.println("<p>Erreur : aucun client trouvé</p>");
            writer.println("</body>\n</html>");
            writer.flush();
            return;
        }
        if (professionals == null || professionals.isEmpty())
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
        for (Client client : clients)
        {
            writer.println("<option value=\"" + client.getId() + "\">" + client.getFirstname() + " " + client.getName() + "</options>");
        }
        writer.println("</select><br/>");


        //select professional
        writer.println("<label for=\"selectProfessional\">Select a Professional</label><br/>\n" +
                "<select name=\"selectProfessional\" id=\"selectProfessional\">");
        for (Professional professional : professionals)
        {
            writer.println("<option value=\"" + professional.getId() + "\">" + professional.getFirstname() + " " + professional.getName() + "</options>");
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
            writer.println("<h3>No professional selected</h3>");
            writer.flush();
            return;
        }
        if (clientId == null || clientId.isBlank())
        {
            writer.println("<h3>No client selected</h3>");
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


        Professional professional = bdd.getProfessional(Long.parseLong(professionalId));
        Client client = bdd.getClient(Long.parseLong(clientId));

        bdd.addRDV(new RDV(date, professional, client));

        writer.println("<h2>Rendez-vous created :</h2>\n" +
                "<p>Professional : " + professional.getFirstname() + " " + professional.getName() + "</p>\n" +
                "<p>Client : " + client.getFirstname() + " " + client.getName() + "</p>\n" +
                "<p>date : " + date + "</p>");

        writer.println("</body>\n</html>");
    }
}
