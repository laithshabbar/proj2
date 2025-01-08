package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	 public static List<User> getUsers() {
	        List<User> users = new ArrayList<>();
	        String sql = "SELECT * FROM users WHERE role = 'user'";

	        try (Connection conn = DBConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {

	            while (rs.next()) {
	                User user = new User(
	                        rs.getInt("user_id"),
	                        rs.getString("username"),
	                        rs.getString("email")
	                );
	                users.add(user);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return users;
	    }
    // Fetch all users with the role 'driver'
    public static List<User> getDrivers() {
        List<User> drivers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'driver'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email")
                );
                drivers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // Add a new driver to the database
    public static boolean addDriver(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'driver')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a driver by user ID
    public static boolean removeDriver(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
