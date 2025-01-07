package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    // Fetch reservation history for a user
    public static List<ReservationHistory> getReservationHistory(int userId) throws SQLException {
        List<ReservationHistory> history = new ArrayList<>();
        
        String query = "SELECT r.reservation_id, r.seat_number, r.reservation_time, " +
                       "rd.departure_time, " +
                       "c.name as city_name, s.name as station_name " +
                       "FROM reservations r " +
                       "JOIN rides rd ON r.ride_id = rd.ride_id " +
                       "JOIN stations s ON rd.station_id = s.id " +
                       "JOIN cities c ON s.city_id = c.id " +
                       "WHERE r.user_id = ? " +
                       "ORDER BY rd.departure_time DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
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
        }
        
        return history;
    }

    // Cancel a reservation if it's upcoming
    public static boolean cancelReservation(int userId, int reservationId) throws SQLException {
        String query = "SELECT departure_time FROM reservations r " +
                       "JOIN rides rd ON r.ride_id = rd.ride_id " +
                       "WHERE r.reservation_id = ? AND r.user_id = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, reservationId);
            ps.setInt(2, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getTimestamp("departure_time").after(new java.util.Date())) {
                    // Proceed to cancel
                    String cancelQuery = "DELETE FROM reservations WHERE reservation_id = ?";
                    try (PreparedStatement cancelPs = con.prepareStatement(cancelQuery)) {
                        cancelPs.setInt(1, reservationId);
                        cancelPs.executeUpdate();
                        return true; // Successfully canceled
                    }
                }
            }
        }
        
        return false; // Reservation cannot be canceled
    }
}
