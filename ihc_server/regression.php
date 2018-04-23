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
      <form name="regressionForm" onsubmit="return validateForm()" action="./admin_server/get_regression_data.php" method="post" enctype="multipart/form-data">
        <span style="font-size: 1.25vw"><strong>Compare</strong></span><br><br>
        <select class="ui search dropdown" name="stat1">
          <option value="">Choose statistic</option>
          <option value="1">Change in Time</option>
          <option value="2">Grade</option>
          <option value="3">User Type</option>
          <option value="4">Number of Completed Events</option>
        </select><br><br>
        <span style="font-size: 1.25vw"><strong>to</strong></span><br><br>
        <select class="ui search dropdown" name="stat2">
          <option value="">Choose statistic</option>
          <option value="1">Number of Completed Events</option>
          <?php for ($j = 1; $j <= count($questions); $j++) { ?>
            <option value="<?php echo $j; ?>">Responses to Survey Question: <?php echo $questions[$j-1]['question']; ?></option>
          <?php } ?>
        </select><br><br>
        <input class="ui green button" type="submit" value="Create Regression Visual">
      </form><br>

      <div class="ui divider"></div><br>

      <div id="regression_analysis_chart" style="width: 50vw; height: 30vw;"></div>

    </div>
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
