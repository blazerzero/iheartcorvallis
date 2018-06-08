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
  echo "Connection failed!<br>";
}

$eventid = $name = $host = $location = $fullAddress = $setdateandtime = $startdate = $starttime = $startdt = $enddate = $endtime = $enddt = $description = $changeimage = $image = $link1 = $link2 = $link3 = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $eventid = $_POST["eventid"];
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
  $changeimage = $_POST["changeimage"];
  $image = $_POST["image"];
  $link1 = $_POST["link1"];
  $link2 = $_POST["link2"];
  $link3 = $_POST["link3"];

  if ($setdateandtime == 0) {   // the event is an "Anytime" event
    $startdt = "1900-01-01 00:00:00";   // arbitrary start date
    $enddt = "2099-12-31 23:59:59";     // arbitrary end date
  }
  else {    // the event has a set date/time range
    $startdt = $startdate . " " . $starttime . ":00";   // add seconds to starting datetime
    $enddt = $enddate . " " . $endtime . ":00";   // add seconds to ending datetime
    if (strlen($startdt) < 19) {    // starting datetime not properly formatted
      $startdt = $startdt . ":00";  // add seconds to starting datetime to make it properly formatted
    }
    if (strlen($enddt) < 19) {    // ending datetime not properly formatted
      $enddt = $enddt . ":00";    // add seconds to ending datetime to make it properly formatted
    }
  }

  if ($changeimage == 0)  {   // the user does not want to change the event image

    /* UPDATE EVNET INFO */
    $stmt = $mysqli->prepare("UPDATE ihc_events SET name=?, host=?, location=?, address=?, startdt=?, enddt=?, description=?, link1=?, link2=?, link3=? WHERE eventid=?");
    $stmt->bind_param('ssssssssssi', $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $link1, $link2, $link3, $eventid);
    $stmt->execute();

    if ($stmt->error == "") {   // successfully updated event info
      $message = "Event has been updated!";
    }
    else {    // error updating event info
      $message = "Error updating event!";
    }
    $url = "../manage_events.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to 4url
    exit;
  }

  else if ($changeimage == 1) {   // the user wants to change the event image
    /* ADD NEW IMAGE TO IMAGES DIRECTORY AND ITS NAME TO THE EVENT TABLE */

    /* GET THE NAME OF THE CURRENT EVENT IMAGE */
    $stmt = $mysqli->prepare("SELECT image FROM ihc_events WHERE eventid=?");
    $stmt->bind_param('i', $eventid);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();

    $eventpicture = $row['image'];
    $dir = "../images/events/".$eventpicture;   // build event image path

    if (!is_writable($dir)) {   // unable to access event image
 	   echo $dir . ' is not writeable';
    }

    /* CHECK IF ANY OTHER EVENTS USE THIS IMAGE */
    $stmt = $mysqli->prepare("SELECT eventid FROM ihc_events WHERE image=?");
    $stmt->bind_param('s', $eventpicture);
    $stmt->execute();
    $res = $stmt->get_result();

    if ($res->num_rows == 1) {    // this is the only event using the image
      if(!unlink($dir)) {   // error deleting the event image
        echo 'Error deleting ' . $eventpicture;
      }
      else {    // successfully deleted the event image
        echo ('Deleted ' . $eventpicture);
      }
    }

    /* STORE THE NEW EVENT IMAGE */
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
      $errors[]='File size must be excately 2 MB';
    }

    if(empty($errors)==true) {
      if(move_uploaded_file($file_tmp, $new_dir)) {
        echo "The file HAS BEEN UPLOADED";
      }
      //echo "Success";
    }else{
      print_r($errors);
    }

    /* UPDATE EVENT INFO */
    $stmt = $mysqli->prepare("UPDATE ihc_events SET name=?, host=?, location=?, address=?, startdt=?, enddt=?, description=?, image=?, link1=?, link2=?, link3=? WHERE eventid=?");
    $stmt->bind_param('sssssssssssi', $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $file_name, $link1, $link2, $link3, $eventid);
    $stmt->execute();

    if ($stmt->error == "") {   // successfully updated event info
      $message = "Event has been updated!";
    }
    else {    // error updating event info
      $message = "Error updating event!";
    }
    $url = "../manage_events.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
    exit;

  }

}

$mysqli->close();

?>
