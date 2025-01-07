package servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import util.CityStationService;
import util.CityDTO;

import java.util.List;

@WebServlet("/CityStationServlet")
public class CityStationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CityStationService cityStationService;

    @Override
    public void init() throws ServletException {
        cityStationService = new CityStationService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<CityDTO> cities = cityStationService.getCitiesWithStations();
            request.setAttribute("cities", cities);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading cities and stations: " + e.getMessage());
        }

        request.getRequestDispatcher("CityStations.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String errorMessage = null;

        try {
            if ("addCity".equals(action)) {
                String cityName = request.getParameter("cityName");
                cityStationService.addCity(cityName);
            } else if ("addStation".equals(action)) {
                int cityId = Integer.parseInt(request.getParameter("cityId"));
                String stationName = request.getParameter("stationName");
                String mapsLink = request.getParameter("mapsLink");
                cityStationService.addStation(cityId, stationName, mapsLink);
            } else if ("deleteCity".equals(action)) {
                int cityId = Integer.parseInt(request.getParameter("cityId"));
                cityStationService.deleteCity(cityId);
            } else if ("deleteStation".equals(action)) {
                int stationId = Integer.parseInt(request.getParameter("stationId"));
                cityStationService.deleteStation(stationId);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
        }

        doGet(request, response);
    }
}
