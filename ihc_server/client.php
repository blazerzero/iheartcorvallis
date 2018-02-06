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

   $name = $location = $date = $time = $dateandtime = $description = $image = $link1 = $link2 = $link3 = $pin = $addressFields = $fullAddress = $addressData = $prepAddress = $latLng = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $name = $_POST["name"];
      $location = $_POST["location"];
      $date = $_POST["date"];
      $time = $_POST["time"];
      $description = $_POST["description"];
      $image = $_POST["image"];
      $link1 = $_POST["link1"];
      $link2 = $_POST["link2"];
      $link3 = $_POST["link3"];
      $pin = $_POST["pin"];

      $addressFields = array(
         $_POST["streetaddress"],
         $_POST["city"],
         $_POST["state"],
         $_POST["zip"]
      );

      $fullAddress = implode(', ', array_filter($addressFields));
      $addressData = implode(' ', array_filter($addressFields));
      $prepAddress = str_replace(' ', '+', $addressData);
      $geocode = file_get_contents('https://maps.google.com.maps/api/geocode/json?address='.$prepAddr.'&sensor=false&key=AIzaSyBYbpLA_XmLpBF31OVt91u1K3z2pAVyvrM');
      $output = json_decode($geocode);
      $latitude = $output->results[0]->geometry->location->lat;
      $longitude = $output->results[0]->geometry->location->lng;
      // MAKE GEOCODING FUNCTION and set $latLng to the return value of this function

      $dateandtime = $date . " " . $time . ":00";

      echo "Event name entered: " . $name . "<br>";
      echo "Location entered: " . $location . "<br>";
      echo "Address entered: " . $addressData . "<br>";
      echo "Latitude: " . $latitude . "<br>";
      echo "Longitude: " . $longitude . "<br>";
      echo "date and time entered: " . $dateandtime . "<br>";
      echo "description entered: " . $description . "<br>";
      echo "image entered: " . $image . "<br>";
      echo "link1 entered: " . $link1 . "<br>";
      echo "link2 entered: " . $link2 . "<br>";
      echo "link3 entered: " . $link3 . "<br>";
      echo "Pin entered: " . $pin . "<br>";

      # ADD ACCOUNT IF IT DOESN'T ALREADY EXIST
      if ($alreadyExists == False) {
         $result = $mysqli->query("INSERT INTO ihc_events (name, location, address, latitude, longitude, dateandtime, description, image, link1, link2, link3, pin) VALUES ('$name', '$location', '$fullAddress', '$latitude', '$longitude',  '$dateandtime', '$description', '$image', '$link1', '$link2', '$link3', '$pin')");

         if ($result == True) {
            echo "Events are Added!"; # account successfully added to database
         }
         else {
            echo "Adding Error"; # error adding account to database
         }
      }
   }
   mysqli_close($con);
?>
