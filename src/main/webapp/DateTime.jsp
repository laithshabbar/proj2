<%@ page import="java.sql.*, java.util.*, javax.sql.*, util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Date Time Page</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }
        .banner {
            width: 100%;
            height: 100vh;
            background-image: linear-gradient(rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0.75)), url(background.jpg);
            background-size: cover;
            background-position: center;
        }
        .navbar {
            width: 85%;
            margin: auto;
            padding: 35px 0;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .logo {
            width: 360px !important;
            cursor: pointer;
        }
        .navbar ul li {
            list-style: none;
            display: inline-block;
            margin: 0 20px;
            position: relative;
        }
        .navbar ul li a {
            text-decoration: none;
            color: white;
            text-transform: uppercase;
        }
        .content {
            width: 100%;
            position: absolute;
            margin-top: 15%;
            transform: translateY(-50%);
            text-align: center;
            color: white;
        }
        .content h1 {
            font-size: 80px;
        }
        .content p {
            font-size: 20px auto;
            font-weight: 100;
            line-height: 25px;
            margin-bottom: 5%;
            margin-top: 1%;
        }
        .datetime-container {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-bottom: 30px;
        }

        .input-group {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }

        .input-group:hover {
            transform: translateY(-5px);
        }

        .input-group label {
            display: block;
            margin-bottom: 12px;
            font-size: 18px;
            color: white;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        input[type="date"],
        select {
            width: 300px;
            padding: 15px;
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 10px;
            background: rgba(173, 160, 211, 0.2);
            color: white;
            font-size: 16px;
            outline: none;
            max-height: 200px;
            transition: all 0.3s ease;
        }

        input[type="date"]:focus,
        select:focus {
            border-color: #ada0d3;
            background: rgba(173, 160, 211, 0.3);
        }

        select option {
            background-color: darkslateblue;
            font-size: 15px;
            font-weight: bold;
            color: white;
        }

        button[type="submit"] {
            padding: 15px 40px;
            background: transparent;
            border: 2px solid #ada0d3;
            color: white;
            border-radius: 25px;
            cursor: pointer;
            font-size: 18px;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: all 0.3s ease;
        }

        button[type="submit"]:hover {
            background: rgba(173, 160, 211, 0.3);
            transform: translateY(-2px);
        }
        @media (max-width: 1024px) {
        .content h1 {
            font-size: 2.8rem;
        }

        .content p {
            font-size: 1.1rem;
        }

        .navbar ul {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
        }

        .navbar ul li {
            margin: 15px 25px;
        }

        .datetime-container {
            gap: 20px;
        }

        .input-group {
            width: 40%;
        }

        button[type="submit"] {
            width: 40%;
        }
    }

    @media (max-width: 768px) {
 
        .content {
            top: 55%;
            transform: translateY(-80%); 
        }

        .content h1 {
            font-size: 2rem;
        }

        .content p {
            font-size: 0.9rem;
            margin-bottom: 10px;
        }

        .navbar ul {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
        }

        .navbar ul li {
            margin: 10px 0;
        }

        .datetime-container {
            flex-direction: column;
            gap: 10px;
            padding: 10px;
        }

        .input-group {
            width: 90%;
            padding: 20px;
        }

        input[type="date"],
        select,
        button[type="submit"] {
            width: 60%;
            padding: 12px;
            font-size: 14px;
        }

        button[type="submit"] {
            font-size: 16px;
        }
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
                    <li><a href="LogoutServlet">Log out</a></li>
            </ul>
        </div>
        <div class="content">
            <h1>SELECT DEPARTURE TIME</h1>
            <p>Choose the time and date you wish to depart to JUST university</p>

            <form id="datetime-form" action="SaveDateTimeServlet" method="POST">
                <div class="datetime-container">
                    <div class="input-group">
                        <label for="departure-date">Select Date</label>
                        <input type="date" id="departure-date" name="departure-date" required>
                    </div>

                    <div class="input-group">
                        <label for="departure-time">Select Time</label>
                        <select id="departure-time" name="departure-time" required>
                            <option value="">Select Date First</option>
                        </select>
                    </div>
                </div>
                <button type="submit" id="submit-btn">Confirm Departure</button>
            </form>
        </div>
    </div>

    <script>
        const dateInput = document.getElementById('departure-date');
        const timeSelect = document.getElementById('departure-time');
        const form = document.getElementById('datetime-form');

        const today = new Date();
        today.setDate(today.getDate() + 1);
        const tomorrow = today.toISOString().split('T')[0];
        dateInput.setAttribute('min', tomorrow);

        dateInput.addEventListener('change', function() {
            const selectedDate = new Date(this.value);
            if (selectedDate.getDay() === 5) {
                alert("Fridays are not available for departure.");
                this.setCustomValidity("Fridays are not allowed.");
                this.reportValidity();
                timeSelect.innerHTML = '<option value="">Select Date First</option>';
                return;
            }
            this.setCustomValidity("");
            fetchTimes(this.value);
        });

        function fetchTimes(date) {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', 'FetchAvailableTimesServlet', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            
            xhr.onload = function() {
                if (xhr.status === 200) {
                    timeSelect.innerHTML = xhr.responseText;
                }
            };
            
            const params = 'date=' + date + 
                          '&city=<%= session.getAttribute("selectedCity") %>' + 
                          '&station=<%= session.getAttribute("selectedStation") %>';
            xhr.send(params);
        }

        form.onsubmit = function(e) {
            if (!dateInput.value || !timeSelect.value) {
                e.preventDefault();
                alert("Please select both date and time");
                return false;
            }
        };
    </script>
</body>
</html>