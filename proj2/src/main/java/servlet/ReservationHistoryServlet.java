package servlet;

import util.ReservationService;
import util.ReservationHistory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ReservationHistoryServlet")
public class ReservationHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            List<ReservationHistory> history = ReservationService.getReservationHistory(userId);
            request.setAttribute("reservationHistory", history);
            request.getRequestDispatcher("account.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error retrieving reservation history: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String reservationIdStr = request.getParameter("reservationId");
        if (reservationIdStr != null) {
            try {
                int reservationId = Integer.parseInt(reservationIdStr);
                boolean canceled = ReservationService.cancelReservation(userId, reservationId);
                if (canceled) {
                    response.sendRedirect("ReservationHistoryServlet");
                } else {
                    response.getWriter().write("Cannot cancel this reservation.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("Error canceling reservation: " + e.getMessage());
            }
        }
    }
}
