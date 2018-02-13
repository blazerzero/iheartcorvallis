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

      $result = $mysqli->query("SELECT prizeid FROM ihc_prizes");
      while ($row = $result->fetch_assoc()) {
         $prizeids[] = $row;
      }

      $newprizeid = $prizeids[count($prizeids)-1]['prizeid'] + 1;

      /* ADD EVENT TO DATABASE */
      $result = $mysqli->query("INSERT INTO ihc_prizes (prizeid, name, level) VALUES ('$newprizeid', '$name', '$level')");

      $url = "";

      if ($result == True) {
         $message = "Prize has been added!";
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../index.html";
      }
      else {
         $message = "Error adding prize!"; # error adding prize to database
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../add_prize.php";
      }

      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      mysqli_close($con);
      exit;

   }

   mysqli_close($con);

?>
