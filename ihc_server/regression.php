<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();
  $questions = array();
  while ($question = $result->fetch_assoc()) {
    $questions[] = $question;
  }
  ?>

  <?php
  if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $stat1 = $_POST["stat1"];
    $stat2 = $_POST["stat2"];

    $res = "";
    if ($stat1 == 1) {
      if ($stat2 == 5) {

      }
      for ($i = 1; $i <= count($questions); $i++) {
        if ($stat2 - 4 == $i) {
          $stmt = $mysqli->prepare("SELECT * FROM ihc_survey_responses WHERE questionid=? GROUP BY userid");
          $stmt->bind_param('i', $i);
          $stmt->execute();
          $res = $stmt->get_result();
          break;
        }
      }
      $responses = $res->fetch_assoc();
      print_r($responses);
    }
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
    function validateForm() {
      var stat1Field = document.forms["regressionForm"]["stat1"].value;
      var stat2Field = document.forms["regressionForm"]["stat2"].value;
      if (stat1Field == null || stat1Field == "" ||
          stat2Field == null || stat2Field == "") {
        alert("Please choose two statistics to compare!");
        return false;
      }
      else {
        return true;
      }
    }
    </script>
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
      <div class="ui divider"></div><br>
      <?php $j = 0; ?>
      <form name="regressionForm" onsubmit="return validateForm()" action="./get_regression_data.php" method="post" enctype="multipart/form-data">
        <span style="font-size: 1.25vw"><strong>Compare</strong></span><br><br>
        <select class="ui search dropdown" name="stat1" id="stat1">
          <option value="">Choose statistic</option>
          <option value="1">Change in Time</option>
          <option value="2">Grade</option>
          <option value="3">User Type</option>
          <option value="4">Number of Completed Events</option>
        </select><br><br>
        <span style="font-size: 1.25vw"><strong>to</strong></span><br><br>
        <select class="ui search dropdown" name="stat2" id="stat2">
          <option value="">Choose statistic</option>
          <option value="5">Number of Completed Events</option>
          <?php for ($j = 1; $j <= count($questions); $j++) { ?>
            <option value="<?php echo $j+4; ?>">Responses to Survey Question: <?php echo $questions[$j-1]['question']; ?></option>
          <?php } ?>
        </select><br><br>
        <input class="ui green button" type="submit" value="Create Regression Visual" onclick="createGraph()">
      </form><br>
    </div>
    <!--<script type="text/javascript">
    function createGraph() {
      alert("The submit button works!");
      alert("Stat1: " + document.getElementById("stat1").value);
      alert("Stat2: " + document.getElementById("stat2").value);
      if (document.getElementById("stat1").value == 1) { <?php $stat1 = 1; ?>; }
      else if (document.getElementById("stat1").value == 2) { <?php $stat1 = 2; ?>; }
      else if (document.getElementById("stat1").value == 3) { <?php $stat1 = 3; ?>; }
      else if (document.getElementById("stat1").value == 4) { <?php $stat1 = 4; ?>; }
      if (document.getElementById("stat2").value == 5) { <?php $stat2 = 5; ?>; }
      /*<?php for ($i = 1; $i <= count($questions); $i++) { ?>
        if (document.getElementById("stat2").value == <?php echo $i + 5; ?>) {
          <?php $stat2 = $i + 5; ?>;
          break;
        }
      <?php } ?>*/
      alert("stat1: " + <?php echo $stat1; ?>);
      alert("stat2: " + <?php echo $stat2; ?>);
    }
    </script>-->
  </body>
  </html>

  <?php
  $stmt->close();
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
