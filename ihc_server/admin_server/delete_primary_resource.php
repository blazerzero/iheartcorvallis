<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';

/* GET VALUES VIA GET */
$id = $_GET['id'];
$picture = $_GET['image'];

$dir = "../images/resources/".$picture;   // build resource image path

if (!is_writable($dir)) {
 echo $dir . ' is not writeable';   // unable to access resource image path
}

/* CHECK IF THERE ARE ANY OTHER RESOURCES THAT USE THIS IMAGE */
$stmt = $mysqli->prepare("SELECT id FROM ihc_resource_info WHERE image=?");
$stmt->bind_param('s', $picture);
$stmt->execute();
$res = $stmt->get_result();
if ($res->num_rows == 1) {    // this is the only resource using this image
  if(!unlink($dir)) {   // error deleting the resource image
    echo 'Error deleting ' . $picture;
  }
  else {    // successfully deleted the resource image
    echo ('Deleted ' . $picture);
  }
}

/* DELETE THE RESOURCE */
$stmt = $mysqli->prepare("DELETE FROM ihc_resource_info WHERE id=?");
$stmt->bind_param('i', $id);
$stmt->execute();
echo "<br>" . $stmt->error;
if ($stmt->error == "") {   // successfully deleted the resource
  $message = "Resource has been deleted!";
}
else {    // error deleting the resource
  $message = "Error deleting resource!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
$url = "../manage_primary_resources.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url

?>
