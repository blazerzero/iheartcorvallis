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
       $message = "Unable to connect to the resource marker database!";
       echo "<script type='text/javascript'>alert('$message');</script>";
       $url = "../add_marker.php";
       echo "<script type='text/javascript'>document.location.href = '$url';</script>";
       exit;
   }

   $title = $description = $link = "";
   $resourceids = array();

   if ($_SERVER["REQUEST_METHOD"] == "POST") {

      /* GET VALUES VIA POST */
      $title = $_POST["title"];
      $description = $_POST["description"];
      $link = $_POST["link"];

      $result = $mysqli->query("SELECT id FROM ihc_resource_info");
      while ($row = $result->fetch_assoc()) {
         $resourceids[] = $row;
      }

      $newresourceid = $resourceids[count($resourceids)-1]['id'] + 1;

      /* ADD RESOURCE TO DATABASE */
      $result = $mysqli->query("INSERT INTO ihc_resource_info (id, title, description, link) VALUES ('$newresourceid', '$title', '$description', '$link')");

      $url = "";

      if ($result == True) {
         $message = "Resource has been added to the resource page!";
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../index.php";
      }
      else {
         $message = "Error adding resource to the resource page!"; # error adding prize to database
         echo "<script type='text/javascript'>alert('$message');</script>";
         $url = "../add_primary_resource.php";
      }

      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      mysqli_close($con);
      exit;

   }

   mysqli_close($con);

?>
