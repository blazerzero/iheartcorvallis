<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';
$questionid = $_GET['id'];    // get survey question ID via GET

/* DELETE SURVEY question */
$stmt = $mysqli->prepare("DELETE FROM ihc_survey WHERE id=?");
$stmt->bind_param('i', $questionid);
$stmt->execute();
if ($stmt->error == "") {   // successfully deleted survey question
  $message = "Survey question has been deleted!";
}
else {    // error deleting survey question
  $message = "Error deleting survey question!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
$url = "../manage_survey.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
?>
