<%@ page import="java.util.List" %>
<html>
    <head>
        <title>Choose your station page</title>
        <link rel="stylesheet" href="style.css">
        <style>
            .buttons-container {
                max-height: 400px;
                overflow-y: auto;
                padding: 20px;
                scrollbar-width: thin;
                scrollbar-color: #888 #f1f1f1;
            }
            .buttons-container::-webkit-scrollbar {
                width: 8px;
            }
            .buttons-container::-webkit-scrollbar-track {
                background: #f1f1f1;
                border-radius: 4px;
            }
            .buttons-container::-webkit-scrollbar-thumb {
                background: #888;
                border-radius: 4px;
            }
            .buttons-container::-webkit-scrollbar-thumb:hover {
                background: #555;
            }
            .buttons-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(200px, 1fr));
            gap: 40px 200px; /* Row gap and column gap */
            width: 100%;
            max-width: 1500px;
            margin-left: 105px; /* Adjust this value to control left alignment */
            justify-content: start; /* Keep items aligned to the left */
            }
            .bigbuttons {
            width: 140%;
            padding: 70px 0;
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
            font-size: 28px;
            margin-top: 1%;
            }
            form.buttons-grid {
                padding: 5px;
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
                    <li><a href="AboutPage.html">Home</a></li>
                    <li><a href="ReservationHistoryServlet">History</a></li>
                    <li><a href="#">Stations</a></li>
                </ul>
            </div>          
            <div class="content">
                <h1>SELECT YOUR CITY</h1>
                <p>Choose one of the following cities to go to one of the supported stations.</p>
                <div class="buttons-container">
                    <form action="CityServlet" method="post" class="buttons-grid">
                        <%
                            // Get the cities list from the request attribute
                            @SuppressWarnings("unchecked")
                            List<String> cities = (List<String>) request.getAttribute("cities"); 
                            if (cities != null) {
                                for (String city : cities) {
                        %>
                            <button type="submit" name="city" value="<%= city %>" class="bigbuttons"><span></span><%= city %></button>
                        <% 
                                }
                            } else {
                        %>
                            <p>No cities available</p>
                        <% 
                            }
                        %>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>