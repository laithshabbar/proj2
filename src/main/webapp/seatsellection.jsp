<%@ page import="java.sql.*, java.util.*" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seat Selection Page</title>
    <link rel="stylesheet" href="seatstyle.css">
</head>
<body>
    <div class="banner">
        <div class="navbar">
            <a href="https://www.just.edu.jo/Pages/Default.aspx">
                <img src="logo.png" alt="Logo" class="logo">
            </a>
            <ul>
                <li><a href="#">Home</a></li>
                <li><a href="#">Account</a></li>
                <li><a href="#">Stations</a></li>
            </ul>
        </div>

        <div class="content">
            <h1>SELECT YOUR SEAT</h1>
            <p>Choose one of the following open seats. Reserved seats will be grayed out.</p>

            <% 
                // Get rideId from session
                Integer rideId = (Integer) session.getAttribute("rideId");
                if (rideId == null) {
                    out.print("Ride ID is not set.");
                    return;
                }

                // Database connection setup
                Connection connection = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                Set<String> reservedSeats = new HashSet<String>(); // Explicit type for Java < 1.7

                try {
                    // Assuming a JDBC connection is established
                    connection = DriverManager.getConnection("jdbc:mysql://database-1.ctko6w88sr3f.eu-north-1.rds.amazonaws.com/bus_system", "laith", "Laith2002");

                    // Query to fetch reserved seat numbers for the given rideId
                    String sql = "SELECT seat_number FROM reservations WHERE ride_id = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, rideId);
                    rs = stmt.executeQuery();

                    // Populate reservedSeats with seat numbers
                    while (rs.next()) {
                        reservedSeats.add(rs.getString("seat_number"));
                    }
                } catch (SQLException e) {
                    out.print("Error fetching seat availability: " + e.getMessage());
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (connection != null) connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            %>

            <div class="bus-container">
                <div class="bus-layout">
                    <div class="top-seats">
                        <div class="seat-column">
                            <% 
                                // Generate seat buttons for the first part of the bus
                                String[] seats = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4"};
                                for (String seat : seats) {
                                    boolean isReserved = reservedSeats.contains(seat);
                                    String disabledAttribute = isReserved ? "disabled" : "";
                                    String style = isReserved ? "background-color: gray;" : "";
                            %>
                            <button class="seat" data-seat-number="<%= seat %>" <%= disabledAttribute %> style="<%= style %>"><%= seat %></button>
                            <% } %>
                        </div>
                    </div>
                    <div class="bottom-seats">
                        <div class="seat-column">
                            <% 
                                // Generate seat buttons for the second part of the bus
                                String[] bottomSeats = {"A5", "A6", "A7", "A8", "B5", "B6", "B7", "B8", "C5", "C6", "C7", "C8", "D5", "D6", "D7", "D8"};
                                for (String seat : bottomSeats) {
                                    boolean isReserved = reservedSeats.contains(seat);
                                    String disabledAttribute = isReserved ? "disabled" : "";
                                    String style = isReserved ? "background-color: gray;" : "";
                            %>
                            <button class="seat" data-seat-number="<%= seat %>" <%= disabledAttribute %> style="<%= style %>"><%= seat %></button>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>

            <form id="reservationForm" action="SeatReservationServlet" method="POST">
                <input type="hidden" id="seatNumberInput" name="seatNumber">
                <input type="hidden" id="rideIdInput" name="rideId" value="<%= rideId %>">
                <button id="confirmBtn" type="submit" disabled>Confirm Seat</button>
            </form>
        </div>
    </div>

    <script>
        const seatButtons = document.querySelectorAll('.seat');
        const seatNumberInput = document.getElementById('seatNumberInput');
        const confirmBtn = document.getElementById('confirmBtn');

        // Loop through all seat buttons and add an event listener
        seatButtons.forEach(button => {
            button.addEventListener('click', () => {
                // Skip reserved seats (those that are disabled or gray)
                if (button.disabled || button.style.backgroundColor === "gray") {
                    return;
                }

                const seatNumber = button.dataset.seatNumber; // Get seat number from the button's data attribute
                
                // Reset all seat buttons to default state (no background color)
                seatButtons.forEach(btn => {
                    if (!btn.disabled && btn.style.backgroundColor !== "gray") {
                        btn.style.backgroundColor = ""; // Reset background color for available seats
                    }
                });

                // Set the seat number in the hidden input field
                seatNumberInput.value = seatNumber;

                // Update the color of the clicked seat to indicate selection
                button.style.backgroundColor = "green";  // Selected seat color

                // Enable the confirmation button
                confirmBtn.disabled = false;
            });
        });
    </script>
</body>
</html>
