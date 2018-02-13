<!DOCTYPE HTML>

<?php

   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";

   $alreadyExists = False;

   $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
   //Output any connection error
   if ($mysqli->connect_error) {
       die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
       $message = "Unable to connect to the event database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       header("Location: ../index.html");
       exit;
   }

   $name = $location = $date = $time = $dateandtime = $description = $image = $link1 = $link2 = $link3 = $pin = $fullAddress = $addressData = $prepAddress = $latLng = $row = "";

   $eventids = array();

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $name = $_POST["name"];
      $location = $_POST["location"];
      $fullAddress = $_POST["fulladdress"];
      $date = $_POST["date"];
      $time = $_POST["time"];
      $description = $_POST["description"];
      $image = $_POST["image"];
      $link1 = $_POST["link1"];
      $link2 = $_POST["link2"];
      $link3 = $_POST["link3"];
      $pin = $_POST["pin"];

      $dateandtime = $date . " " . $time . ":00";

      /* GET EVENT COUNT */
      $result = $mysqli->query("SELECT eventid, totalEventCount FROM ihc_events");
      while ($row = $result->fetch_assoc()) {
         $events[] = $row;
      }

      if ($events[count($events)-1]['totalEventCount'] > $events[count($events)-1]['eventid']) {
         $totalEventCount = $events[count($events)-1]['totalEventCount'] + 1;
      }
      else {
         $totalEventCount = $events[count($events)-1]['eventid'] + 1;
      }

      /* ADD EVENT TO DATABASE */
      $result = $mysqli->query("INSERT INTO ihc_events (eventid, name, location, address, dateandtime, description, image, link1, link2, link3, pin) VALUES ('$totalEventCount', '$name', '$location', '$fullAddress', '$dateandtime', '$description', '$image', '$link1', '$link2', '$link3', '$pin')");

      if ($result == True) {
         $message = "Event has been added!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      if ($result == False) {
         $message = "Error adding event!<br>"; # error adding account to database
         $url = "../add_event.php";
         echo "<script type='text/javascript'>alert('$message');</script>";
         echo "<script type='text/javascript'>document.location.href = '$url';</script>";
         exit;
      }

      /* UPDATE TOTAL EVENT COUNT IN DATABASE */
      $result = $mysqli->query("UPDATE ihc_events SET totalEventCount='$totalEventCount'");
      if ($result == True) {
         $url = "../index.html";
         echo "<script type='text/javascript'>document.location.href = '$url';</script>";
         exit;
      }
      if ($result == False) {
         $message = "Error updating total event count!<br>";
         $url = "../add_event.php";
         echo "<script type='text/javascript'>alert('$message');</script>";
         echo "<script type='text/javascript'>document.location.href = '$url';</script>";
         exit;
      }

   mysqli_close($con);
}
?>

<!--<html>
   <head>
      <title>Event Added - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="../css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="../css/stylesheet.css"/>
      <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
         $("return-btn").click(function() {
            document.location.href = "../index.html";
         });
      });
      </script>
   </head>
   <body>
      <div class="siteheader">
         <br><br>
         <left class="sitenametop">I HEART CORVALLIS</left>
         <br><br>
         <left class="sitenamebottom">Administrative Suite</left>
         <br><br>
      </div>
      <ul class="navbar">
         <div class="ui pointing dropdown link item">
            <a href="../index.html" class="text">Home</a>
         </div>
         <div class="ui pointing dropdown link item" id="events_dropdown">
            <a class="text">Events</a>
            <div class="menu">
               <a href="../add_event.php" class="item">Add an Event</a>
               <a href="../manage_events.php" class="item">Manage Events</a>
            </div>
         </div>
         <div class="ui pointing dropdown link item" id="resources_dropdown">
            <a class="text">Resources</a>
            <div class="menu">
               <a class="item">Manage Primary Resources</a>
               <a class="item">Manage Resource Map</a>
            </div>
         </div>
         <div class="ui pointing dropdown link item" id="prizes_dropdown">
            <a class="text">Prizes</a>
            <div class="menu">
               <a class="item">Add a Prize</a>
               <a class="item">Manage Prizes</a>
            </div>
         </div>
      </ul>

      <div class="mainbody">
         <button class="ui large orange button" id="return-btn">Return Home</button>
      </div>
</html>-->
