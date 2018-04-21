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
       $message = "Unable to connect to the prize database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       $url = "../add_prize.php";
       echo "<script type='text/javascript'>document.location.href = '$url';</script>";
       exit;
   }

   $name = $level = "";
   $prizeids = array();

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $name = $_POST["name"];
      $levelVal = $_POST["level"];

      if ($levelVal == "1") {
         $level = "gold";
      }
      else if ($levelVal == "2") {
         $level = "silver";
      }
      else if ($levelVal == "3") {
         $level = "bronze";
      }

      $stmt = $mysqli->prepare("INSERT INTO ihc_prizes (name, level) VALUES (?, ?)");
      $stmt->bind_param('ss', $name, $level);
      $stmt->execute();

      $url = "";

      if ($stmt->error == "") {
         $message = "Prize has been added!";
         $url = "../index.php";
      }
      else {
         $message = "Error adding prize!"; # error adding prize to database
         $url = "../add_prize.php";
      }

      $stmt->close();
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      exit;

   }

   $mysqli->close();

?>
