<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
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
   
   $result = $mysqli->query("DELETE FROM ihc_resource_info WHERE id='$id'");
   if ($result == True) {
      $message = "Resource has been deleted!";
   }
   else {
      $message = "Error deleting resource!";
   }
   $mysqli->close();
   echo "<script type='text/javascript'>alert('$message');</script>";
   $url = "../manage_primary_resources.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>
