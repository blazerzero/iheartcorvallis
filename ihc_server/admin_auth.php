<!DOCTYPE html>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (!isset($_SESSION["id"]) && $_SESSION["id"] != null) {
  $url = "./index.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
else { ?>

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
    <div class="siteheader">
      <br><br>
      <left class="sitenametop">I HEART CORVALLIS</left>
      <br><br>
      <left class="sitenamebottom">Administrative Suite</left>
      <br><br>
    </div>
    <br><br>
    <center>
      <h1>Login</h1>
      <h4>Please fill in your credentials to login.</h4>
      <form action="./admin_server/login.php" method="post">
        <div class="elem">
          Email: <input class="inputbox" type="email" name="email"><br><br>
        </div>
        <div class="elem">
          Password: <input class="inputbox" type="password" name="password"><br><br>
        </div>
        <input class="ui large button" type="submit" value="Log In">
      </form>
    </center>
  </body>
  </html>

<?php } ?>
