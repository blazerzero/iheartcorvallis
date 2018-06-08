<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';
$prizeid = $_GET['prizeid'];    // get prize ID via GET

/* DELETE PRIZE */
$stmt = $mysqli->prepare("DELETE FROM ihc_prizes WHERE prizeid=?");
$stmt->bind_param('i', $prizeid);
$stmt->execute();
if ($stmt->error == "") {   // successfully deleted the prize
  $message = "Prize has been deleted!";
}
else {    // error deleting the prize
  $message = "Error deleting prize!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
$url = "../manage_prizes.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url

?>
