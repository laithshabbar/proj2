package servlet;


import util.RideService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/DriverRidesServlet")
public class DriverRidesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private RideService rideService;

    @Override
    public void init() throws ServletException {
        
        this.rideService = new RideService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer driverId = (Integer) session.getAttribute("user_id");

        if (driverId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session");
            return;
        }

        try {
            // Fetch rides and reservations for the driver
            List<Map<String, Object>> rides = rideService.getDriverRidesWithReservations(driverId);

            // Set rides in request scope to be displayed on the JSP
            request.setAttribute("rides", rides);
            request.getRequestDispatcher("driveRides.jsp").forward(request, response);

        } catch (Exception e) {
           
            System.err.println("Error fetching driver rides: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching rides: " + e.getMessage());
        }
    }
}
