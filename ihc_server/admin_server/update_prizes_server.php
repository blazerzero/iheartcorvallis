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
       echo "Connection failed!<br>";
   }
   else {
      echo "Connection succesful!<br>";
   }

   $prizeid = $name = $level = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $prizeid = $_POST["prizeid"];
      $name = $_POST["name"];
      $level = $_POST["level"];

      $result = $mysqli->query("UPDATE ihc_prizes SET name='$name', level='$level' WHERE eventid='$eventid'");

      if ($result == True) {
         $message = "Prize has been updated!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error updating prize!"; # error updating prize in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../manage_prizes.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   $mysqli->close();
}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
