package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/StationChoiceServlet")
public class StationChoiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");

        if (city != null && !city.isEmpty()) {
            // Store the selected city in the session
            HttpSession session = request.getSession();
            session.setAttribute("selectedCity", city);

            // Redirect to the city's station page
            switch (city) {
                case "Amman":
                    response.sendRedirect("AmmanStations.html");
                    break;
                case "Irbid":
                    response.sendRedirect("IrbidStations.html");
                    break;
                case "Al-Mafraq / Az-Zarqa":
                    response.sendRedirect("Mafraq_ZarqaStations.html");
                    break;
                case "Balqa / Madaba":
                    response.sendRedirect("Balqa_MadabaSatations.html");
                    break;
                case "Al-Karak / Al-Tafilah":
                    response.sendRedirect("Karak_TafilahStations.html");
                    break;
                case "Aqaba / Ma'an":
                    response.sendRedirect("Aqaba_MaanSations.html");
                    break;
                default:
                    response.sendRedirect("error.html"); // Redirect to an error page for unexpected values
            }
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Invalid city selection. Please try again.</h3>");
        }
    }
}
