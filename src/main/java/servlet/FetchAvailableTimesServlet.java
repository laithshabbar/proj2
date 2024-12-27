package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import util.DBConnection;

@WebServlet("/FetchAvailableTimesServlet")
public class FetchAvailableTimesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String selectedDate = request.getParameter("date");
        String cityName = request.getParameter("city");
        String stationName = request.getParameter("station");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT TIME_FORMAT(TIME(r.departure_time), '%H:%i') as time " +
                "FROM rides r " +
                "JOIN cities c ON r.city_id = c.id " +
                "JOIN stations s ON r.station_id = s.id " +
                "LEFT JOIN reservations res ON r.ride_id = res.ride_id " +
                "WHERE c.name = ? AND s.name = ? " +
                "AND DATE(r.departure_time) = ? " +
                "AND r.departure_time > NOW() " +
                "GROUP BY r.ride_id, r.departure_time " +
                "HAVING COUNT(res.reservation_id) < 32 " +
                "ORDER BY r.departure_time")) {
            
            stmt.setString(1, cityName);
            stmt.setString(2, stationName);
            stmt.setString(3, selectedDate);
            
            // Prepare to format time in 12-hour format
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm"); // 24-hour format
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a"); // 12-hour format with AM/PM

            try (ResultSet rs = stmt.executeQuery()) {
                out.println("<option value=''>Select Time</option>");
                while (rs.next()) {
                    String time24 = rs.getString("time");

                    // Convert 24-hour time to 12-hour format with AM/PM
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(inputFormat.parse(time24)); // Parse 24-hour time
                        String formattedTime = outputFormat.format(calendar.getTime()); // Format as 12-hour time

                        // Output the formatted time option
                        out.printf("<option value='%s'>%s</option>", time24, formattedTime);
                    } catch (Exception e) {
                        out.println("<option value=''>Error formatting time</option>");
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            out.println("<option value=''>Error loading times</option>");
            e.printStackTrace();
        }
    }
}
