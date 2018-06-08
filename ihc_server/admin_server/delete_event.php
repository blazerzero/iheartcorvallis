<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

require 'db.php';

/* GET VALUES VIA GET */
$eventid = $_GET['eventid'];
$eventpicture = $_GET['image'];

$dir = "../images/events/".$eventpicture;		// build event image path

if (!is_writable($dir)) {
	echo $dir . ' is not writeable';		// unable to access event image path
}

/* CHECK IF THE IMAGE IS USED FOR ANY OTHER EVENTS */
$stmt = $mysqli->prepare("SELECT eventid FROM ihc_events WHERE image=?");
$stmt->bind_param('s', $eventpicture);
$stmt->execute();
$res = $stmt->get_result();
if ($res->num_rows == 1) {		// there is only one event that uses this image
	if(!unlink($dir)) {		// error deleting the image
		echo 'Error deleting ' . $eventpicture;
	}
	else {		// successfully deleted the image
		echo ('Deleted ' . $eventpicture);
	}
}

/* DELETE THE EVENT */
$stmt = $mysqli->prepare("DELETE FROM ihc_events WHERE eventid=?");
$stmt->bind_param('i', $eventid);
$stmt->execute();
if ($stmt->error == "") {		// successfully deleted the event
	$message = "Event has been deleted!";
}
else {		// error deleting the event
	$message = "Error deleting event!";
}

$stmt->close();
$mysqli->close();
echo "<script type='text/javascript'>alert('$message');</script>";		// show alert with message
$url = "../manage_events.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";		// redirect user to $url

?>
