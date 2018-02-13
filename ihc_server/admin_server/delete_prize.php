<?php
require 'db.php';
$prizeid = $_GET['prizeid'];
$result = $mysqli->query("DELETE FROM ihc_prizes WHERE prizeid='$prizeid'");
if ($result == True) {
   $message = "Prize has been deleted!";
}
else {
   $message = "Error deleting prize!";
}
echo "<script type='text/javascript'>alert('$message');</script>";
$url = "../manage_prizes.php";
echo "<script type='text/javascript'>document.location.href = '$url';</script>";
?>
