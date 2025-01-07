package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.UserService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService(); // Initialize the service class
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("txtName");
        String password = request.getParameter("txtPwd");

        try {
            // Check if the username exists and validate credentials
            int userId = userService.validateLogin(username, password);
            if (userId == -1) {
                // Login failed, invalid username or password
                String errorMessage = URLEncoder.encode("Incorrect username or password. Please try again.", StandardCharsets.UTF_8.toString());
                response.sendRedirect("index.html?error=" + errorMessage);
                return;
            }

            // Set session attributes
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("user_id", userId);

            // Fetch the user's role and redirect accordingly
            String role = userService.getUserRole(username);
            if ("driver".equals(role)) {
                response.sendRedirect("ChooseRole.jsp");
            } else if ("admin".equals(role)) {
                response.sendRedirect("Admin.jsp");
            } else {
                response.sendRedirect("CityServlet");
            }

        } catch (Exception e) {
            // Log exception (could be expanded to logging systems) and handle error
            String errorMessage = URLEncoder.encode("An error occurred: " + e.getMessage(), StandardCharsets.UTF_8.toString());
            response.sendRedirect("index.html?error=" + errorMessage);
        }
    }
}
