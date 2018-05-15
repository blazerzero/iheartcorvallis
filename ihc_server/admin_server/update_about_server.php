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

$id = $info = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $id = $_POST["id"];
  $info = $_POST["info"];
  $changeimage = $_POST["changeimage"];
  $image = $_POST["image"];

  if ($changeimage == 0) {
    $stmt = $mysqli->prepare("UPDATE ihc_about SET info=? WHERE id=?");
    $stmt->bind_param('si', $info, $id);
    $stmt->execute();

    if ($stmt->error == "") {
      $message = "The About Page content has been updated!";
    }
    else {
      echo $stmt->error . "<br>";
      $message = "Error updating the About Page content!"; # error updating prize in database
    }

    $url = "../manage_about.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";
    exit;
  }
  else if ($changeimage == 1) {
    $stmt = $mysqli->prepare("SELECT image FROM ihc_about WHERE id=?");
    $stmt->bind_param('i', $id);
    $stmt->execute();
    $res = $stmt->get_result();
    $row = $res->fetch_assoc();

    $picture = $row['image'];
    $dir = "../images/about/".$picture;

    if (!is_writable($dir)) {
 	   echo $dir . ' is not writeable';
    }

    $stmt = $mysqli->prepare("SELECT id FROM ihc_about WHERE image=?");
    $stmt->bind_param('s', $picture);
    $stmt->execute();
    $res = $stmt->get_result();

    if ($res->num_rows == 1) {
      if(!unlink($dir)) {
        echo 'Error deleting ' . $picture;
      }
      else {
        echo ('Deleted ' . $picture);
      }
    }

    $errors= array();
    $file_name = $_FILES['image']['name'];
    $file_size = $_FILES['image']['size'];
    $file_tmp = $_FILES['image']['tmp_name'];
    $file_type = $_FILES['image']['type'];
    $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

    $expensions= array("jpeg","jpg","png");

    $new_dir = "../images/about/".$file_name;

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

    $stmt = $mysqli->prepare("UPDATE ihc_about SET info=?, image=? WHERE id=?");
    $stmt->bind_param('ssi', $info, $file_name, $id);
    $stmt->execute();

    if ($stmt->error == "") {
      $message = "The About Page content has been updated!";
    }
    else {
      echo $stmt->error . "<br>";
      $message = "Error updating the About Page content!"; # error updating prize in database
    }

    $url = "../manage_about.php";

    $stmt->close();
    $mysqli->close();
    echo "<script type='text/javascript'>alert('$message');</script>";
    echo "<script type='text/javascript'>document.location.href = '$url';</script>";
    exit;
  }

}

$mysqli->close();

?>
