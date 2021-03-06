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
  $message = "Unable to connect to the resource marker database!";
  echo "<script type='text/javascript'>alert('$message');</script>";
  $url = "../add_marker.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
  exit;
}

$title = $description = $image = $link = "";
$resourceids = array();

if ($_SERVER["REQUEST_METHOD"] == "POST") {

  /* GET VALUES VIA POST */
  $title = $_POST["title"];
  $description = $_POST["description"];
  $image = $_POST["image"];
  $link = $_POST["link"];

  /* ADD IMAGE TO IMAGES DIRECTORY */

  $errors= array();
  $file_name = $_FILES['image']['name'];
  $file_size = $_FILES['image']['size'];
  $file_tmp = $_FILES['image']['tmp_name'];
  $file_type = $_FILES['image']['type'];
  $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));

  $expensions= array("jpeg","jpg","png");

  $new_dir = "../images/resources/".$file_name;

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
  }else{
    print_r($errors);
  }

  /* ADD RESOURCE TO DATABASE */
  $stmt = $mysqli->prepare("INSERT INTO ihc_resource_info (title, description, image, link) VALUES (?, ?, ?, ?)");
  $stmt->bind_param('ssss', $title, $description, $file_name, $link);
  $stmt->execute();

  $url = "";

  if ($stmt->error == "") {   // successfully added resource to database
    $message = "Resource has been added to the resource page!";
    $url = "../index.php";
  }
  else {    // error adding resource to database
    $message = "Error adding resource to the resource page!";
    echo "<br>" . $stmt->error;
    $url = "../add_primary_resource.php";
  }

  $stmt->close();
  $mysqli->close();
  echo "<script type='text/javascript'>alert('$message');</script>";    // show alert with message
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";    // redirect user to $url
  exit;

}

$mysqli->close();

?>
