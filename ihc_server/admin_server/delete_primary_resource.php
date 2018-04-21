<?php

require 'db.php';
$id = $_GET['id'];
$picture = $_GET['image'];

$dir = "../images/resources/".$picture;

if (!is_writable($dir)) {
 echo '$dir is not writeable';
}

if(!unlink($dir)) {
 echo 'Error deleting $picture';
}
else {
 echo ('Deleted $picture');
}

$stmt = $mysqli->prepare("DELETE FROM ihc_resource_info WHERE id=");
$stmt->bind_param('i', $id);
$stmt->execute();
if ($stmt->error == "") {
  $message = "Resource has been deleted!";
}
else {
  $message = "Error deleting resource!";
}
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";
$url = "../manage_primary_resources.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";

?>
