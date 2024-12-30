<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Station Selection</title>
        <link rel="stylesheet" href="style.css">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div class="content">
            <h1>SELECT YOUR STATION</h1>
            <p>Choose one of the following supported stations in <span id="cityName">${cityName}</span>.</p>   
        </div>
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
        <form action="StationSelectServlet" method="post">
            <div class="custom-select" style="width:200px;">
                <select name="station" required id="stationSelect">
                    <option value="">Select Station</option>
                    <%
                    @SuppressWarnings("unchecked")
                    ArrayList<HashMap<String, String>> stations = (ArrayList<HashMap<String, String>>) request.getAttribute("stations");
                    if(stations != null) {
                        for(HashMap<String, String> station : stations) {
                    %>
                        <option value="<%= station.get("name") %>" data-link="<%= station.get("maps_link") %>">
                            <%= station.get("name") %>
                        </option>
                    <%
                        }
                    }
                    %>
                </select>
            </div>   
            <div class="buttons2">
                <button type="submit"><span></span>CONFIRM</button>
                <button type="button" id="directionsBtn"><span></span>DIRECTIONS</button>
            </div>
        </form>
    </body>
    
    <!--drop down script-->
    <script src="dropdownscript.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const stationSelect = document.querySelector('#stationSelect');
            const directionsBtn = document.querySelector('#directionsBtn');
            
            directionsBtn.addEventListener('click', function() {
                const selectedOption = stationSelect.options[stationSelect.selectedIndex];
                if (stationSelect.value === '') {
                    alert('Please select a station first.');
                    return;
                }
                
                const mapsLink = selectedOption.getAttribute('data-link');
                if (mapsLink) {
                    window.open(mapsLink, '_blank');
                } else {
                    alert('No directions available for this station.');
                }
            });
        });
    </script>
</html>