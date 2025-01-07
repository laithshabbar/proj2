package util;

import java.sql.*;
import java.util.*;

public class CityService {

    public List<Map<String, String>> getCities() throws SQLException {
        String sql = "SELECT id, name FROM cities";
        List<Map<String, String>> cities = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
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

    public String getStationsJson(int cityId) throws SQLException {
        String sql = "SELECT id, name FROM stations WHERE city_id = ?";
        List<Map<String, String>> stations = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
        
        StringBuilder jsonResponse = new StringBuilder("[");
        for (int i = 0; i < stations.size(); i++) {
            Map<String, String> station = stations.get(i);
            jsonResponse.append("{\"id\":\"").append(station.get("id")).append("\",")
                    .append("\"name\":\"").append(station.get("name")).append("\"}");
            if (i < stations.size() - 1) {
                jsonResponse.append(",");
            }
        }
        jsonResponse.append("]");
        return jsonResponse.toString();
    }
}
