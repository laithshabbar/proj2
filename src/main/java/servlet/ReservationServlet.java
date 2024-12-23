package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Retrieve information from session
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("id"); 
        String seatNumber = (String) session.getAttribute("seatNumber");
        Integer rideId = (Integer) session.getAttribute("rideId");

        // Log session values for debugging
        System.out.println("Username: " + username);
        System.out.println("User ID: " + userId);
        System.out.println("Seat Number: " + seatNumber);
        System.out.println("Ride ID: " + rideId);

        // Validate data (ensure all necessary data is available)
        if (username == null || userId == null || seatNumber == null || rideId == null) {
            response.getWriter().write("Error: Missing reservation details.");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.83/bus_system", "root", "root");

            // SQL query to insert reservation data into the database
            String query = "INSERT INTO reservations (user_id, ride_id, seat_number) "
                         + "VALUES (?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, userId); // User ID
            ps.setInt(2, rideId); // Ride ID
            ps.setString(3, seatNumber); // Seat number

            // Execute the query
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Registration successful, show confirmation page
                response.sendRedirect("confirmation.jsp");
            } else {
                // Registration failed
                response.getWriter().write("Error: Could not complete reservation.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write("Error: JDBC Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Database error: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
