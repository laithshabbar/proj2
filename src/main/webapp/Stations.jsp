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
    <style>
    .custom-select {
    position: absolute; 
    top: 50%; 
    left: 38%; 
    transform: translate(-50%, -50%); 
    font-size: 25px;
    z-index: 100;
}
.custom-select select {
    display: none; 
}
.select-selected {
    background-color: #ada0d3;
    width: 300%;
    border-radius: 25px 25px 0px 0px;
}
.select-selected:after {
    position: absolute;
    content: "";
    top: 23px;
    left: 300%;
    height: 0;
    border: 6px solid transparent;
    border-color: #fff transparent transparent transparent;
}
.select-selected.select-arrow-active:after {
    border-color: transparent transparent #fff transparent;
    top: 15px;
}
.select-items div, .select-selected {
    color: #ffffff;
    padding: 12px 20px; 
    border: 1px solid transparent;
    border-color: transparent transparent rgba(0, 0, 0, 0.1) transparent;
    cursor: pointer;
    font-size: 20px; 
}
.select-items {
    position: absolute;
    background-color:#644ea7 ;
    border-bottom-left-radius: 25px;
    border-bottom-right-radius: 25px;
    top: 100%;
    left: 0;
    right: 0;
    z-index: 99;
    width: 321%;
    max-height: 300px; 
    overflow-y: auto; 
}
.select-hide {
    display: none;
}
.select-items div:hover, .same-as-selected {
    background-color: rgba(0, 0, 0, 0.1);
}
.buttons2 {
    position: absolute; 
    top: 63%; 
    left: 38%; 
    transform: translate(-50%, -50%); 
    width:40%;
    text-align: center;
    margin: 12%;
    font-weight: bold;
    background: transparent;
    color: white;
    cursor: pointer;
    font-size: 20px;
    margin-top: 1%;
    z-index: 1;
}
    </style>
    <body>
        <div class="content">
            <h1>SELECT YOUR STATION</h1>
            <p>Choose one of the following supported stations in ${cityName}.</p>   
        </div>
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
        </div>
        <form action="StationServlet" method="post">
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