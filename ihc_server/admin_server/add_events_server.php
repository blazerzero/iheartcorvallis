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
       echo "Connection failed!<br>";
   }
   else {
      echo "Connection succesful!<br>";
   }

   $name = $location = $date = $time = $dateandtime = $description = $image = $link1 = $link2 = $link3 = $pin = $fullAddress = $addressData = $prepAddress = $latLng = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
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

      echo "Event name entered: " . $name . "<br>";
      echo "Location entered: " . $location . "<br>";
      echo "Address entered: " . $fullAddress . "<br>";
      echo "date and time entered: " . $dateandtime . "<br>";
      echo "description entered: " . $description . "<br>";
      echo "image entered: " . $image . "<br>";
      echo "link1 entered: " . $link1 . "<br>";
      echo "link2 entered: " . $link2 . "<br>";
      echo "link3 entered: " . $link3 . "<br>";
      echo "Pin entered: " . $pin . "<br>";

      $result = $mysqli->query("INSERT INTO ihc_events (name, location, address, latitude, longitude, dateandtime, description, image, link1, link2, link3, pin) VALUES ('$name', '$location', '$fullAddress', '$dateandtime', '$description', '$image', '$link1', '$link2', '$link3', '$pin')");

      if ($result == True) {
         echo "Event has been added!"; # account successfully added to database
      }
      else {
         echo "Error adding event!"; # error adding account to database
      }
   }
   mysqli_close($con);
?>
