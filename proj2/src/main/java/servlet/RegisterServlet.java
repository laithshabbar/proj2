package servlet;

import util.UserService;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();  // Initialize UserService
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String uniId = request.getParameter("uni_id");

        // Validate inputs
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty() || 
            uniId == null || uniId.trim().isEmpty()) {
            
            String errorMessage = "All fields are required.";
            response.sendRedirect("register.html?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            return;
        }

        try {
            if (userService.isUsernameTaken(username)) {
                String errorMessage = "Username is already in use. Please try a different one.";
                response.sendRedirect("register.html?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
                return;
            }

            if (userService.isEmailTaken(email)) {
                String errorMessage = "Email is already in use. Please try a different one.";
                response.sendRedirect("register.html?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
                return;
            }

            // Hash the password before registering the user
            boolean isRegistered = userService.registerUser(username, password, email, uniId);
            
            if (isRegistered) {
                response.sendRedirect("index.html");  // Redirect to login page
            } else {
                String errorMessage = "Registration failed. Please try again.";
                response.sendRedirect("register.html?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            String errorMessage = "An error occurred during registration. Please try again later.";
            response.sendRedirect("register.html?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
        }
    }
}
