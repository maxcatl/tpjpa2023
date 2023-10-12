package servlet;

import DAO.DAO_BDD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "userinfo", urlPatterns = "/UserInfo")
public class UserInfo extends HttpServlet
{
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html>\n<body>\n" +
                "<h1>Récapitulatif des informations</h1>\n" +
                "<ul>\n" +
                "<li>type de compte : " + req.getParameter("AccountType") + "\n" +
                "<li>Nom : " + req.getParameter("name") + "\n" +
                "<li>Prenom : " + req.getParameter("firstname") + "\n" +
                "</ul>\n");

        String accountType = req.getParameter("AccountType");
//        out.println("<h1>" + accountType + "</h1>")
        if (accountType == null || accountType.isBlank() || (!accountType.equals("client") && !accountType.equals("professional")))
        {
            out.println("<h3>Le type de compte n'est pas valide</h3>\n" +
                    "</body>\n</html>");
            return;
        }

        String name = req.getParameter("name");
        String firstname = req.getParameter("firstname");
        if (name == null || name.isBlank() || firstname == null || firstname.isBlank())
        {
            out.println("<h3>Le nom ou le prénom n'est pas valide</h3>\n" +
                    "</body>\n</html>");
            return;
        }


//        DAO_BDD bdd = new DAO_BDD();
//        if (accountType.equals("professional"))
//            bdd.addPro(new Professeur(name, firstname));
//        else
//            bdd.addClient(new Eleve(name, firstname));

        out.println("<h3>Utilisateur    ajouté à la base de donnée</h3><br/>\n" +
                "<a href=\"index.html\">Retour à la page d'accueil</a>\n" +
                "</body>\n</html>");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter out = resp.getWriter();
        DAO_BDD bdd = new DAO_BDD();
        out.println(bdd.listUsers());
    }
}
