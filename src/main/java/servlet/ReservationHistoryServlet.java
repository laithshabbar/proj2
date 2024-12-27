package servlet;

import util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/ReservationHistoryServlet")
public class ReservationHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<ReservationHistory> history = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT r.reservation_id, r.seat_number, r.reservation_time, " +
                "rd.departure_time, " +
                "c.name as city_name, s.name as station_name " +
                "FROM reservations r " +
                "JOIN rides rd ON r.ride_id = rd.ride_id " +
                "JOIN stations s ON rd.station_id = s.id " +
                "JOIN cities c ON s.city_id = c.id " +
                "WHERE r.user_id = ? " +
                "ORDER BY rd.departure_time DESC")) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservationHistory entry = new ReservationHistory(
                        rs.getInt("reservation_id"),
                        rs.getString("city_name"),
                        rs.getString("station_name"),
                        rs.getString("seat_number"),
                        rs.getTimestamp("reservation_time"),
                        rs.getTimestamp("departure_time")
                    );
                    history.add(entry);
                }
            }
            
            request.setAttribute("reservationHistory", history);
            request.getRequestDispatcher("account.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error retrieving reservation history: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String reservationIdStr = request.getParameter("reservationId");
        if (reservationIdStr != null) {
            try (Connection con = DBConnection.getConnection()) {
                int reservationId = Integer.parseInt(reservationIdStr);
                
                // Check if the reservation belongs to the user and if it's upcoming
                String query = "SELECT departure_time FROM reservations r " +
                               "JOIN rides rd ON r.ride_id = rd.ride_id " +
                               "WHERE r.reservation_id = ? AND r.user_id = ?";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, reservationId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getTimestamp("departure_time").after(new Date())) {
                            // Proceed to cancel
                            String cancelQuery = "DELETE FROM reservations WHERE reservation_id = ?";
                            try (PreparedStatement cancelPs = con.prepareStatement(cancelQuery)) {
                                cancelPs.setInt(1, reservationId);
                                cancelPs.executeUpdate();
                            }
                            response.sendRedirect("ReservationHistoryServlet");
                        } else {
                            response.getWriter().write("Cannot cancel this reservation.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("Error canceling reservation: " + e.getMessage());
            }
        }
    }
    
    public static class ReservationHistory {
        private int reservationId;
        private String cityName;
        private String stationName;
        private String seatNumber;
        private java.sql.Timestamp reservationTime;
        private java.sql.Timestamp departureTime;
        
        public ReservationHistory(int reservationId, String cityName, String stationName, 
                                String seatNumber, java.sql.Timestamp reservationTime, 
                                java.sql.Timestamp departureTime) {
            this.reservationId = reservationId;
            this.cityName = cityName;
            this.stationName = stationName;
            this.seatNumber = seatNumber;
            this.reservationTime = reservationTime;
            this.departureTime = departureTime;
        }
        
        // Getters
        public int getReservationId() { return reservationId; }
        public String getCityName() { return cityName; }
        public String getStationName() { return stationName; }
        public String getSeatNumber() { return seatNumber; }
        public java.sql.Timestamp getReservationTime() { return reservationTime; }
        public java.sql.Timestamp getDepartureTime() { return departureTime; }
        
        // Formatted date getters
        public String getFormattedReservationTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a"); // 12-hour format with AM/PM
            return sdf.format(this.reservationTime);
        }
        
        public String getFormattedDepartureTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a"); // 12-hour format with AM/PM
            return sdf.format(this.departureTime);
        }
        
        public boolean isUpcoming() {
            return this.departureTime.after(new Date());
        }
    }
}
