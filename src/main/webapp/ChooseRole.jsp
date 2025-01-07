<%@ page import="java.util.List" %>
<html>
    <head>
        <title>Choose your station page</title>
        <link rel="stylesheet" href="style.css">
        <style>
            .banner{
                width: 100%;
                height: 100vh;
                background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
                background-size: cover;
                background-position: center;
            }
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
            
        @media (max-width: 1200px) {
            .logo {
                max-width: 300px;
            }
            .content  {
                margin-top: 30%;
            }
            
            .button {
                margin-top: 25%;
            }
        }
        @media (max-width: 845px) {
            .logo {
                max-width: 300px;
            }
            .content  {
                margin-top: 35%;
            }
            .content h1{
            font-size:50px;
            }
            .button {
                margin-top: 25%;
            }
        }
        @media (max-width: 699px) {/*xddddd*/
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
            .button {
                margin-top: 25%;
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
            </div>          
            <div class="content">
                <h1>CHOOSE DRIVER ROLE</h1>
                <p>If you want to add a new ride click on ADD A RIDE, if you want to check you upcoming rides click on CHECK UPCOMING RIDES.</p>
                <div class="buttons-container">
                 <a href="InsertRideServlet">
                    <button >ADD A RIDE</button>
                    </a>
                    <a href="DriverRidesServlet">
                    <button >UPCOMING RIDES</button>
                    </a>
                </div>
            </div>
        </div>
    </body>
</html>