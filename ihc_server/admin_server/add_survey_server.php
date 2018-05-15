<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

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
  $choices = $_POST["choices"];

  $stmt = $mysqli->prepare("INSERT INTO ihc_survey (question, choices) VALUES ('$question', '$choices')");
  $stmt->bind_param('ss', $question, $choices);
  $stmt->execute();

  $url = "";

  if ($stmt->error == "") {
    $message = "Survey question has been added!";
    $url = "../index.php";
  }
  else {
    $message = "Error adding survey question!"; # error adding survey to database
    $url = "../add_survey_question.php";
  }

  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
  exit;

}

$stmt->close();
$mysqli->close();

?>
