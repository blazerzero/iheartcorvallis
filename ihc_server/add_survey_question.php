<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <html>
  <head>
    <title>Add a Prize - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>

    function validateForm() {
      var questionField = document.forms["questionForm"]["question"].value;
      var choicesField = document.forms["questionForm"]["choices"].value;
      if (questionField == null || questionField == "" ||
      choicesField == null || choicesField == "" ||) {
        alert("Please fill all required fields before submitting!");
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
      <left class="sectionheader"><h1>Add Survey Question</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="questionForm" onsubmit="return validateForm()" action="./admin_server/add_survey_server.php" method="post">
        <div class="elem">
          <span class="requirednote">*</span>
          Question: <input class="inputbox" type="text" name="question"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Choices: <input class="inputbox" type="text" name="choices"><br><br>
          <span class="requirednote"><strong>Please separate answer choices with a comma.</strong></span><br><br>
        </div>
        <input class="ui button" type="submit">
      </form>
    </div>
  </body>
  </html>

  <?php
  $mysqli->close();
}
else {
  $url = "./admin_auth.php";
  echo "<script type='text/javascript'>document.location.href = '$url';</script>";
}
?>
