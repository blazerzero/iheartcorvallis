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
      //echo "Connection succesful!<br>";
   }

   $prizeid = $name = $type = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $question = $_POST["question"];
      $choices = $_POST["choices"];

      $result = $mysqli->query("UPDATE ihc_survey SET question='$question', choices='$choices' WHERE id='$id'");

      if ($result == True) {
         $message = "Survey question has been updated!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error updating survey question!"; # error updating survey question in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../manage_survey.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   $mysqli->close();

?>
