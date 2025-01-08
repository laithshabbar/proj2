package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RideService {

    public List<Map<String, Object>> getDriverRidesWithReservations(int driverId) throws SQLException {
        List<Map<String, Object>> rides = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection()) {
            String ridesQuery = "SELECT r.ride_id, r.departure_time, s.name as station_name, c.name as city_name " +
                    "FROM rides r " +
                    "JOIN stations s ON r.station_id = s.id " +
                    "JOIN cities c ON s.city_id = c.id " +
                    "WHERE r.driver_id = ? AND r.departure_time > NOW() " +
                    "ORDER BY r.departure_time ASC";

            try (PreparedStatement ridesStmt = connection.prepareStatement(ridesQuery)) {
                ridesStmt.setInt(1, driverId);

                try (ResultSet ridesResult = ridesStmt.executeQuery()) {
                    while (ridesResult.next()) {
                        Map<String, Object> ride = new HashMap<>();
                        ride.put("rideId", ridesResult.getInt("ride_id"));
                        ride.put("departureTime", ridesResult.getString("departure_time"));
                        ride.put("stationName", ridesResult.getString("station_name"));
                        ride.put("cityName", ridesResult.getString("city_name"));

                        // Fetch reservations for each ride
                        List<Map<String, String>> reservations = getReservationsForRide(connection, ridesResult.getInt("ride_id"));
                        ride.put("reservations", reservations);

                        rides.add(ride);
                    }
                }
            }
        }

        return rides;
    }

    private List<Map<String, String>> getReservationsForRide(Connection connection, int rideId) throws SQLException {
        List<Map<String, String>> reservations = new ArrayList<>();

        String reservationsQuery = "SELECT u.uni_id, u.username, res.seat_number " +
                "FROM reservations res " +
                "JOIN users u ON res.user_id = u.user_id " +
                "WHERE res.ride_id = ?";

        try (PreparedStatement reservationsStmt = connection.prepareStatement(reservationsQuery)) {
            reservationsStmt.setInt(1, rideId);

            try (ResultSet reservationsResult = reservationsStmt.executeQuery()) {
                while (reservationsResult.next()) {
                    Map<String, String> reservation = new HashMap<>();
                    reservation.put("uniId", reservationsResult.getString("uni_id"));
                    reservation.put("username", reservationsResult.getString("username"));
                    reservation.put("seatNumber", reservationsResult.getString("seat_number"));
                    reservations.add(reservation);
                }
            }
        }

        return reservations;
    }
    // Check if the driver has already added 10 rides on the specified date
    public boolean isRideLimitExceededForDate(Integer driverId, String rideDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rides WHERE driver_id = ? AND DATE(departure_time) = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, driverId);
            preparedStatement.setString(2, rideDate);  

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) >= 10;
                }
            }
        }
        return false;
    }

    // Check if a ride exists for the given city, station, and departure time
    public boolean checkRideExists(String cityId, String stationId, String departureTime) {
        String checkSQL = "SELECT COUNT(*) FROM rides WHERE city_id = ? AND station_id = ? AND departure_time = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkSQL)) {
            preparedStatement.setInt(1, Integer.parseInt(cityId));
            preparedStatement.setInt(2, Integer.parseInt(stationId));
            preparedStatement.setString(3, departureTime);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Insert a new ride into the database
    public boolean insertRide(String cityId, String stationId, String departureTime, Integer userId) throws SQLException {
        String insertRideSQL = "INSERT INTO rides (city_id, station_id, departure_time, driver_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertRideSQL)) {
            preparedStatement.setInt(1, Integer.parseInt(cityId));
            preparedStatement.setInt(2, Integer.parseInt(stationId));
            preparedStatement.setString(3, departureTime);
            preparedStatement.setInt(4, userId);

            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        }
    }
    public static int getRideIdByCityStationDateTime(String city, String station, String departureDateTime) {
        int rideId = -1; 

        String query = "SELECT r.ride_id "
                     + "FROM rides r "
                     + "JOIN cities c ON r.city_id = c.id "
                     + "JOIN stations s ON r.station_id = s.id "
                     + "WHERE c.name = ? AND s.name = ? AND r.departure_time = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, city);
            ps.setString(2, station);
            ps.setString(3, departureDateTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rideId = rs.getInt("ride_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rideId;
    }
    public static Set<String> getReservedSeatsForRide(int rideId) {
        Set<String> reservedSeats = new HashSet<>();
        String query = "SELECT seat_number FROM reservations WHERE ride_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, rideId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservedSeats.add(rs.getString("seat_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservedSeats;
    }
}

