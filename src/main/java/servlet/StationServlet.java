package servlet;

import util.StationDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/StationServlet")
public class StationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String cityName = (String) session.getAttribute("selectedCity");
        
        if (cityName == null) {
            response.sendRedirect("CitySelection.jsp");
            return;
        }

        try {
            // Get stations for the city using the StationQuery class
            ArrayList<HashMap<String, String>> stations = StationDTO.getStationsForCity(cityName);
            
            request.setAttribute("cityName", cityName);
            request.setAttribute("stations", stations);
            request.getRequestDispatcher("Stations.jsp").forward(request, response);
            
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String station = request.getParameter("station");
        if (station != null && !station.isEmpty()) {
            // Store the selected station in the session
            HttpSession session = request.getSession();
            session.setAttribute("selectedStation", station);
            // Redirect to the DateTime page where the user selects the time
            response.sendRedirect("DateTime.jsp");
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Please select a station.</h3>");
        }
    }
}
