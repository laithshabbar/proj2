package servlet;

import util.AvailableTimesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/FetchAvailableTimesServlet")
public class FetchAvailableTimesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AvailableTimesService availableTimesService;

    @Override
    public void init() throws ServletException {
        this.availableTimesService = new AvailableTimesService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String selectedDate = request.getParameter("date");
        String cityName = request.getParameter("city");
        String stationName = request.getParameter("station");

        try {
            // Fetch available times for the given parameters
            List<String> availableTimes = availableTimesService.getAvailableTimes(cityName, stationName, selectedDate);

            out.println("<option value=''>Select Time</option>");
            for (String time : availableTimes) {
                out.printf("<option value='%s'>%s</option>", time, time);
            }

        } catch (Exception e) {
            out.println("<option value=''>Error loading times</option>");
            e.printStackTrace();
        }
    }
}
