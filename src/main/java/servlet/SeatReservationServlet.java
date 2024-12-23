package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SeatReservationServlet
 */
@WebServlet("/SeatReservationServlet")
public class SeatReservationServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data (seat, date, time)
		HttpSession session = request.getSession();
		String selectedCity = (String) session.getAttribute("selectedCity");
        String seatNumber = request.getParameter("seatNumber");
        String departureTime = (String) session.getAttribute("departureTime");
        String departureDate = (String) session.getAttribute("departureDate");
        String departureStation =(String) session.getAttribute("selectedStation");


        // Store data in session
        session.setAttribute("seatNumber", seatNumber);
        session.setAttribute("departureTime", departureTime);
        session.setAttribute("departureDate", departureDate);
		session.setAttribute("selectedCity", selectedCity);
        session.setAttribute("selectedStation", departureStation);
        
        

        // Redirect to the confirmation page
        response.sendRedirect("confirmation.jsp");
    }
}

