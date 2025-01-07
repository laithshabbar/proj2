package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {

    private static final int MAX_DAILY_RESERVATIONS = 6; // Limit for daily reservations

    public boolean isSeatTaken(Connection con, Integer rideId, String seatNumber) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM reservations WHERE ride_id = ? AND seat_number = ? FOR UPDATE")) {
            ps.setInt(1, rideId);
            ps.setString(2, seatNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean isUserAlreadyReserved(Connection con, Integer userId, Integer rideId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND ride_id = ? FOR UPDATE")) {
            ps.setInt(1, userId);
            ps.setInt(2, rideId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean hasReservationForRideDate(Connection con, Integer userId, Integer rideId) throws SQLException {
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

    public boolean hasExceededDailyLimit(Connection con, Integer userId) throws SQLException {
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

    public boolean makeReservation(Connection con, Integer userId, Integer rideId, String seatNumber) throws SQLException {
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
