<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Driver Rides</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }
        .banner{
            width: 100%;
            height: 100vh;
            background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
            background-size: cover;
            background-position: center;
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
        .logo{
            width: 360px;
            cursor: pointer;
        }
        body {
            background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
            background-attachment: fixed;  /* Add this line */
            background-size: cover;
            background-position: center;
            min-height: 100vh;
            color: white;
        }

        .content {
            width: 85%;
            margin: auto;
            padding: 35px 0;
        }

        h1 {
            font-size: 60px;
            text-align: center;
            margin-bottom: 40px;
        }

        .ride {
            background-color: rgba(0, 0, 0, 0.5);;
            border: 2px solid #ada0d3;
            border-radius: 25px;
            padding: 25px;
            margin-bottom: 30px;
        }

        h3 {
            font-size: 24px;
            margin-bottom: 15px;
            color: #ada0d3;
        }

        h4 {
            font-size: 20px;
            margin: 20px 0 15px;
            color: #ada0d3;
        }

        p {
            font-size: 18px;
            line-height: 25px;
            margin-bottom: 10px;
        }

        .reservations table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-top: 15px;
        }

        .reservations th {
            background-color: #ada0d3;
            padding: 12px 20px;
            text-align: left;
            font-weight: bold;
        }

        .reservations td {
            padding: 12px 20px;
            background-color: #644ea7;
        }

        .reservations th:first-child {
            border-top-left-radius: 25px;
        }

        .reservations th:last-child {
            border-top-right-radius: 25px;
        }

        .reservations tr:last-child td:first-child {
            border-bottom-left-radius: 25px;
        }

        .reservations tr:last-child td:last-child {
            border-bottom-right-radius: 25px;
        }

        @media (max-width: 1250px) {
            h1 {
                font-size: 48px;
            }
            
            .content {
                width: 90%;
            }
        }

        @media (max-width: 768px) {
            h1 {
                font-size: 36px;
            }
            
            .ride {
                padding: 15px;
            }
            
            .reservations th, 
            .reservations td {
                padding: 8px 12px;
                font-size: 14px;
            }
        }
    </style>
</head>
<body>
<div class="navbar">
            <a href="https://www.just.edu.jo/Pages/Default.aspx">
                <img src="logo.png" class="logo">
            </a>
            <ul>
                    <li><a href="LogoutServlet">Log out</a></li>
            </ul>
        </div>
    <div class="content">
        <h1>UPCOMING RIDES</h1>
        <%
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rides = (List<Map<String, Object>>) request.getAttribute("rides");
            if (rides != null) {
                for (Map<String, Object> ride : rides) {
        %>
            <div class="ride">
                <h3>Ride ID: <%= ride.get("rideId") %></h3>
                <p>Departure Time: <%= ride.get("departureTime") %></p>
                <p>Station: <%= ride.get("stationName") %>, City: <%= ride.get("cityName") %></p>
                <h4>Reservations:</h4>
                <div class="reservations">
                    <table>
                        <thead>
                            <tr>
                                <th>UNI ID</th>
                                <th>Username</th>
                                <th>Seat Number</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                @SuppressWarnings("unchecked")
                                List<Map<String, String>> reservations = (List<Map<String, String>>) ride.get("reservations");
                                if (reservations != null) {
                                    for (Map<String, String> reservation : reservations) {
                            %>
                                <tr>
                                    <td><%= reservation.get("uniId") %></td>
                                    <td><%= reservation.get("username") %></td>
                                    <td><%= reservation.get("seatNumber") %></td>
                                </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        <%
                }
            } else {
        %>
            <div class="ride">
                <p>No upcoming rides available.</p>
            </div>
        <%
            }
        %>
    </div>
</body>
</html>