<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation Page</title>
    <link rel="stylesheet" href="style.css">
</head>
<style>
body {
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
    background-color: #282c34;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    color: white;
}

.banner {
    text-align: center;
}

h1 {
    font-size: 3rem;
    color: white;
    margin-bottom: 1rem;
}

p {
    font-size: 1.5rem;
    color: white;
    margin: 0.5rem 0;
}

.confirmation-container {
    margin-top: 2%;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.6);
    padding: 4rem;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

button {
    width: 25%;
    padding: 40px 0;
    text-align: center;
    border-radius: 25px;
    margin: 2%;
    font-weight: bold;
    border: 2px solid #ada0d3;
    background: transparent;
    color: white;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    font-size: 25px;
    margin-top: 1%;
    display: inline-block;
    transition: background-color 0.3s, color 0.3s;
}

/* Popup styles */
.popup {
    display: none;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    background-color: #282c34;
    padding: 20px;
    border: 2px solid #ada0d3;
    border-radius: 10px;
    z-index: 1000;
    text-align: center;
    min-width: 300px;
}

.popup.error {
    border-color: #ff6b6b;
}

.popup.success {
    border-color: #51cf66;
}

.popup-content {
    color: white;
    margin-bottom: 20px;
}

.popup-close {
    background-color: #ada0d3;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
}

.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}
</style>
<body>
    <div class="banner">
        <div class="navbar">
            <a href="https://www.just.edu.jo/Pages/Default.aspx">
                <img src="logo.png" class="logo">
            </a>
            <ul>
                    <li><a href="AboutPage.html">Home</a></li>
                    <li><a href="ReservationHistoryServlet">History</a></li>
                    <li><a href="LogoutServlet">Log out</a></li>
            </ul>
        </div>
        <div class="confirmation-container">
            <h1>BOOKING CONFIRMATION</h1>

            <%
                // Retrieve the departure date string from session
                String departureDateStr = (String) session.getAttribute("departureDateTime");

                if (departureDateStr != null) {
                    // Parse the string into a Date object
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date departureDate = inputFormat.parse(departureDateStr);

                    // Format the Date object into the desired format with AM/PM
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
                    String formattedDate = outputFormat.format(departureDate);
            %>
            <p><strong>Date:</strong> <%= formattedDate %></p>
            <% } else { %>
            <p><strong>Date:</strong> No date available</p>
            <% } %>

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
    </div>

    <!-- Popup elements -->
    <div id="overlay" class="overlay"></div>
    
    <div id="errorPopup" class="popup error">
        <div class="popup-content" id="errorMessage"></div>
        <button class="popup-close" onclick="closePopup('errorPopup')">Close</button>
    </div>

    <div id="successPopup" class="popup success">
        <div class="popup-content" id="successMessage"></div>
        <button class="popup-close" onclick="closePopup('successPopup')">Close</button>
    </div>

    <script>
    function showPopup(popupId, message) {
        document.getElementById('overlay').style.display = 'block';
        const popup = document.getElementById(popupId);
        popup.style.display = 'block';
        document.getElementById(popupId === 'errorPopup' ? 'errorMessage' : 'successMessage').textContent = message;
    }

    function closePopup(popupId) {
        document.getElementById('overlay').style.display = 'none';
        document.getElementById(popupId).style.display = 'none';
    }

    // Check for messages in session and show appropriate popup or redirect
    <% if (session.getAttribute("errorMessage") != null) { %>
        showPopup('errorPopup', '<%= session.getAttribute("errorMessage") %>');
        <% session.removeAttribute("errorMessage"); %>
    <% } %>

    <% if (session.getAttribute("successMessage") != null) { %>
        window.location.href = 'ThankYouPage.html';
        <% session.removeAttribute("successMessage"); %>
    <% } %>
</script>
</body>
</html>
