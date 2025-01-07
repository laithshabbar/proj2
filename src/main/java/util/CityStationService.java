package util;

import java.sql.*;
import java.util.*;

public class CityStationService {

    public List<CityDTO> getCitiesWithStations() throws SQLException {
        Map<Integer, CityDTO> citiesMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT c.id as city_id, c.name as city_name, " +
                 "s.id as station_id, s.name as station_name, s.maps_link " +
                 "FROM cities c LEFT JOIN stations s ON c.id = s.city_id " +
                 "ORDER BY c.name, s.name");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int cityId = rs.getInt("city_id");
                CityDTO city = citiesMap.get(cityId);

                if (city == null) {
                    city = new CityDTO(cityId, rs.getString("city_name"));
                    citiesMap.put(cityId, city);
                }

                String stationName = rs.getString("station_name");
                if (stationName != null) {
                    StationDTO station = new StationDTO(
                        rs.getInt("station_id"),
                        stationName,
                        rs.getString("maps_link")
                    );
                    city.getStations().add(station);
                }
            }
        }

        return new ArrayList<>(citiesMap.values());
    }

    public void addCity(String cityName) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO cities (name) VALUES (?)")) {
            pstmt.setString(1, cityName);
            pstmt.executeUpdate();
        }
    }

    public void addStation(int cityId, String stationName, String mapsLink) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO stations (city_id, name, maps_link) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, cityId);
            pstmt.setString(2, stationName);
            pstmt.setString(3, mapsLink);
            pstmt.executeUpdate();
        }
    }
    public void deleteCity(int cityId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM cities WHERE id = ?")) {
            pstmt.setInt(1, cityId);
            pstmt.executeUpdate();
        }
    }

    public void deleteStation(int stationId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM stations WHERE id = ?")) {
            pstmt.setInt(1, stationId);
            pstmt.executeUpdate();
        }
    }
}
