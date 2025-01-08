package servlet;

import util.RideService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SaveDateTimeServlet")
public class SaveDateTimeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String departureDate = request.getParameter("departure-date");
        String departureTime = request.getParameter("departure-time");

        if (departureDate != null && !departureDate.isEmpty() && departureTime != null && !departureTime.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
                Date time = inputFormat.parse(departureTime);
                String formattedTime = outputFormat.format(time);

                String fullDateTime = departureDate + " " + formattedTime;

                HttpSession session = request.getSession();
                session.setAttribute("departureDateTime", fullDateTime);

                String city = (String) session.getAttribute("selectedCity");
                String station = (String) session.getAttribute("selectedStation");

                if (city == null || station == null) {
                    response.getWriter().write("Error: Missing city or station parameters.");
                    return;
                }

                int rideId = RideService.getRideIdByCityStationDateTime(city, station, fullDateTime);

                if (rideId != -1) {
                    session.setAttribute("rideId", rideId);
                    response.sendRedirect("SeatAvailabilityServlet");
                } else {
                    response.getWriter().write("No ride found for the selected parameters.");
                }
            } catch (Exception e) {
                response.getWriter().write("Error parsing time: " + e.getMessage());
            }
        } else {
            response.getWriter().write("Please select both a date and time.");
        }
    }
}
