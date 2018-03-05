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
       $message = "Unable to connect to the resource marker database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       $url = "../add_marker.php";
       echo "<script type='text/javascript'>document.location.href = '$url';</script>";
       exit;
   }

   $name = $address = $typeVal = $type = "";
   $prizeids = array();

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $name = $_POST["name"];
      $address = $_POST["address"];
      $typeVal = $_POST["type"];

      if ($typeVal == "1") {
         $type = "Activities and Entertainment";
      }
      else if ($typeVal == "2") {
         $type = "Grocery Stores";
      }
      else if ($typeVal == "3") {
         $type = "Restaurants";
      }
      else if ($typeVal == "4") {
         $type = "Shopping";
      }
      else if ($typeVal == "5") {
         $type = "City Offices";
      }
      else if ($typeVal == "6") {
         $type = "OSU Campus";
      }

      $result = $mysqli->query("SELECT id FROM ihc_resources");
      while ($row = $result->fetch_assoc()) {
         $markerids[] = $row;
      }

      $newmarkerid = $markerids[count($markerids)-1]['id'] + 1;

      /* ADD EVENT TO DATABASE */
      $result = $mysqli->query("INSERT INTO ihc_resources (id, name, address, type) VALUES ('$newmarkerid', '$name', '$address', '$type')");

      $url = "";

      if ($result == True) {
         $message = "Resource has been added to the map!";
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../index.php";
      }
      else {
         $message = "Error adding resource to the map!"; # error adding prize to database
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../add_marker.php";
      }

      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      mysqli_close($con);
      exit;

   }

   mysqli_close($con);

?>
