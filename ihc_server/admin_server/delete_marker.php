<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';
$id = $_GET['id'];    // get marker id via GET

/* DELETE RESOURCE MARKER */
$stmt= $mysqli->prepare("DELETE FROM ihc_resources WHERE id=?");
$stmt->bind_param('i', $id);
$stmt->execute();
if ($stmt->error == "") {   // successfully deleted resource marker
  $message = "Marker has been deleted!";
}
else {    // error deleting resource marker
  $message = "Error deleting marker!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
$url = "../manage_resource_map.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url

?>
