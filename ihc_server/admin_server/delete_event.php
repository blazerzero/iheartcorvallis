<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
   require 'db.php';
   $eventid = $_GET['eventid'];
   $eventpicture = $_GET['image'];

   $dir = "../images/events/".$eventpicture;

   if (!is_writable($dir)) {
	   echo '$dir is not writeable';
   }

   if(!unlink($dir)) {
	   echo 'Error deleting $eventpicture';
   }
   else {
	   echo ('Deleted $eventpicture');
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
   $mysqli->close();
   echo "<script type='text/javascript'>alert('$message');</script>";
   $url = "../manage_events.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";



/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>
