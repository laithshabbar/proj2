package servlet;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;


import util.DBConnection;



@WebServlet("/FetchAvailableTimesServlet")

public class FetchAvailableTimesServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String selectedDate = request.getParameter("date");
        String cityName = request.getParameter("city");
        String stationName = request.getParameter("station");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT TIME_FORMAT(TIME(departure_time), '%H:%i') as time " +
                "FROM rides r " +
                "JOIN cities c ON r.city_id = c.id " +
                "JOIN stations s ON r.station_id = s.id " +
                "WHERE c.name = ? AND s.name = ? " +
                "AND DATE(departure_time) = ? " +
                "AND departure_time > NOW() " +
                "ORDER BY departure_time")) {
            
            stmt.setString(1, cityName);
            stmt.setString(2, stationName);
            stmt.setString(3, selectedDate);
            
            try (ResultSet rs = stmt.executeQuery()) {
                out.println("<option value=''>Select Time</option>");
                while (rs.next()) {
                    String time = rs.getString("time");
                    out.printf("<option value='%s'>%s</option>", time, time);
                }
            }
        } catch (SQLException e) {
            out.println("<option value=''>Error loading times</option>");
            e.printStackTrace();
        }
    }
}