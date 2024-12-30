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
    private static final int MAX_DAILY_RESERVATIONS = 6; // Limit for daily reservations

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("user_id");
        String seatNumber = (String) session.getAttribute("seatNumber");
        Integer rideId = (Integer) session.getAttribute("rideId");

        // Log session values for debugging
        System.out.println("Username: " + username);
        System.out.println("User ID: " + userId);
        System.out.println("Seat Number: " + seatNumber);
        System.out.println("Ride ID: " + rideId);

        // Validate data
        if (username == null || userId == null || seatNumber == null || rideId == null) {
            session.setAttribute("errorMessage", "Error: Missing reservation details.");
            response.sendRedirect("confirmation.jsp");
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            // Start transaction and set isolation level
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Check if user already has a reservation for this ride
            if (isUserAlreadyReserved(con, userId, rideId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You have already reserved a seat for this ride.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check if user has a reservation on the same date
            if (hasReservationForRideDate(con, userId, rideId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You already have a reservation on this date. You cannot reserve another ride for this date.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check if user has exceeded the daily reservation limit
            if (hasExceededDailyLimit(con, userId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You have reached the maximum limit of " + MAX_DAILY_RESERVATIONS + " reservations for today.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check seat availability with row-level locking
            if (isSeatTaken(con, rideId, seatNumber)) {
                con.rollback();
                session.setAttribute("errorMessage", "Sorry, this seat has just been taken. Please choose another seat.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Make the reservation
            if (makeReservation(con, userId, rideId, seatNumber)) {
                con.commit();
                session.setAttribute("successMessage", "Reservation completed successfully!");
                response.sendRedirect("confirmation.jsp");
            } else {
                con.rollback();
                session.setAttribute("errorMessage", "Error: Could not complete reservation.");
                response.sendRedirect("confirmation.jsp");
            }

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("confirmation.jsp");
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSeatTaken(Connection con, Integer rideId, String seatNumber) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM reservations WHERE ride_id = ? AND seat_number = ? FOR UPDATE")) {
            ps.setInt(1, rideId);
            ps.setString(2, seatNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean isUserAlreadyReserved(Connection con, Integer userId, Integer rideId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND ride_id = ? FOR UPDATE")) {
            ps.setInt(1, userId);
            ps.setInt(2, rideId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean hasReservationForRideDate(Connection con, Integer userId, Integer rideId) throws SQLException {
        String query = 
            "SELECT COUNT(*) FROM reservations r " +
            "JOIN rides ri ON r.ride_id = ri.ride_id " +
            "WHERE r.user_id = ? " +
            "AND DATE(ri.departure_time) = (SELECT DATE(departure_time) FROM rides WHERE ride_id = ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, rideId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean hasExceededDailyLimit(Connection con, Integer userId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM reservations WHERE user_id = ? " +
                "AND DATE(reservation_time) = CURRENT_DATE FOR UPDATE")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int dailyReservations = rs.getInt(1);
                    return dailyReservations >= MAX_DAILY_RESERVATIONS;
                }
            }
        }
        return false;
    }

    private boolean makeReservation(Connection con, Integer userId, Integer rideId, String seatNumber) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO reservations (user_id, ride_id, seat_number, reservation_time) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setInt(1, userId);
            ps.setInt(2, rideId);
            ps.setString(3, seatNumber);
            return ps.executeUpdate() > 0;
        }
    }
}