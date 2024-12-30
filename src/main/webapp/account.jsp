<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="servlet.ReservationHistoryServlet.ReservationHistory" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservation History</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }
.navbar{
    width: 85%;
    margin: auto;
    padding: 35px 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.logo{
    width: 360px;
    cursor: pointer;
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
        body {
            background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(background.jpg);
            background-size: cover;
            background-position: center;
            background-attachment: fixed;  /* This keeps the background fixed */
            min-height: 100vh;
            color: white;
        }

        .history-container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 35px 20px;
        }

        h1 {
            font-size: 40px;
            text-align: center;
            margin-bottom: 30px;
            color: white;
        }
        
        .history-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: rgba(0, 0, 0, 0.5);
            border-radius: 25px;
            overflow: hidden;
        }

        .history-table th, .history-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid rgba(173, 160, 211, 0.3);
        }

        .history-table th {
            background-color: rgba(173, 160, 211, 0.2);
            font-weight: bold;
            color: #ada0d3;
            text-transform: uppercase;
        }

        .history-table tr:hover {
            background-color: rgba(173, 160, 211, 0.1);
        }

        .no-reservations {
            text-align: center;
            padding: 40px;
            color: #ada0d3;
            font-size: 20px;
            background: rgba(0, 0, 0, 0.5);
            border-radius: 25px;
            margin-top: 20px;
        }

        .status-badge {
            padding: 8px 16px;
            border-radius: 25px;
            font-size: 0.9em;
            display: inline-block;
        }

        .status-upcoming {
            background-color: rgba(173, 160, 211, 0.2);
            color: #ada0d3;
        }

        .status-completed {
            background-color: rgba(100, 78, 167, 0.2);
            color: #644ea7;
        }

        .location-cell {
            font-weight: 500;
            color: white;
        }

        .station-name {
            color: #ada0d3;
            font-size: 0.9em;
            margin-top: 5px;
        }

        .cancel-btn {
            background: transparent;
            color: white;
            border: 2px solid #ada0d3;
            padding: 8px 16px;
            cursor: pointer;
            font-size: 0.9em;
            border-radius: 25px;
            transition: 0.5s;
            position: relative;
            overflow: hidden;
        }

        .cancel-btn:hover {
            background-color: #ada0d3;
        }

        @media (max-width: 768px) {
            .history-container {
                padding: 20px 10px;
            }

            h1 {
                font-size: 24px;
            }

            .history-table th, .history-table td {
                padding: 10px;
                font-size: 14px;
            }

            .status-badge {
                padding: 4px 8px;
                font-size: 0.8em;
            }
        }
    </style>
    <script>
        function confirmCancel(form) {
            var confirmation = confirm("Are you sure you want to cancel this reservation?");
            if (confirmation) {
                form.submit();
            }
        }
    </script>
</head>
<body>
<div class="banner">
            <div class="navbar">
                <a href="https://www.just.edu.jo/Pages/Default.aspx">
                    <img src="logo.png" class="logo">
                </a>
                <ul>
                    <li><a href="#">Home</a></li>
                <li><a href="ReservationHistoryServlet">History</a></li>
                <li><a href="AboutPage.html">Stations</a></li>
                </ul>
            </div>
        </div>
    <div class="history-container">
        <h1>YOUR RESERVATION HISTORY</h1>
         
        <%
        @SuppressWarnings("unchecked")
        List<ReservationHistory> history = (List<ReservationHistory>)request.getAttribute("reservationHistory");
        if (history == null || history.isEmpty()) {
        %>
            <div class="no-reservations">
                <p>You haven't made any reservations yet.</p>
            </div>
        <%
        } else {
        %>
            <table class="history-table">
                <thead>
                    <tr>
                        <th>Location</th>
                        <th>Seat Number</th>
                        <th>Departure Time</th>
                        <th>Reservation Made</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    for (ReservationHistory reservation : history) {
                    %>
                        <tr>
                            <td class="location-cell">
                                <%= reservation.getCityName() %>
                                <div class="station-name"><%= reservation.getStationName() %></div>
                            </td>
                            <td><%= reservation.getSeatNumber() %></td>
                            <td><%= reservation.getFormattedDepartureTime() %></td>
                            <td><%= reservation.getFormattedReservationTime() %></td>
                            <td>
                                <% if (reservation.isUpcoming()) { %>
                                    <span class="status-badge status-upcoming">Upcoming</span>
                                <% } else { %>
                                    <span class="status-badge status-completed">Completed</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (reservation.isUpcoming()) { %>
                                    <form action="ReservationHistoryServlet" method="post" id="cancelForm<%= reservation.getReservationId() %>">
                                        <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
                                        <input type="button" class="cancel-btn" value="Cancel" 
                                               onclick="confirmCancel(document.getElementById('cancelForm<%= reservation.getReservationId() %>'))">
                                    </form>
                                <% } %>
                            </td>
                        </tr>
                    <%
                    }
                    %>
                </tbody>
            </table>
        <%
        }
        %>
    </div>
</body>
</html>