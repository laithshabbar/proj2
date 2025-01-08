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
<style>
@media (max-width: 1249px) {
            .logo {
                max-width: 360px;
            }
            .content  {
                margin-top: 25%;
            }
        }
        @media (max-width: 1031px) {
            .logo {
                max-width: 500px;
            }
            .content  {
                margin-top: 30%;
            }
            .content h1{
            font-size:50px;
            }
            
        }
        @media (max-width: 800px) {
            .logo {
                max-width: 500px;
                align-content:center;
            }
            .content  {
                margin-top: 40%;
            }
            .content h1{
            font-size:40px;
            }
            .content p{
            font-size:15px;
            }
            
        }
        @media (max-width: 699px) {/*xddddd*/
            .logo {
                max-width: 500px;
                align-content:center;
            }
            .content  {
                margin-top: 50%;
            }
            .content h1{
            font-size:30px;
            }
            .content p{
            font-size:10px;
            }
            .seat-column {
                grid-template-columns: repeat(4, 1fr);
                gap: 8px;
            }
    
            .seat {
                width: 50px;
                height: 35px;
                font-size: 12px;
            }
            
        }
        
</style>
<body>
    <div class="banner">
        <div class="navbar">
            <a href="https://www.just.edu.jo/Pages/Default.aspx">
                <img src="logo.png" alt="Logo" class="logo">
            </a>
            <ul>
                    <li><a href="AboutPage.html">Home</a></li>
                    <li><a href="ReservationHistoryServlet">History</a></li>
                    <li><a href="LogoutServlet">Log out</a></li>
            </ul>
        </div>

        <div class="content">
            <h1>SELECT YOUR SEAT</h1>
            <p>Choose one of the following open seats. Reserved seats will be grayed out.</p>

            <% 
                // Database connection code
                Integer rideId = (Integer) session.getAttribute("rideId");
                if (rideId == null) {
                    out.print("Ride ID is not set.");
                    return;
                }

                Connection connection = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                Set<String> reservedSeats = new HashSet<String>(); 

                try {
                    connection = util.DBConnection.getConnection();
                    String sql = "SELECT seat_number FROM reservations WHERE ride_id = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, rideId);
                    rs = stmt.executeQuery();

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
                              
                                String[] topSeats = {"A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                                                   "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8"};
                                for (String seat : topSeats) {
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
                             
                                String[] bottomSeats = {"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8",
                                                      "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8"};
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

        seatButtons.forEach(button => {
            button.addEventListener('click', () => {
                if (button.disabled || button.style.backgroundColor === "gray") {
                    return;
                }

                const seatNumber = button.dataset.seatNumber;
                
                seatButtons.forEach(btn => {
                    if (!btn.disabled && btn.style.backgroundColor !== "gray") {
                        btn.style.backgroundColor = "";
                    }
                });

                seatNumberInput.value = seatNumber;
                button.style.backgroundColor = "#644ea7";
                confirmBtn.disabled = false;
            });
        });
    </script>
</body>
</html>
