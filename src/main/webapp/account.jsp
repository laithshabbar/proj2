<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="servlet.ReservationHistoryServlet.ReservationHistory" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservation History</title>
    <style>
        .history-container {
            max-width: 1000px;
            margin: 20px auto;
            padding: 20px;
        }
        .history-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .history-table th, .history-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .history-table th {
            background-color: #f5f5f5;
            font-weight: bold;
        }
        .history-table tr:hover {
            background-color: #f9f9f9;
        }
        .no-reservations {
            text-align: center;
            padding: 20px;
            color: #666;
        }
        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.9em;
        }
        .status-upcoming {
            background-color: #e3f2fd;
            color: #1976d2;
        }
        .status-completed {
            background-color: #e8f5e9;
            color: #388e3c;
        }
        .location-cell {
            font-weight: 500;
        }
        .station-name {
            color: #666;
            font-size: 0.9em;
        }
        .cancel-btn {
            background-color: #d32f2f;
            color: white;
            border: none;
            padding: 8px 16px;
            cursor: pointer;
            font-size: 0.9em;
            border-radius: 4px;
        }
        .cancel-btn:hover {
            background-color: #c62828;
        }
    </style>
    <script>
        function confirmCancel(form) {
            var confirmation = confirm("Are you sure you want to cancel this reservation?");
            if (confirmation) {
                form.submit();  // Submit the form if confirmed
            }
        }
    </script>
</head>
<body>
    <div class="history-container">
        <h1>Your Reservation History</h1>
         
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
