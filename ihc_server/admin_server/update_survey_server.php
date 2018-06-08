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
  echo "Connection failed!<br>";
}

$prizeid = $name = $type = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $id = $_POST["id"];
  $question = $_POST["question"];
  $choices = $_POST["choices"];

  /* UPDATE THE SURVEY question */
  $stmt = $mysqli->prepare("UPDATE ihc_survey SET question=?, choices=? WHERE id=?");
  $stmt->bind_param('ssi', $question, $choices, $id);
  $stmt->execute();

  if ($stmt->error == "") {   // successfully updated the survey question
    $message = "Survey question has been updated!";
  }
  else {    // error updating the survey question
    $message = "Error updating survey question!";
  }
  $url = "../manage_survey.php";

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
}
$mysqli->close();

?>
