package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.DBConnection;

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
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // Get stations for the city using city name
            stmt = conn.prepareStatement(
                "SELECT s.name, s.maps_link FROM stations s " +
                "JOIN cities c ON s.city_id = c.id " +
                "WHERE c.name = ?"
            );
            stmt.setString(1, cityName);
            rs = stmt.executeQuery();
            
            ArrayList<HashMap<String, String>> stations = new ArrayList<>();
            
            while (rs.next()) {
                HashMap<String, String> station = new HashMap<>();
                station.put("name", rs.getString("name"));
                station.put("maps_link", rs.getString("maps_link"));
                stations.add(station);
            }
            
            request.setAttribute("cityName", cityName);
            request.setAttribute("stations", stations);
            request.getRequestDispatcher("Stations.jsp").forward(request, response);
            
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            
            }
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