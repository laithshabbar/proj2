package servlet;

import util.RideService;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/SeatAvailabilityServlet")
public class SeatAvailabilityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer rideId = (Integer) session.getAttribute("rideId");
        
        if (rideId == null) {
            request.setAttribute("errorMessage", "Ride ID is not set.");
            request.getRequestDispatcher("/seatsellection.jsp").forward(request, response);
            return;
        }

        try {
            Set<String> reservedSeats = RideService.getReservedSeatsForRide(rideId);

            request.setAttribute("reservedSeats", reservedSeats);
            request.setAttribute("rideId", rideId);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching seat availability: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/seatsellection.jsp").forward(request, response);
    }
}
