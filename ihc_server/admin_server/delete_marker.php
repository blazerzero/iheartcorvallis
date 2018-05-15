<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';
$id = $_GET['id'];
$stmt= $mysqli->prepare("DELETE FROM ihc_resources WHERE id=?");
$stmt->bind_param('i', $id);
$stmt->execute();
if ($stmt->error == "") {
  $message = "Marker has been deleted!";
}
else {
  $message = "Error deleting marker!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";
$url = "../manage_resource_map.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";

?>
