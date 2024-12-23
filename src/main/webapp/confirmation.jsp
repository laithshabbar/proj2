<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation Page</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="confirmation-container">
        <h1>Booking Confirmation</h1>
        <p><strong>date:</strong> ${sessionScope.departureDateTime}</p>
        <p><strong>Seat:</strong> ${sessionScope.seatNumber}</p>
        <p><strong>City:</strong> ${sessionScope.selectedCity}</p>
        <p><strong>Station:</strong> ${sessionScope.selectedStation}</p>

        <!-- Form to confirm reservation -->
        <form action="ReservationServlet" method="POST">
            
            <input type="hidden" name="departureDate" value="${sessionScope.departureDateTime}">
            <input type="hidden" name="seatNumber" value="${sessionScope.seatNumber}">
            <input type="hidden" name="selectedCity" value="${sessionScope.selectedCity}">
            <input type="hidden" name="selectedStation" value="${sessionScope.selectedStation}">

            <button type="submit">Confirm Reservation</button>
        </form>
    </div>
</body>
</html>
