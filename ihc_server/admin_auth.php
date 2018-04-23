<!DOCTYPE html>

<?php require "./admin_server/login.php"; ?>

<?php if (!isset($_SESSION["id"]) && $_SESSION["id"] != null) {
  $url = "./index.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
else {
  ?>

  <html>
  <head>
    <title>Login - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/components/dropdown.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
  </head>
  <body>
    <center>
      <h2>Login</h2>
      <p>Please fill in your credentials to login.</p>
      <form action="./admin_server/login.php" method="post">
        <div class="elem">
          Email: <input class="inputbox" type="email" name="email"><br><br>
        </div>
        <div class="elem">
          Password: <input class="inputbox" type="password" name="password"><br><br>
        </div>
        <input class="ui button" type="submit" value="Log In">
      </form>
    </center>
  </body>
  </html>

<?php } ?>
