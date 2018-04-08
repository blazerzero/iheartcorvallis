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
       $message = "Unable to connect to the prize database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       $url = "../add_prize.php";
       echo "<script type='text/javascript'>document.location.href = '$url';</script>";
       exit;
   }

   $question = $choices = "";
  

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $question = $_POST["question"];
      $survey = $_POST["survey"];

      $result = $mysqli->query("INSERT INTO ihc_survey (question, choices) VALUES ('$question', '$choices')");


      if ($result == True) {
         $message = "survey has been added!";
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../index.php";
      }
      else {
         $message = "Error adding survey!"; # error adding survey to database
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../add_survey_question.php";
      }

      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      $mysqli->close();

   }

   $mysqli->close();
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/

?>
