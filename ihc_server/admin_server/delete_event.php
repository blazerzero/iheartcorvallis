<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

   require 'db.php';
   $eventid = $_GET['eventid'];
   $eventpicture = $_GET['image'];

   $dir = "../images/events/".$eventpicture;

   if (!is_writable($dir)) {
	   echo $dir . ' is not writeable';
   }

	 $stmt = $mysqli->prepare("SELECT eventid FROM ihc_events WHERE image=?");
	 $stmt->bind_param('s', $eventpicture);
	 $stmt->execute();
	 $res = $stmt->get_result();
	 if ($res->num_rows == 1) {
	   if(!unlink($dir)) {
		   echo 'Error deleting ' . $eventpicture;
	   }
	   else {
		   echo ('Deleted ' . $eventpicture);
	   }
	 }

   $stmt = $mysqli->prepare("DELETE FROM ihc_events WHERE eventid=?");
	 $stmt->bind_param('i', $eventid);
	 $stmt->execute();
   if ($stmt->error == "") {
      $message = "Event has been deleted!";
   }
   else {
      $message = "Error deleting event!";
   }

	 $stmt->close();
   $mysqli->close();
   echo "<script type='text/javascript'>alert('$message');</script>";
   $url = "../manage_events.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";

?>
