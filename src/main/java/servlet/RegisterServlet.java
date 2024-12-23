package servlet;

import java.io.IOException;
import java.sql.*;

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

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Username and password cannot be empty.</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("register.html");
            rd.include(request, response);
            return;
        }

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Use try-with-resources to manage resources
            try (Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.83/bus_system", "root", "root");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

                ps.setString(1, username);
                ps.setString(2, password); // TODO: Replace with hashed password

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
            }
        } catch (ClassNotFoundException e) {
            log("JDBC Driver not found", e);
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Error: JDBC Driver not found.</h3>");
        } catch (SQLException e) {
            log("Database connection error", e);
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Database error. Please contact support.</h3>");
        }
    }
}
