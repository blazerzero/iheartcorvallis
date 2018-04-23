<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <html>
  <head>
    <title>Regression Analysis Hub - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script>
    function validateForm() {
      var stat1Field = document.forms["regressionForm"]["stat1"].value;
      var stat2Field = document.forms["regressionForm"]["stat2"].value;
      if (stat1Field == null || stat1Field == "" ||
          stat2Field == null || stat2Field == "") {
        alert("Please choose two statistics to compare!");
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
      <left class="sectionheader"><h1>Regression Analysis Hub</h1></left><br>
      <div class="ui divider"></div><br>

      <form name="regressionForm" onsubmit="return validateForm()" action="./admin_server/get_regression_data.php" method="post" enctype="multipart/form-data">
        Compare
        <select class="ui search dropdown" name="stat1">
          <option value="">Choose statistic</option>
          <option value="1">Change in Time</option>
          <option value="2"></option>

        </select>
        with
        <select class="ui search dropdown" name="stat2">
          <option value="">Choose statistic</option>
        </select>
        <input class="ui green button" type="submit" value="Create Regression Visual">
      </form>


    </div>
  </body>
  </html>

  <?php
  $stmt->close();
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
