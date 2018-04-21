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

   $id = $info = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $info = $_POST["info"];

      $stmt = $mysqli->prepare("UPDATE ihc_about SET info=? WHERE id=?");
      $stmt->bind_param('si', $info, $id);
      $stmt->execute();

      if ($stmt->error == "") {
         $message = "About Page been updated!";
      }
      else {
         $message = "Error updating About Page!"; # error updating prize in database
      }

      $url = "../manage_about.php";
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      exit;

   }

   $mysqli->close();

?>
