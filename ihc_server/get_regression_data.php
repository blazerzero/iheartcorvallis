<?php

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
  $message = "Unable to connect to the event database!";
  echo "<script type='text/javascript'>alert('$message');</script>";
  header("Location: ../index.html");
  exit;
}

$stat1 = $stat2 = $res = $res1 = $res2 = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $stat1 = $_POST["stat1"];
  $stat2 = $_POST["stat2"];

  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();
  $questions = array();
  while ($question = $result->fetch_assoc()) {
    $questions[] = $question;
  }

  if ($stat1 == 1) {
    if ($stat2 == 5) {
      echo "Here";
      $stmt = $mysqli->prepare("SELECT * FROM ihc_completed_events ORDER BY userid");
      $stmt->execute();
      $res = $stmt->get_result();
      print_r($res->num_rows);
      echo "<br>";
      //print_r($res);
      while ($completedevents = $res->fetch_assoc()) {
        print_r($completedevents);
        echo "<br>";
      }
    }
  }
  else {

  }


?>

<html>
<head>
  <title>Regression Analysis Center - I Heart Corvallis Administrative Suite</title>
  <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
  <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
  <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  <script>
  $(document).ready(function() {
    $("#siteheader").load("siteheader.html");
  });
  </script>
</head>
<body>
  <div class="siteheader" id="siteheader"></div>

  <div class="mainbody">
    <left class="sectionheader"><h1>Regression Analysis Center</h1></left><br>
    <form action="./regression.php">
      <button class="ui red button" href="./regression.php">
        <i class="arrow left icon"></i>
        Back to Selection Prompt
      </button>
    </form>
  </div>

  <div class="ui divider"></div><br>

  <div id="regression_analysis_chart" style="width: 50vw; height: 30vw;"></div>

<?php
}
?>
