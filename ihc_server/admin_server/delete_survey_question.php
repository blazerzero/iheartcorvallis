<?php
   require 'db.php';
   $questionid = $_GET['id'];
   $stmt = $mysqli->prepare("DELETE FROM ihc_survey WHERE id=?");
   $stmt->bind_param('i', $questionid);
   $stmt->execute();
   if ($stmt->error == "") {
      $message = "Survey question has been deleted!";
   }
   else {
      $message = "Error deleting survey question!";
   }
   $mysqli->close();
   echo "<script type='text/javascript'>alert('$message');</script>";
   $url = "../manage_survey.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
?>
