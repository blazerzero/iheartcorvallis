<?php

//require "./login.php";

//if (isset($_SESSION["id"]) && $_SESSION["id"] != null) {
   $dbhost="oniddb.cws.oregonstate.edu";
   $dbname="habibelo-db";
   $dbuser="habibelo-db";
   $dbpass="RcAbWdWDkpj7XNTL";

   $alreadyExists = False;

   $mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
   //Output any connection error
   if ($mysqli->connect_error) {
       die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
       echo "Connection failed!<br>";
   }
   else {
      echo "Connection succesful!<br>";
   }

   $id = $title = $description = $link = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $title = $_POST["title"];
      $description = $_POST["description"];
      $link = $_POST["link"];

      $result = $mysqli->query("UPDATE ihc_resource_info SET title='$title', description='$description', link='$link' WHERE id='$id'");

      if ($result == True) {
         $message = "Resource has been updated!";
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      else {
         $message = "Error updating resource!"; # error updating resource in database
         echo "<script type='text/javascript'>alert('$message');</script>";
      }
      $url = "../manage_primary_resources.php";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }
   mysqli_close($con);
/*}
else {
   $url = "../admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}*/
?>
