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

$eventid = $host = $name = $location = $fullAddress = $startdate = $starttime = $startdt = $enddate = $endtime = $enddt = $description = $changeimage = $image = $link1 = $link2 = $link3 = "";

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
  $changeimage = $_POST["changeimage"];
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

  //$file_name = "";

  if ($changeimage == 0)  {
    /* KEEP OLD IMAGE IN IMAGES DIRECTORY AND EVENT TABLE */
    $stmt = $mysqli->prepare("UPDATE ihc_events SET name=?, host=?, location=?, address=?, startdt=?, enddt=?, description=?, link1=?, link2=?, link3=? WHERE eventid=?");
    $stmt->bind_param('ssssssssssi', $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $link1, $link2, $link3, $eventid);
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

  else if ($changeimage == 1) {
    /* ADD NEW IMAGE TO IMAGES DIRECTORY AND ITS NAME TO THE EVENT TABLE */

    $stmt = $mysqli->prepare("SELECT image FROM ihc_events WHERE eventid=?");
    $stmt->bind_param('i', $eventid);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();

    $eventpicture = $row['image'];
    $dir = "../images/events/".$eventpicture;

    if (!is_writable($dir)) {
 	   echo $dir . ' is not writeable';
    }

    $stmt = $mysqli->prepare("SELECT eventid FROM ihc_events WHERE image=?");
    $stmt->bind_param('s', $eventpicture);
    $stmt->execute();
    $res = $stmt->get_result();

    if ($res->num_rows == 1) {
      if(!unlink($dir)) {
        echo 'Error deleting ' . $eventpicture;
      }
      else {
        echo ('Deleted ' . $eventpicture);
      }
    }

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

    $stmt = $mysqli->prepare("UPDATE ihc_events SET name=?, host=?, location=?, address=?, startdt=?, enddt=?, description=?, image=?, link1=?, link2=?, link3=? WHERE eventid=?");
    $stmt->bind_param('sssssssssssi', $name, $host, $location, $fullAddress, $startdt, $enddt, $description, $file_name, $link1, $link2, $link3, $eventid);
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

}

$mysqli->close();

?>
