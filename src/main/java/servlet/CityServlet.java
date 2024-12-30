package servlet;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/CityServlet")
public class CityServlet extends HttpServlet {

   
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> cities = DBConnection.getCities();  // Retrieve cities from DB
            request.setAttribute("cities", cities);  // Set the cities attribute in the request

            // Forward the request to the JSP page
            request.getRequestDispatcher("/ChooseCity.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching cities");
        }
    }
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String city = request.getParameter("city");

	        if (city != null && !city.isEmpty()) {
	            // Store the selected city in the session
	            HttpSession session = request.getSession();
	            session.setAttribute("selectedCity", city);

	          
	                    response.sendRedirect("StationServlet");
	                
	            
	        } else {
	            response.setContentType("text/html");
	            response.getWriter().println("<h3 style='color:red;'>Invalid city selection. Please try again.</h3>");
	        }
	    }
}


