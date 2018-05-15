<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

<?php
require './admin_server/db.php';
$id = $_GET['id'];
$allResponses = array();
$studentResponses = array();
$questionrow = $question = $choicesStr = "";
$choices = array();
//$questionres = $mysqli->query("SELECT * FROM ihc_survey WHERE id='$id'");
$stmt = $mysqli->prepare("SELECT * FROM ihc_survey WHERE id=?");
$stmt->bind_param('i', $id);
$stmt->execute();
$questionres = $stmt->get_result();
if ($questionres->num_rows > 0) {
   $questionrow = $questionres->fetch_assoc();
   $question = $questionrow['question'];
   $choicesStr = $questionrow['choices'];
   $token = strtok($choicesStr, ",");
   while ($token !== false) {
      $choices[] = $token;
      $token = strtok(",");
   }
}
//$surveyres = $mysqli->query("SELECT * FROM ihc_survey_responses WHERE questionid='$id'");
$stmt = $mysqli->prepare("SELECT * FROM ihc_survey_responses WHERE questionid=?");
$stmt->bind_param('i', $id);
$stmt->execute();
$surveyres = $stmt->get_result();
if ($surveyres->num_rows > 0) {
   while ($row = $surveyres->fetch_assoc()) {
      $allResponses[] = $row;
      $userid = $row['userid'];
      //$userres = $mysqli->query("SELECT * FROM ihc_users WHERE id='$userid'");
      $stmt = $mysqli->prepate("SELECT * FROM ihc_users WHERE id=?");
      $stmt->bind_param('i', $userid);
      $stmt->execute();
      $userres = $stmt->get_result();
      if ($userres->num_rows > 0) {
         $user = $userres->fetch_assoc();
         if ((int)$user['type'] < 2) {
            $studentResponses[] = $row;
         }
      }
   }
}

?>

<html>
   <head>
      <title>View Question Responses: <?php echo $event['name']; ?> - I Heart Corvallis Administrative Suite</title>
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
         <left class="sectionheader"><h1>View Question Responses</h1></left><br>
         <div class="ui divider"></div><br>

         <div>
            <h2>General</h2>
            <h4>Question: <?php echo $question; ?></h4>
            <h4>Choices:
               <?php
               for($i = 0; $i < count($choices) - 1; $i++) echo $choices[$i] . ", ";
               echo $choices[count($choices) - 1];
               ?>
            </h4>
            <h4>Number of Responses: <?php echo count($allResponses); ?></h4>
         </div>

      </div>
   </body>
</html>

<?php }
else {
   $url = "./admin_auth.php";
   echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
