<?php
require 'db.php';
$eventid = $_GET['eventid'];
$result = $mysqli->query("DELETE FROM ihc_events WHERE eventid='$eventid'");
if ($result == True) {
   $message = "Event has been deleted!";
}
else {
   $message = "Error deleting event!";
}
echo "<script type='text/javascript'>alert('$message');</script>";
$url = "../manage_events.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";
/*$sql = 'DELETE FROM ihc_events WHERE eventid=:id';
$statement = $connection->prepare($sql);
if ($statement->execute([':id' => $id])) {
  header("Location: /");
}*/
?>
