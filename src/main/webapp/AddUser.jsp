<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Page - Manage Users</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }

        .banner {
            width: 100%;
            min-height: 100vh;
            background-image: linear-gradient(rgba(0,0,0,0.75),rgba(0,0,0,0.75)), url(AdminBackGround.jpg);
            background-size: cover;
            background-attachment: fixed;
            background-position: center;
            padding: 20px 0;
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
            width: 360px;
            cursor: pointer;
        }

        .content {
            width: 60%;
            margin: 10px auto;
            color: white;
        }

        .content h1 {
            font-size: 40px;
            margin-bottom: 30px;
            text-align: center;
        }

        .section {
            background-color: rgba(0, 0, 0, 0.5);
            padding: 30px;
            margin-bottom: 30px;
            border-radius: 25px;
            border: 2px solid #ada0d3;
        }

        .section h2 {
            font-size: 30px;
            margin-bottom: 20px;
            color: #ada0d3;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-size: 20px;
            margin-bottom: 10px;
            color: white;
        }

        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 300px;
            padding: 10px;
            border: 2px solid #ada0d3;
            border-radius: 15px;
            background-color: rgba(173, 160, 211, 0.2);
            color: white;
            font-size: 18px;
        }

        button, input[type="submit"] {
            padding: 15px 30px;
            border: 2px solid #ada0d3;
            border-radius: 25px;
            background: transparent;
            color: white;
            cursor: pointer;
            font-size: 18px;
            position: relative;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        button:before, input[type="submit"]:before {
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

        button:hover, input[type="submit"]:hover {
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(173, 160, 211, 0.4);
        }

        button:hover:before, input[type="submit"]:hover:before {
            width: 100%;
        }

        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-top: 20px;
            background-color: rgba(0, 0, 0, 0.3);
            border-radius: 15px;
            overflow: hidden;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border: 1px solid #ada0d3;
            color: white;
        }

        /* Remove double borders between cells */
        th:not(:last-child), td:not(:last-child) {
            border-right: none;
        }
        tr:not(:last-child) td, tr:not(:last-child) th {
            border-bottom: none;
        }

        /* Round the corners of the first and last cells */
        th:first-child {
            border-top-left-radius: 15px;
        }
        th:last-child {
            border-top-right-radius: 15px;
        }
        tr:last-child td:first-child {
            border-bottom-left-radius: 15px;
        }
        tr:last-child td:last-child {
            border-bottom-right-radius: 15px;
        }

        th {
            background-color: rgba(173, 160, 211, 0.2);
            color: #ada0d3;
        }

        @media (max-width: 1250px) {
            .content h1 { font-size: 30px; }
            input[type="text"],
            input[type="password"],
            input[type="email"] { width: 250px; }
        }

        @media (max-width: 768px) {
            .logo { width: 280px; }
            .content h1 { font-size: 24px; }
            input[type="text"],
            input[type="password"],
            input[type="email"] { width: 200px; }
            .section h2 { font-size: 24px; }
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
            <h1>ADMIN MANAGEMENT PAGE</h1>
            <br>
            <!-- Add Driver Section -->
            <div class="section">
                <h2>Add a Driver</h2>
                <form action="AddDriverServlet" method="post">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email">
                    </div>
                    
                    <button type="submit">Add Driver</button>
                </form>
            </div>

            <!-- All Drivers Section -->
            <div class="section">
                <h2>All Drivers</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        @SuppressWarnings("unchecked")
                        List<util.User> drivers = (List<util.User>) request.getAttribute("drivers");
                        
                        if (drivers != null) {
                            for (util.User user : drivers) {
                        %>
                        <tr>
                            <td><%= user.getUsername() %></td>
                            <td><%= user.getEmail() %></td>
                            <td>
                                <form action="AddDriverServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="user_id" value="<%= user.getUser_id() %>">
                                    <button type="submit">Remove</button>
                                </form>
                            </td>
                        </tr>
                        <% 
                            }
                        }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>