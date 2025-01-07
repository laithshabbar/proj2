package servlet;

import util.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.RideService;

@WebServlet("/InsertRideServlet")
public class InsertRideServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cityId = request.getParameter("city_id");
        if (cityId != null) {
            try (Connection connection = DBConnection.getConnection()) {
                List<Map<String, String>> stations = fetchStationsByCity(connection, Integer.parseInt(cityId));

                StringBuilder jsonResponse = new StringBuilder();
                jsonResponse.append("[");

                for (int i = 0; i < stations.size(); i++) {
                    Map<String, String> station = stations.get(i);
                    jsonResponse.append("{");
                    jsonResponse.append("\"id\":\"").append(station.get("id")).append("\",");
                    jsonResponse.append("\"name\":\"").append(station.get("name")).append("\"");
                    jsonResponse.append("}");
                    if (i < stations.size() - 1) {
                        jsonResponse.append(",");
                    }
                }

                jsonResponse.append("]");

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
            }
        } else {
            loadCities(request);
            request.getRequestDispatcher("insert-ride.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cityId = request.getParameter("city_id");
        String stationId = request.getParameter("station_id");
        String departureTime = request.getParameter("departure_time");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            request.setAttribute("message", "User is not logged in.");
            loadCities(request);
            request.getRequestDispatcher("insert-ride.jsp").forward(request, response);
            return;
        }

        // Extract the date part of the departureTime (assuming it's in a format like "yyyy-MM-dd HH:mm:ss")
        String rideDate = departureTime.split(" ")[0];

        RideService rideService = new RideService(); // Ensure the service is initialized

        try {
            // Check if the driver has already added 10 rides for this specific date
            if (rideService.isRideLimitExceededForDate(userId, rideDate)) {
                request.setAttribute("message", "You can only add 10 rides on this date.");
                loadCities(request);
                request.getRequestDispatcher("insert-ride.jsp").forward(request, response);
                return;
            }

            // Check if the ride already exists
            boolean rideExists = rideService.checkRideExists(cityId, stationId, departureTime);
            if (rideExists) {
                request.setAttribute("message", "A ride is already scheduled for this time and station.");
                loadCities(request);
                request.getRequestDispatcher("insert-ride.jsp").forward(request, response);
                return;
            }

            // Insert the new ride
            if (rideService.insertRide(cityId, stationId, departureTime, userId)) {
                request.setAttribute("message", "Ride successfully inserted!");
            } else {
                request.setAttribute("message", "Failed to insert the ride.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
        }

        // Reload cities and forward back to the JSP
        loadCities(request);
        request.getRequestDispatcher("insert-ride.jsp").forward(request, response);
    }

    // Fetch cities from the database
    private List<Map<String, String>> fetchCities(Connection connection) throws SQLException {
        String sql = "SELECT id, name FROM cities";
        List<Map<String, String>> cities = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Map<String, String> city = new HashMap<>();
                city.put("id", String.valueOf(resultSet.getInt("id")));
                city.put("name", resultSet.getString("name"));
                cities.add(city);
            }
        }
        return cities;
    }

    // Fetch stations for a given city
    private List<Map<String, String>> fetchStationsByCity(Connection connection, int cityId) throws SQLException {
        String sql = "SELECT id, name FROM stations WHERE city_id = ?";
        List<Map<String, String>> stations = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> station = new HashMap<>();
                    station.put("id", String.valueOf(resultSet.getInt("id")));
                    station.put("name", resultSet.getString("name"));
                    stations.add(station);
                }
            }
        }
        return stations;
    }

    // Load cities into the request attribute for use in the JSP
    private void loadCities(HttpServletRequest request) {
        try (Connection connection = DBConnection.getConnection()) {
            request.setAttribute("cities", fetchCities(connection));
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
        }
    }
}
