<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";

   $alreadyExists = False;

   $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
   //Output any connection error
   if ($mysqli->connect_error) {
       die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
       echo "Connection failed!<br>";
   }
   else {
      //echo "Connection succesful!<br>";
   }

   $eventid = $name = $location = $startdate = $starttime = $startdt = $enddate = $endtime = $enddt = $description = $image = $link1 = $link2 = $link3 = $pin = $fullAddress = $addressData = $prepAddress = $latLng = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $eventid = $_POST["eventid"];
      $name = $_POST["name"];
      $location = $_POST["location"];
      $fullAddress = $_POST["fulladdress"];
      $startdate = $_POST["startdate"];
      $starttime = $_POST["starttime"];
      $enddate = $_POST["enddate"];
      $endtime = $_POST["endtime"];
      $description = $_POST["description"];
      $image = $_POST["image"];
      $link1 = $_POST["link1"];
      $link2 = $_POST["link2"];
      $link3 = $_POST["link3"];
      $pin = $_POST["pin"];

      $startdt = $startdate . " " . $starttime . ":00";
      $enddt = $enddate . " " . $endtime . ":00";
      if (strlen($startdt) < 19) {
         $startdt = $startdt . ":00";
      }
      if (strlen($enddt) < 19) {
         $enddt = $enddt . ":00";
      }

      /*echo "Event ID: " . $eventid . "<br>";
      echo "Event name entered: " . $name . "<br>";
      echo "Location entered: " . $location . "<br>";
      echo "Address entered: " . $fullAddress . "<br>";
      echo "date and time entered: " . $dateandtime . "<br>";
      echo "description entered: " . $description . "<br>";
      echo "image entered: " . $image . "<br>";
      echo "link1 entered: " . $link1 . "<br>";
      echo "link2 entered: " . $link2 . "<br>";
      echo "link3 entered: " . $link3 . "<br>";
      echo "Pin entered: " . $pin . "<br>";*/

      $result = $mysqli->query("UPDATE ihc_events SET name='$name', location='$location', address='$fullAddress', startdt='$startdt', enddt='$enddt', description='$description', image='$image', link1='$link1', link2='$link2', link3='$link3', pin='$pin' WHERE eventid='$eventid'");

      if ($result == True) {
         $message = "Event has been updated!";
      }
      else {
         $message = "Error updating event!";
      }
      echo "<script type='text/javascript'>alert('$message');</script>";
      $url = "../manage_events.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   $mysqli->close();
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>
