<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $id = $_GET['id'];
  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey WHERE id=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $question = $result->fetch_assoc();
  }
  ?>

  <html>
  <head>
    <title>Edit Survey Question - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var questionField = document.forms["questionForm"]["question"].value;
      var choicesField = document.forms["questionForm"]["choices"].value;
      if (questionField == null || questionField == "" ||
          choicesField == null || choicesField == "") {
            alert("Please fill both fields before submitting!");
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
      <left class="sectionheader"><h1>Edit Survey Question</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="questionForm" onsubmit="return validateForm()" action="./admin_server/update_survey_server.php" method="post">
        <div class="elem" style="display: none">
          ID: <input class="inputbox" type="text" name="id" value="<?php echo $question['id']; ?>" readonly><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Question: <textarea class="inputbox" rows="4" cols="70" name="question"><?php echo $question['question']; ?></textarea><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Choices: <textarea class="inputbox" rows="4" cols="70" name="choices"><?php echo $question['choices']; ?></textarea><br>
          <span class="requirednote">Please separate answer choices with a comma, and list them in order of decreasing value.</span><br><br>
        </div>
        <input class="ui green button" type="submit" value="Update Survey Question">
      </form>
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
