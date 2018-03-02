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

   $prizeid = $name = $type = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $name = $_POST["name"];
      $address = $_POST["address"];
      $type = $_POST["type"];

      $result = $mysqli->query("UPDATE ihc_resources SET name='$name', address='$address', type='$type' WHERE id='$id'");

      if ($result == True) {
         $message = "Resource marker has been updated!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error updating resource marker!"; # error updating prize in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../manage_resource_map.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   mysqli_close($con);
?>
