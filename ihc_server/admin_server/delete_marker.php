<?php

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
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";
$url = "../manage_resource_map.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";

?>
