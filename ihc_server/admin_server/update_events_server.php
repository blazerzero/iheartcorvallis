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

   $eventid = $name = $location = $date = $time = $dateandtime = $description = $image = $link1 = $link2 = $link3 = $pin = $fullAddress = $addressData = $prepAddress = $latLng = $row = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $eventid = $_POST["eventid"];
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

      $dateandtime = $date . " " . $time;
      if (strlen($dateandtime) < 19) {
         $dateandtime = $dateandtime . ":00";
      }

      echo "Event ID: " . $eventid . "<br>";
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

      $result = $mysqli->query("UPDATE ihc_events SET name='$name', location='$location', address='$fullAddress', dateandtime='$dateandtime', description='$description', image='$image', link1='$link1', link2='$link2', link3='$link3', pin='$pin' WHERE eventid='$eventid'");

      if ($result == True) {
         echo "Events are Added!"; # account successfully added to database
      }
      else {
         echo "Adding Error"; # error adding account to database
      }
   }
   mysqli_close($con);

   /*function geocode($address) {
      $address = urlencode($address);
      $url = "http://maps.google.com/maps/api/geocode/json?sensor=false&address={$address}";
      $resp_json = file_get_contents($url);
      $resp = json_decode($resp_json, true);
      if ($resp['status'] = "OK") {
         $lat = $resp['results'][0]['geometry']['location']['lat'];
         $lng = $resp['results'][0]['geometry']['location']['lng'];
         $formatted_address = $resp['results'][0]['formatted_address'];

         if ($lat && $lng && $formatted_address) {
            $data_arr = array();
            array_push($data_arr, $lat, $lng, $formatted_address);
            return $data_arr;
         }
         else {
            return false;
         }
      }
      else {
         return false;
      }
   }*/
?>
