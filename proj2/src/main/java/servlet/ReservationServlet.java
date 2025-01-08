package servlet;

import util.DBConnection;
import util.ReservationDAO;
import java.io.IOException;
import java.sql.Connection;
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
    private ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("user_id");
        String seatNumber = (String) session.getAttribute("seatNumber");
        Integer rideId = (Integer) session.getAttribute("rideId");

       
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
            if (reservationDAO.isUserAlreadyReserved(con, userId, rideId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You have already reserved a seat for this ride.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check if user has a reservation on the same date
            if (reservationDAO.hasReservationForRideDate(con, userId, rideId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You already have a reservation on this date. You cannot reserve another ride for this date.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check if user has exceeded the daily reservation limit
            if (reservationDAO.hasExceededDailyLimit(con, userId)) {
                con.rollback();
                session.setAttribute("errorMessage", "You have reached the maximum limit of 6 reservations for today.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Check seat availability with row-level locking
            if (reservationDAO.isSeatTaken(con, rideId, seatNumber)) {
                con.rollback();
                session.setAttribute("errorMessage", "Sorry, this seat has just been taken. Please choose another seat.");
                response.sendRedirect("confirmation.jsp");
                return;
            }

            // Make the reservation
            if (reservationDAO.makeReservation(con, userId, rideId, seatNumber)) {
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
}
