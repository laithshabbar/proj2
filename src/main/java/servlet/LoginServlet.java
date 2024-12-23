package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("txtName");
        String password = request.getParameter("txtPwd");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.83/bus_system", "root", "root");

            // Prepare SQL query
            String query = "SELECT id, username FROM users WHERE username = ? AND password = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Login successful
                int userId = rs.getInt("id"); // Retrieve the user ID from the database
                HttpSession session = request.getSession(); // Create a new session
                session.setAttribute("username", username); // Store username in session
                session.setAttribute("id", userId); // Store user ID in session

                // Redirect to station page or dashboard
                response.sendRedirect("stationpage1.html");
            } else {
                // Login failed
                request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.forward(request, response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Error: JDBC Driver not found.</h3>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red;'>Database connection error: " + e.getMessage() + "</h3>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
