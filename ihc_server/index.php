<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>   <!-- the user is logged in -->

  <html>
  <head>
    <title>Home - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/components/dropdown.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");   // load the site header and navigation bar
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <br><br>
    <div><center>
      <div>
        <a href="./add_event.php">
          <button class="circular ui icon button">
            <i class="upload icon"></i>
            <p>Add an Event</p>
          </button>
        </a>
        <a href="./manage_events.php">
          <button class="circular ui icon button">
            <i class="suitcase icon"></i>
            <p>Manage Events</p>
          </button>
        </a>
      </div>
      <br>
      <div>
        <a href="./add_primary_resource.php">
          <button class="circular ui icon button">
            <i class="book icon"></i>
            <p>Add to Resource Page</p>
          </button>
        </a>
        <a href="./manage_primary_resources.php">
          <button class="circular ui icon button">
            <i class="info circle icon"></i>
            <p>Manage Resource Page</p>
          </button>
        </a>
      </div>
      <br>
      <div>
        <a href="./add_marker.php">
          <button class="circular ui icon button">
            <i class="map pin icon"></i>
            <p>Add a Resource to the Map</p>
          </button>
        </a>
        <a href="./manage_resource_map.php">
          <button class="circular ui icon button">
            <i class="map icon"></i>
            <p>Manage Resource Map</p>
          </button>
        </a>
      </div>
      <br>
      <div>
        <a href="./add_prize.php">
          <button class="circular ui icon button">
            <i class="plus icon"></i>
            <p>Add a Prize</p>
          </button>
        </a>
        <a href="./manage_prizes.php">
          <button class="circular ui icon button">
            <i class="edit icon"></i>
            <p>Manage Prizes</p>
          </button>
        </a>
      </div>
      <br>
      <div>
        <a href="./manage_about.php">
          <button class="circular ui icon button">
            <i class="plus icon"></i>
            <p>Manage About Page</p>
          </button>
        </a>
      </div>
      <br>
      <div>
        <a href="./add_user.php">
          <button class="circular ui icon button">
            <i class="plus icon"></i>
            <p>Add User</p>
          </button>
        </a>
      </div>
    </center></div>
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
