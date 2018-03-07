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

   $id = $info = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $info = $_POST["info"];

      $result = $mysqli->query("UPDATE ihc_about SET info='$info' WHERE id='$id'");

      if ($result == True) {
         $message = "About Page been updated!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error updating About Page!"; # error updating prize in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../manage_about.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   $mysqli->close();
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>
