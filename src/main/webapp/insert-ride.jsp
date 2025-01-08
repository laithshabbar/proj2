<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert Ride</title>
    <link rel="stylesheet" href="style.css">
   
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

    <style>
    .banner{
    width: 100%;
    height: 100vh;
    background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
    background-size: cover;
    background-position: center;
            }
            .navbar{
                width: 85%;
                margin: auto;
                padding: 35px 0;
                display: flex;
                align-items: center;
                justify-content: space-between;
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
    .flatpickr-calendar.open {
    position: fixed !important;
    top: 50% !important;
    left: 50% !important;
    transform: translate(-50%, -50%) !important;
    z-index: 9999 !important;
}

.flatpickr-calendar:before,
.flatpickr-calendar:after {
    display: none !important;
}

/* semi-transparent overlay */
.flatpickr-calendar.open:before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: -1;
}
.flatpickr-calendar {
            background: #2b2b2b !important;
            border: 1px solid #ada0d3 !important;
            border-radius: 15px !important;
            box-shadow: 0 5px 15px rgba(0,0,0,0.3) !important;
            width: 320px !important;
            font-size: 14px !important;
        }

        .flatpickr-day {
            color: white !important;
            border-radius: 5px !important;
        }

        .flatpickr-day.selected {
            background: #ada0d3 !important;
            border-color: #ada0d3 !important;
        }

        .flatpickr-day:hover {
            background: rgba(173, 160, 211, 0.3) !important;
        }

        .flatpickr-day.disabled {
            color: #666 !important;
        }

        .flatpickr-months .flatpickr-month {
            background: #2b2b2b !important;
            color: white !important;
        }

        .flatpickr-current-month .flatpickr-monthDropdown-months {
            background: #2b2b2b !important;
            color: white !important;
        }

        .flatpickr-time {
            background: #2b2b2b !important;
            border-top: 1px solid #ada0d3 !important;
        }

        .flatpickr-time input {
            color: white !important;
            background: transparent !important;
        }

        .flatpickr-time .flatpickr-am-pm {
            color: white !important;
            background: transparent !important;
        }

        .numInputWrapper:hover {
            background: rgba(173, 160, 211, 0.1) !important;
        }

        .flatpickr-weekdays {
            color: #ada0d3 !important;
        }

        .flatpickr-prev-month, .flatpickr-next-month {
            color: #ada0d3 !important;
        }

        .flatpickr-prev-month:hover svg, .flatpickr-next-month:hover svg {
            fill: white !important;
        }
        .form-group {
            display: flex;
            flex-direction: column;
            margin: 20px 0;
            align-items: center;
        }

        .form-group label {
            font-size: 20px;
            margin-bottom: 10px;
            color: white;
            font-weight: bold;
        }

        select, input[type="text"] {
            width: 300px;
            padding: 15px;
            margin: 5px 0;
            border: 2px solid #ada0d3;
            border-radius: 20px;
            background-color: rgba(173, 160, 211, 0.2);
            color: white;
            font-size: 18px;
        }

        select option {
            background-color: #644ea7;
            color: white;
        }

        button {
            width: 200px;
            padding: 15px 30px;
            margin: 20px;
            border: 2px solid #ada0d3;
            border-radius: 25px;
            background: transparent;
            color: white;
            cursor: pointer;
            font-size: 18px;
            transition: all 0.3s ease;
            position: relative;
            z-index: 1;
        }

        button:before {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 0%;
            height: 100%;
            background-color: #ada0d3;
            transition: all 0.3s;
            border-radius: 25px;
            z-index: -1;
        }

        button:hover {
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(173, 160, 211, 0.4);
        }

        button:hover:before {
            width: 100%;
        }

        #message {
            color: #ff6b6b;
            margin: 20px;
            font-size: 18px;
        }

        @media (max-width: 1400px) {
            .logo {
                max-width: 300px;
            }
            .content  {
                margin-top: 25%;
            }
            .content h1{
            font-size:70px;
            }
            .button {
                margin-top: 25%;
            }
        }
        @media (max-width: 1125px) {
            .logo {
                max-width: 400px;
            }
            .content  {
                margin-top: 30%;
            }
            .content h1{
            font-size:60px;
            }
            .button {
                margin-top: 25%;
            }
        }
        @media (max-width: 881px) {
            .logo {
                max-width: 400px;
            }
            .content  {
                margin-top: 40%;
            }
            .content h1{
            font-size:60px;
            }
            .button {
                margin-top: 25%;
            }
        }
        @media (max-width: 630px) {
            .logo {
                max-width: 400px;
            }
            .content  {
                margin-top: 50%;
            }
            .content h1{
            font-size:30px;
            }
            .button {
                margin-top: 25%;
            }
        }
    </style>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            
            flatpickr("#departure_time", {
                enableTime: true,
                dateFormat: "Y-m-d H:i",
                minDate: tomorrow,
                maxTime: "18:00",
                minTime: "06:00",
                time_24hr: true,
                minuteIncrement: 10,
                defaultHour: 6,
                defaultMinute: 0,
                appendTo: document.getElementById('date-picker-container'),
                onChange: function(selectedDates, dateStr, instance) {
                    if (selectedDates[0]) {
                        var date = selectedDates[0];
                        var minutes = date.getMinutes();
                        var roundedMinutes = Math.round(minutes / 10) * 10;
                        date.setMinutes(roundedMinutes);
                        instance.setDate(date);
                    }
                }
            });
        });

        function updateStations() {
            var cityId = document.getElementById('city_id').value;
            var stationSelect = document.getElementById('station_id');
            stationSelect.innerHTML = '<option value="">Select a Station</option>';

            if (cityId) {
                var xhr = new XMLHttpRequest();
                xhr.open('GET', 'InsertRideServlet?city_id=' + cityId, true);
                xhr.onload = function () {
                    if (xhr.status === 200) {
                        var stations = JSON.parse(xhr.responseText);
                        stations.forEach(function (station) {
                            var option = document.createElement('option');
                            option.value = station.id;
                            option.textContent = station.name;
                            stationSelect.appendChild(option);
                        });
                    } else {
                        console.error('Failed to fetch stations: ' + xhr.statusText);
                    }
                };
                xhr.send();
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
                    <li><a href="LogoutServlet">Log out</a></li>
                   </ul>
        </div>
        <div class="content">
            <h1>INSERT A NEW RIDE</h1>
            <br><br><br>
            <div id="message" style="color: #644ea7;">
                <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
            </div>
            
            <form action="InsertRideServlet" method="post">
                

                <div class="form-group">
                    <label for="city_id">City:</label>
                    <select id="city_id" name="city_id" required onchange="updateStations()">
                        <option value="">Select a City</option>
                        <% 
                        @SuppressWarnings("unchecked")
                        ArrayList<Map<String, String>> cities = (ArrayList<Map<String, String>>) request.getAttribute("cities");
                        if (cities != null) {
                            for (Map<String, String> city : cities) {
                        %>
                            <option value="<%= city.get("id") %>"><%= city.get("name") %></option>
                        <% 
                                }
                            } else {
                        %>
                            <option value="">No cities available</option>
                        <% 
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="station_id">Station:</label>
                    <select id="station_id" name="station_id" required>
                        <option value="">Select a Station</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="departure_time">Departure Time:</label>
                    <div id="date-picker-container">
                        <input type="text" id="departure_time" name="departure_time" required>
                    </div>
                </div>

                <button type="submit">Insert Ride</button>
            </form>
        </div>
    </div>
</body>
</html>