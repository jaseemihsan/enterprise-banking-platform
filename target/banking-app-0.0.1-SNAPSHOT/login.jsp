<!DOCTYPE html>
<html>
<head>
    <title>ABC Bank - Login</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: #f4f7fb;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        header {
            background: #0056b3;
            color: white;
            text-align: center;
            padding: 20px;
            font-size: 28px;
            font-weight: bold;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
        }

        .container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-box {
            width: 380px;
            background: white;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
        }

        .login-box h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #0056b3;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            margin-bottom: 18px;
            font-size: 15px;
        }

        input:focus {
            outline: none;
            border-color: #0056b3;
            box-shadow: 0 0 5px rgba(0,86,179,0.4);
        }

        button {
            width: 100%;
            padding: 12px;
            background: #0056b3;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background: #003d80;
        }

        .error {
            color: red;
            text-align: center;
            margin-bottom: 15px;
            font-weight: bold;
        }

        footer {
            background: #0056b3;
            color: white;
            text-align: center;
            padding: 15px;
            font-size: 14px;
        }
    </style>

</head>

<body>

<header>
    ABC Bank - Secure Enterprise Banking
</header>

<div class="container">

    <div class="login-box">

        <h2>Login</h2>

        <% if(request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="login" method="post">

            <label>Username</label>

            <input type="text"
                   name="username"
                   placeholder="Enter Username"
                   required>

            <label>Password</label>

            <input type="password"
                   name="password"
                   placeholder="Enter Password"
                   required>

            <button type="submit">
                Login
            </button>

        </form>

    </div>

</div>

<footer>
    © 2026 ABC Bank | Secure Banking Platform
</footer>

</body>
</html>
