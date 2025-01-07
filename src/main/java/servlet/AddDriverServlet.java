package servlet;

import util.User;
import util.UserDAO;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddDriverServlet")
public class AddDriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddDriverServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch all users with 'driver' role
        List<User> drivers = UserDAO.getDrivers();

        // Set the list as a request attribute to display in JSP
        request.setAttribute("drivers", drivers);
        request.getRequestDispatcher("AddUser.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if the request is to add a new user or remove an existing user
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String userIdToRemove = request.getParameter("user_id");

        if (username != null && password != null) {
            // Adding a new user
            boolean success = UserDAO.addDriver(username, password, email);
            if (success) {
                response.sendRedirect("AddDriverServlet"); // Refresh the page to see the updated list
            } else {
                response.getWriter().println("Error adding user.");
            }
        } else if (userIdToRemove != null) {
            // Removing an existing user
            boolean success = UserDAO.removeDriver(Integer.parseInt(userIdToRemove));
            if (success) {
                response.sendRedirect("AddDriverServlet"); // Refresh the page to see the updated list
            } else {
                response.getWriter().println("Error removing user.");
            }
        }
    }
}