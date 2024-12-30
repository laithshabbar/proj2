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
}


