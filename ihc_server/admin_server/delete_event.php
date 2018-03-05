<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
   require 'db.php';
   $eventid = $_GET['eventid'];
   $result = $mysqli->query("DELETE FROM ihc_events WHERE eventid='$eventid'");
   if ($result == True) {
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
