package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StationDTO {
    private int id;
    private String name;
    private String mapsLink;
    
    public StationDTO(int id, String name, String mapsLink) {
        this.id = id;
        this.name = name;
        this.mapsLink = mapsLink;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMapsLink() { return mapsLink; }
    public void setMapsLink(String mapsLink) { this.mapsLink = mapsLink; }
    
    public static ArrayList<HashMap<String, String>> getStationsForCity(String cityName) {
        ArrayList<HashMap<String, String>> stations = new ArrayList<>();
        String query = "SELECT s.name, s.maps_link FROM stations s " +
                       "JOIN cities c ON s.city_id = c.id " +
                       "WHERE c.name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, cityName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, String> station = new HashMap<>();
                    station.put("name", rs.getString("name"));
                    station.put("maps_link", rs.getString("maps_link"));
                    stations.add(station);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stations;
    }
}