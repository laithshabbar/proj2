package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("txtName");
        String password = request.getParameter("txtPwd");

        try (Connection con = DBConnection.getConnection();
             //  check if the username exists
             PreparedStatement checkUser = con.prepareStatement("SELECT COUNT(*) as count FROM users WHERE username = ?")) {
            
            checkUser.setString(1, username);
            try (ResultSet userCheck = checkUser.executeQuery()) {
                userCheck.next();
                if (userCheck.getInt("count") == 0) {
                    // Account doesn't exist - redirect with error message
                    String errorMessage = URLEncoder.encode("Account does not exist. Please register first.", StandardCharsets.UTF_8.toString());
                    response.sendRedirect("index.html?error=" + errorMessage);
                    return;
                }
            }

            //  the username exists ,check credentials
            try (PreparedStatement ps = con.prepareStatement("SELECT user_id, username FROM users WHERE username = ? AND password = ?")) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Login successful
                        int userId = rs.getInt("user_id");
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("user_id", userId);

                        // Update the last_login field
                        try (PreparedStatement updatePs = con.prepareStatement("UPDATE users SET last_login = NOW() WHERE user_id = ?")) {
                            updatePs.setInt(1, userId);
                            updatePs.executeUpdate();
                        }

                        response.sendRedirect("CityServlet");
                    } else {
                        // Password is incorrect - redirect with error message
                        String errorMessage = URLEncoder.encode("Incorrect password. Please try again.", StandardCharsets.UTF_8.toString());
                        response.sendRedirect("index.html?error=" + errorMessage);
                    }
                }
            }
        } catch (SQLException e) {
            // Database error - redirect with error message
            String errorMessage = URLEncoder.encode("Database connection error: " + e.getMessage(), StandardCharsets.UTF_8.toString());
            response.sendRedirect("index.html?error=" + errorMessage);
        }
    }
}