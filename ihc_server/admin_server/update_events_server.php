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

   $eventid = $host = $name = $location = $fullAddress = $startdate = $starttime = $startdt = $enddate = $endtime = $enddt = $description = $image = $link1 = $link2 = $link3 = "";

   if ($_SERVER["REQUEST_METHOD"] == "POST") {
      $eventid = $_POST["eventid"];
      $host = $_POST["host"];
      $name = $_POST["name"];
      $location = $_POST["location"];
      $fullAddress = $_POST["fulladdress"];
      $startdate = $_POST["startdate"];
      $starttime = $_POST["starttime"];
      $enddate = $_POST["enddate"];
      $endtime = $_POST["endtime"];
      $description = $_POST["description"];
      $image = $_POST["image"];
      $link1 = $_POST["link1"];
      $link2 = $_POST["link2"];
      $link3 = $_POST["link3"];

      $startdt = $startdate . " " . $starttime . ":00";
      $enddt = $enddate . " " . $endtime . ":00";
      if (strlen($startdt) < 19) {
         $startdt = $startdt . ":00";
      }
      if (strlen($enddt) < 19) {
         $enddt = $enddt . ":00";
      }

      $stmt = $mysqli->prepare("UPDATE ihc_events SET name=?, host=?, location=?, address=?, startdt=?, enddt=?, description=?, image=?, link1=?, link2=?, link3=? WHERE eventid=?");
      $stmt->bind_param('sssssssssssi', $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $image, $link1, $link2, $link3, $eventid);
      $stmt->execute();

      if ($stmt->error == "") {
         $message = "Event has been updated!";
      }
      else {
         $message = "Error updating event!";
      }
      $url = "../manage_events.php";
      
      $stmt->close();
      $mysqli->close();
      echo "<script type='text/javascript'>alert('$message');</script>";
      echo "<script type='text/javascript'>document.location.href = '$url';</script>";
      exit;
   }

   $mysqli->close();

?>
