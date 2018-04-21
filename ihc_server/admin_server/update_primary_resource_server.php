<?php

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

   $id = $title = $description = $link = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $id = $_POST["id"];
      $title = $_POST["title"];
      $description = $_POST["description"];
      $link = $_POST["link"];

      $stmt = $mysqli->prepare("UPDATE ihc_resource_info SET title=?, description=?, link=? WHERE id=?");
      $stmt->bind_param('sssi', $title, $description, $link, $id);
      $stmt->execute();

      if ($stmt->error == "") {
         $message = "Resource has been updated!";
      }
      else {
         $message = "Error updating resource!"; # error updating resource in database
      }
      $url = "../manage_primary_resources.php";
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
   }

   $mysqli->close();

?>
