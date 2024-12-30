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

@WebServlet("/SaveDateTimeServlet")
public class SaveDateTimeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String departureDate = request.getParameter("departure-date"); // Get date
        String departureTime = request.getParameter("departure-time"); // Get time

        // Make sure both values are retrieved
        if (departureDate != null && !departureDate.isEmpty() && departureTime != null && !departureTime.isEmpty()) {
            // Combine the date and time
            String fullDateTime = departureDate + " " + departureTime;

            // Store the combined date and time in the session
            HttpSession session = request.getSession();
            session.setAttribute("departureDateTime", fullDateTime);

            // Retrieve city and station from the session
            String city = (String) session.getAttribute("selectedCity");
            String station = (String) session.getAttribute("selectedStation");

            // Debugging: Print out session attributes to check if they're set correctly
            System.out.println("City: " + city);
            System.out.println("Station: " + station);
            System.out.println("Departure DateTime: " + fullDateTime);

            // Check if any parameter is missing
            if (city == null || station == null) {
                response.getWriter().write("Error: Missing city or station parameters.");
                return;
            }

            
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT r.ride_id "
                         + "FROM rides r "
                         + "JOIN cities c ON r.city_id = c.id "
                         + "JOIN stations s ON r.station_id = s.id "
                         + "WHERE c.name = ? AND s.name = ? AND r.departure_time = ?")) {

                // Set query parameters
                ps.setString(1, city); // Set the city
                ps.setString(2, station); // Set the station
                ps.setString(3, fullDateTime); // Set the departure time

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int rideId = rs.getInt("ride_id");

                        // Store the ride_id in the session
                        session.setAttribute("rideId", rideId);

                        // Redirect to the seat selection page
                        response.sendRedirect("SeatAvailabilityServlet");
                    } else {
                        response.getWriter().write("No ride found for the selected parameters.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("Database error: " + e.getMessage());
            }
        } else {
            // If the form submission is invalid, redirect back with an error
            response.getWriter().write("Please select both a date and time.");
        }
    }
}
