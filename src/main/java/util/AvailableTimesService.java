package util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AvailableTimesService {

    public List<String> getAvailableTimes(String cityName, String stationName, String selectedDate) throws SQLException {
        List<String> availableTimes = new ArrayList<>();

        String query = "SELECT TIME_FORMAT(TIME(r.departure_time), '%H:%i') as time " +
                "FROM rides r " +
                "JOIN cities c ON r.city_id = c.id " +
                "JOIN stations s ON r.station_id = s.id " +
                "LEFT JOIN reservations res ON r.ride_id = res.ride_id " +
                "WHERE c.name = ? AND s.name = ? " +
                "AND DATE(r.departure_time) = ? " +
                "AND r.departure_time > NOW() " +
                "GROUP BY r.ride_id, r.departure_time " +
                "HAVING COUNT(res.reservation_id) < 32 " +
                "ORDER BY r.departure_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cityName);
            stmt.setString(2, stationName);
            stmt.setString(3, selectedDate);

            // Prepare to format time in 12-hour format
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm"); // 24-hour format
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a"); // 12-hour format with AM/PM

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String time24 = rs.getString("time");

                    // Convert 24-hour time to 12-hour format with AM/PM
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(inputFormat.parse(time24)); // Parse 24-hour time
                        String formattedTime = outputFormat.format(calendar.getTime()); // Format as 12-hour time
                        availableTimes.add(formattedTime);
                    } catch (Exception e) {
                        e.printStackTrace(); // Log error if there's an issue formatting time
                    }
                }
            }
        }

        return availableTimes;
    }
}
