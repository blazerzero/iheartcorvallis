<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <html>
  <head>
    <title>Authorize a New User - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var emailField = document.forms["prizeForm"]["email"].value;
      var passwordField = document.forms["prizeForm"]["password"].value;
      if (emailField == null || emailField == "" ||
          passwordField == null || passwordField == "") {   // if any required field in the form is empty
            alert("Please fill both fields before submitting!");
            return false;
      }
      else {
        return true;
      }
    }
    </script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");   // load the site header and the navigation bar
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Authorize a New User</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/add_users_server.php" method="post" enctype="multipart/form-data">
        <div class="elem">
          <span class="requirednote">*</span>
          Email: <input class="inputbox" type="email" name="email"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Password: <input class="inputbox" type="text" name="password"><br><br>
        </div>
        <input class="ui green button" type="submit" value="Create New Authorized User">
      </form>
    </div>
  </body>
  </html>

  <?php
  $mysqli->close();
}
else {    // the user is not logged in
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect the user to the login page
}
?>
