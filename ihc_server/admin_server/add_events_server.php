<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$alreadyExists = False;

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
  $message = "Unable to connect to the event database!";
  echo "<script type='text/javascript'>alert('$message');</script>";
  header("Location: ../index.html");
  exit;
}

$name = $host = $location = $fullAddress = $setdateandtime = $startdate = $starttime = $startdt = $enddate = $endtime = $enddt = $description = $image = $link1 = $link2 = $link3 = $pin = $row = "";

$events = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $name = $_POST["name"];
  $host = $_POST["host"];
  $location = $_POST["location"];
  $fullAddress = $_POST["fulladdress"];
  $setdateandtime = $_POST["setdateandtime"];
  $startdate = $_POST["startdate"];
  $starttime = $_POST["starttime"];
  $enddate = $_POST["enddate"];
  $endtime = $_POST["endtime"];
  $description = $_POST["description"];
  $image = $_POST["image"];
  $link1 = $_POST["link1"];
  $link2 = $_POST["link2"];
  $link3 = $_POST["link3"];
  $pin = $_POST["pin"];

  /* GET EVENT COUNT */
  $result = $mysqli->query("SELECT eventid, totalEventCount FROM ihc_events");
  while ($row = $result->fetch_assoc()) {
    $events[] = $row;
  }

  if ($events[count($events)-1]['totalEventCount'] > $events[count($events)-1]['eventid']) {
    $totalEventCount = $events[count($events)-1]['totalEventCount'] + 1;
  }
  else {
    $totalEventCount = $events[count($events)-1]['eventid'] + 1;
  }

  /* ADD IMAGE TO IMAGES DIRECTORY */

  $errors= array();
  $file_name = $_FILES['image']['name'];
  $file_size = $_FILES['image']['size'];
  $file_tmp = $_FILES['image']['tmp_name'];
  $file_type = $_FILES['image']['type'];
  $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

  $expensions= array("jpeg","jpg","png");

  $new_dir = "../images/events/".$file_name;

  if(in_array($file_ext,$expensions)=== false){
    $errors[]="extension not allowed, please choose a JPEG or PNG file.";
  }

  if($file_size > 2097152) {
    $errors[]='File size must not exceed 2 MB';
  }

  if(empty($errors)==true) {
    if(move_uploaded_file($file_tmp, $new_dir)) {
      echo "The file HAS BEEN UPLOADED";
    }
    //echo "Success";
  }else{
    print_r($errors);
  }

  if ($setdateandtime == 0) {
    $startdt = "1900-01-01 00:00:00";
    $enddt = "2099-12-31 23:59:59";
  }
  else {
    $startdt = $startdate . " " . $starttime . ":00";
    $enddt = $enddate . " " . $endtime . ":00";
    if (strlen($startdt) < 19) {
      $startdt = $startdt . ":00";
    }
    if (strlen($enddt) < 19) {
      $enddt = $enddt . ":00";
    }
  }

  /* ADD EVENT TO DATABASE */
  $stmt = $mysqli->prepare("INSERT INTO ihc_events (eventid, name, host, location, address, startdt, enddt, description, image, link1, link2, link3, pin, totalEventCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
  $stmt->bind_param('isssssssssssii', $totalEventCount, $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $file_name, $link1, $link2, $link3, $pin, $totalEventCount);
  $stmt->execute();

  $url = "";

  if ($stmt->error == "") {   // successfully added event to database
    $message = "Event has been added!";
    $stmt2 = $mysqli->prepare("UPDATE ihc_events SET totalEventCount=?");
    $stmt2->bind_param('i', $totalEventCount);
    $stmt2->execute();

    if ($stmt2->error == "") {  // successfully updated total event count in database
      $url = "../index.php";
    }
    else {    // error updating total event count in database
      $message = "Error updating total event count!";
      $url = "../add_event.php";
    }
  }
  else {    // error adding event to database
    $message = "Error adding event!";
    $url = "../add_event.php";
  }

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
  exit;

}

$mysqli->close();

?>
