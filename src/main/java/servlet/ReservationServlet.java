package servlet;

import util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Retrieve information from session
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("user_id");
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

        // Check if the user already has a reservation for this ride
        if (isUserAlreadyReserved(userId, rideId)) {
            // If user is already reserved, return an error message
            response.getWriter().write("You have already reserved a seat for this ride.");
            return;
        }

        // Use try-with-resources for better resource management
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO reservations (user_id, ride_id, seat_number) VALUES (?, ?, ?)")) {

            // Set query parameters
            ps.setInt(1, userId); // User ID
            ps.setInt(2, rideId); // Ride ID
            ps.setString(3, seatNumber); // Seat number

            // Execute the query
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Reservation successful, redirect to confirmation page
                response.sendRedirect("confirmation.jsp");
            } else {
                // Reservation failed
                response.getWriter().write("Error: Could not complete reservation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }

    // Method to check if the user has already reserved a seat for the same ride
    private boolean isUserAlreadyReserved(Integer userId, Integer rideId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND ride_id = ?")) {

            ps.setInt(1, userId); // User ID
            ps.setInt(2, rideId); // Ride ID

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Reservation already exists for this user and ride
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
