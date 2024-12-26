package servlet;

import util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("txtName");
        String password = request.getParameter("txtPwd");

        // Validate inputs
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Username and password cannot be empty.</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("register.html");
            rd.include(request, response);
            return;
        }

        // Use try-with-resources for better resource management
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            ps.setString(1, username);
            ps.setString(2, password); // TODO: Replace with hashed password for security

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                response.setContentType("text/html");
                response.getWriter().println("<h3 style='color:green;'>Registration successful! You can now login.</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.include(request, response);
            } else {
                response.setContentType("text/html");
                response.getWriter().println("<h3 style='color:red;'>Registration failed. Please try again.</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("register.html");
                rd.include(request, response);
            }
        } catch (SQLException e) {
            log("Database connection error", e);
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Database error. Please contact support.</h3>");
        }
    }
}
