package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;


public class UserService {

    // Hashes a password before storing it in the database
    private String hashPassword(String password) {
        // Generate a salt and hash the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Validates the user's login by checking the entered password against the stored hashed password
    public int validateLogin(String username, String password) throws SQLException {
        String query = "SELECT user_id, password FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    // If the stored password is not hashed 
                    if (!storedPassword.startsWith("$2a$") && !storedPassword.startsWith("$2b$") && !storedPassword.startsWith("$2y$")) {
                
                        if (password.equals(storedPassword)) {
                            // If password matches, hash it and update in the database
                            String hashedPassword = hashPassword(password);
                            updatePassword(username, hashedPassword);
                            return rs.getInt("user_id");
                        }
                    } else {
                        // Password is already hashed, check using bcrypt
                        if (BCrypt.checkpw(password, storedPassword)) {
                            return rs.getInt("user_id");
                        }
                    }
                }
                return -1; 
            }
        }
    }

    // Update the password in the database after hashing it
    private void updatePassword(String username, String hashedPassword) throws SQLException {
        String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    // Retrieves the user's role from the database
    public String getUserRole(String username) throws SQLException {
        String query = "SELECT role FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                } else {
                    return null; // Role not found
                }
            }
        }
    }

    // Registers a new user by hashing the password before storing it in the database
    public boolean registerUser(String username, String password, String email, String uniId) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, uni_id) VALUES (?, ?, ?, ?)";
        String hashedPassword = hashPassword(password); // Hash the password before storing it
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);  // Store the hashed password
            ps.setString(3, email);
            ps.setString(4, uniId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Checks if a username is already taken
    public boolean isUsernameTaken(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            var rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // Checks if an email is already taken
    public boolean isEmailTaken(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            var rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
