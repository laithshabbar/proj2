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

          
                    response.sendRedirect("StationServlet");
                
            
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Invalid city selection. Please try again.</h3>");
        }
    }
}
