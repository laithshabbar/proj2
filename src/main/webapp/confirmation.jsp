<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    background-color: #282c34; /* Adjust background color if needed */
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    color: white; /* Default text color for all text elements */
}

.banner {
    text-align: center; /* Centers the content inside the banner */
}

h1 {
    font-size: 3rem; /* Adjust size as needed */
    color: white;
    margin-bottom: 1rem;
}

p {
    font-size: 1.5rem; /* Adjust size as needed */
    color: white;
    margin: 0.5rem 0;
}

.confirmation-container {
    margin-top:2%;
    text-align: center; /* Centers the content of the container */
    background-color: rgba(0, 0, 0, 0.6); /* Optional: Add a background to the confirmation box */
    padding: 4rem;
    border-radius: 8px; /* Optional: Rounded corners */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Optional: Add a shadow for better visual effect */
}

button{
    width:25%;
    padding: 40px 0;
    text-align: center;
    border-radius: 25px;
    margin: 2%;
    font-weight: bold;
    border: 2px solid  #ada0d3;
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
button:hover {
    background: #ada0d3;
    color: wh; /* Invert text color on hover */
}

button:hover span {
    width: 100%;
}

span {
    background: #ada0d3;
    height: 100%;
    width: 0;
    border-radius: 25px; /* Adjust as needed */
    position: absolute;
    left: 0;
    bottom: 0;
    z-index: -1;
    transition: 0.5s;
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
    width: 360px; /* Adjust size as needed */
}
</style>
<body>
<div class="banner">
<div class="navbar">
                <a href="https://www.just.edu.jo/Pages/Default.aspx">
                    <img src="logo.png" class="logo">
                </a>
                <ul>
                    <li><a href="#">Home</a></li>
                    <li><a href="ReservationHistoryServlet">Account</a></li>
                    <li><a href="#">Stations</a></li>
                </ul>
            </div>          
    <div class="confirmation-container">
        <h1>BOOKING CONFIRMATION</h1>
        <p><strong>Date:</strong> ${sessionScope.departureDateTime}</p>
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
</body>
</html>
