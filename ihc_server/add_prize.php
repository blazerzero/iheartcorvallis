<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <html>
  <head>
    <title>Add a Prize - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var nameField = document.forms["prizeForm"]["name"].value;
      var levelField = document.forms["prizeForm"]["level"].value;
      if (nameField == null || nameField == "" ||
      levelField == null || levelField == "") {
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
      $("#siteheader").load("siteheader.html");
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Add a Prize</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/add_prizes_server.php" method="post" enctype="multipart/form-data">
        <div class="elem">
          <span class="requirednote">*</span>
          Name of Prize: <input class="inputbox" type="text" name="name"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Prize Level: <select class="ui search dropdown" name="level">
            <option value="">Choose a level</option>
            <option value="1">Gold</option>
            <option value="2">Silver</option>
            <option value="3">Bronze</option>
          </select>
          <br><br>
        </div>
        <input class="ui green button" type="submit" value="Create Prize">
      </form>
    </div>
  </body>
  </html>

  <?php
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
