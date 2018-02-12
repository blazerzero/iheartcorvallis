<?php
require 'db.php';
$eventid = $_GET['eventid'];
$result = $mysqli->query("DELETE FROM ihc_events WHERE eventid='$eventid'");
if ($result == True) {
   echo "Event has been deleted!";
}
else {
   echo "Error deleting event!";
}
/*$sql = 'DELETE FROM ihc_events WHERE eventid=:id';
$statement = $connection->prepare($sql);
if ($statement->execute([':id' => $id])) {
  header("Location: /");
}*/
?>
