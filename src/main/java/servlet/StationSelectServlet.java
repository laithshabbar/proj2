package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/StationSelectServlet")
public class StationSelectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String station = request.getParameter("station");

        if (station != null && !station.isEmpty()) {
            // Store the selected station in the session
            HttpSession session = request.getSession();
            session.setAttribute("selectedStation", station);

            // Redirect to the DateTime page where the user selects the time
            response.sendRedirect("DateTime.html");
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Please select a station.</h3>");
        }
    }
}
