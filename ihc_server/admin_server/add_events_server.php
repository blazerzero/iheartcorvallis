<?php

require "./login.php";

if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
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

   $events = array();

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

      $url = "";

      if ($result == True) {
         $message = "Event has been added!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error adding event!<br>"; # error adding event to database
         $url = "../add_event.php";
         echo "<script type='text/javascript'>alert('$message');</script>";
         $mysqli->close();
         echo "<script type='text/javascript'>document.location.href = '$url';</script>";
         exit;
      }

      /* UPDATE TOTAL EVENT COUNT IN DATABASE */
      $result = $mysqli->query("UPDATE ihc_events SET totalEventCount='$totalEventCount'");
      if ($result == True) {
         $url = "../index.php";
      }
      else {
         $message = "Error updating total event count!<br>";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }

      $mysql->_close();
      $url = "../add_event.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      exit;

   }

   $mysqli->close();
}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}

?>
