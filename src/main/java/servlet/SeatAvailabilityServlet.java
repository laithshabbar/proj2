package servlet;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/SeatAvailabilityServlet")
public class SeatAvailabilityServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer rideId = (Integer) session.getAttribute("rideId");
        
        if (rideId == null) {
            request.setAttribute("errorMessage", "Ride ID is not set.");
            request.getRequestDispatcher("/seatsellection.jsp").forward(request, response);
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Set<String> reservedSeats = new HashSet<String>();

        try {
            connection = util.DBConnection.getConnection();
            String sql = "SELECT seat_number FROM reservations WHERE ride_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rideId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reservedSeats.add(rs.getString("seat_number"));
            }
            
            request.setAttribute("reservedSeats", reservedSeats);
            request.setAttribute("rideId", rideId);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching seat availability: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        request.getRequestDispatcher("/seatsellection.jsp").forward(request, response);
    }
}