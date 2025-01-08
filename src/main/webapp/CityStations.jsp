<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="util.CityDTO" %>
<%@ page import="util.StationDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - City and Station Management</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }

        .banner {
            width: 100%;
            min-height: 100vh;
            background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
            background-size: cover;
            background-attachment: fixed; 
            background-position: center;
            padding: 20px 0;
        }
        .navbar{
                width: 85%;
                margin: auto;
                padding: 35px 0;
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
            .navbar ul li{
                list-style: none;
                display: inline-block;
                margin: 0 20px;
                position: relative;
            }
            .navbar ul li a{
                text-decoration: none;
                color: white;
                text-transform: uppercase;
            }

        .logo {
            width: 360px;
            cursor: pointer;
        }

        .content {
            width: 60%;
            margin: 10px auto;
            color: white;
        }

        .content h1 {
            font-size: 40px;
            margin-bottom: 30px;
            text-align: center;
        }

        .section {
            background-color: rgba(0, 0, 0, 0.5);
            padding: 30px;
            margin-bottom: 30px;
            border-radius: 25px;
            border: 2px solid #ada0d3;
        }

        .section h2 {
            font-size: 30px;
            margin-bottom: 20px;
            color: #ada0d3;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-size: 20px;
            margin-bottom: 10px;
            color: white;
        }

        input[type="text"], select {
            width: 350px;
            padding: 10px;
            border: 2px solid #ada0d3;
            border-radius: 15px;
            background-color: rgba(173, 160, 211, 0.2);
            color: white;
            font-size: 18px;
        }

        select option {
            background-color: #644ea7;
            color: white;
        }

        button {
            padding: 15px 30px;
            border: 2px solid #ada0d3;
            border-radius: 25px;
            background: transparent;
            color: white;
            cursor: pointer;
            font-size: 18px;
            position: relative;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        button:before {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 0%;
            height: 100%;
            background-color: #ada0d3;
            transition: all 0.3s;
            border-radius: 25px;
            z-index: -1;
        }

        button:hover {
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(173, 160, 211, 0.4);
        }

        button:hover:before {
            width: 100%;
        }

        .error-message {
            background-color: rgba(255, 107, 107, 0.2);
            color: #ff6b6b;
            padding: 15px;
            margin-bottom: 20px;
            border: 2px solid #ff6b6b;
            border-radius: 25px;
            font-size: 18px;
        }

        .city {
            margin-bottom: 30px;
        }

        .city-header, .station-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .city-name {
            font-size: 24px;
            font-weight: bold;
            color: white;
        }

        .station-name {
            font-size: 18px;
            color: white;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .delete-form {
            margin-left: auto; 
        }
        .delete-form button{
            width:300px; 
        }
        
        .station {
            margin-left: 30px;
            margin-bottom: 10px;
        }

        .maps-link {
            color: #ada0d3;
            text-decoration: none;
        }

        .maps-link:hover {
            text-decoration: underline;
        }

        @media (max-width: 1250px) {
            .content h1 { font-size: 30px; }
            input[type="text"], select { width: 250px; }
            .delete-form button{width:150px; }
        }

        @media (max-width: 768px) {
            .logo { width: 280px; }
            .content h1 { font-size: 24px; }
            .delete-form button{width:100px; }
            input[type="text"], select { width: 200px; }
            .section h2 { font-size: 24px; }
        }
    </style>
</head>
<body>
    <div class="banner">
        <div class="navbar">
            <a href="https://www.just.edu.jo/Pages/Default.aspx">
                <img src="logo.png" class="logo">
            </a>
            <ul>
                    <li><a href="LogoutServlet">Log out</a></li>
            </ul>
        </div>
        
        <div class="content">
            <h1>ADMIN MANAGEMENT PAGE</h1>
            <br>
            <!-- Error Message Display -->
            <% 
            String error = (String)request.getAttribute("error");
            if(error != null && !error.isEmpty()) { 
            %>
                <div class="error-message">
                    <%= error %>
                </div>
            <% } %>

            <!-- Add New City Section -->
            <div class="section">
                <h2>Add New City</h2>
                <form action="" method="post">
                    <input type="hidden" name="action" value="addCity">
                    <div class="form-group">
                        <label for="cityName">City Name:</label>
                        <input type="text" id="cityName" name="cityName" required>
                    </div>
                    <button type="submit">Add City</button>
                </form>
            </div>

            <!-- Add New Station Section -->
            <div class="section">
                <h2>Add New Station</h2>
                <form action="" method="post">
                    <input type="hidden" name="action" value="addStation">
                    <div class="form-group">
                        <label for="cityId">Select City:</label>
                        <select id="cityId" name="cityId" required>
                            <%
                            @SuppressWarnings("unchecked")
                            List<CityDTO> cities = (List<CityDTO>)request.getAttribute("cities");
                            if(cities != null) {
                                for(CityDTO city : cities) {
                            %>
                                <option value="<%=city.getId()%>"><%=city.getName()%></option>
                            <%
                                }
                            }
                            %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="stationName">Station Name:</label>
                        <input type="text" id="stationName" name="stationName" required>
                    </div>
                    <div class="form-group">
                        <label for="mapsLink">Maps Link (optional):</label>
                        <input type="text" id="mapsLink" name="mapsLink">
                    </div>
                    <button type="submit">Add Station</button>
                </form>
            </div>

            <!-- Cities and Stations List -->
            <div class="section">
                <h2>Cities and Stations</h2>
                <%
                if(cities != null) {
                    for(CityDTO city : cities) {
                %>
                <hr width="100%;" color="#644ea7" size="2">
                <br>
                    <div class="city">
                        <div class="city-header">
                            <div class="city-name">
                                <%=city.getName()%>
                            </div>
                            <!-- Delete City Button -->
                            <form action="" method="post" class="delete-form">
                                <input type="hidden" name="action" value="deleteCity">
                                <input type="hidden" name="cityId" value="<%= city.getId() %>">
                                <button type="submit">Delete City</button>
                            </form>
                        </div>
                        <%
                        if(city.getStations().isEmpty()) {
                        %>
                            <div class="station">No stations yet</div>
                        <%
                        } else {
                            for(StationDTO station : city.getStations()) {
                            %>
                                <div class="station">
                                    <div class="station-row">
                                        <div class="station-name">
                                            <%=station.getName()%>
                                            <%
                                            if(station.getMapsLink() != null && !station.getMapsLink().isEmpty()) {
                                            %>
                                                <a href="<%=station.getMapsLink()%>" target="_blank" class="maps-link">(View on Maps)</a>
                                            <%
                                            }
                                            %>
                                        </div>
                                        <!-- Delete Station Button -->
                                        <form action="" method="post" class="delete-form">
                                            <input type="hidden" name="action" value="deleteStation">
                                            <input type="hidden" name="stationId" value="<%= station.getId() %>">
                                            <button type="submit">Delete Station</button>
                                        </form>
                                    </div>
                                </div>
                            <%
                            }
                        }
                        %>
                    </div>
                <%
                    }
                }
                %>
            </div>
        </div>
    </div>
</body>
</html>