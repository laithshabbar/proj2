package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://database-1.ctko6w88sr3f.eu-north-1.rds.amazonaws.com/bus_system";
    private static final String DB_USER = "laith";
    private static final String DB_PASSWORD = "Laith2002";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public static List<String> getCities() throws SQLException {
        List<String> cities = new ArrayList<>();
        String query = "SELECT name FROM cities";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
        }

        return cities;
    }
}
