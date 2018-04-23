<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $prizeid = $_GET['prizeid'];
  $stmt = $mysqli->prepare("SELECT * FROM ihc_prizes WHERE prizeid=?");
  $stmt->bind_param('i', $prizeid);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $prize = $result->fetch_assoc();
  }
  ?>

  <html>
  <head>
    <title>Edit Prize - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var nameField = document.forms["prizeForm"]["name"].value;
      var levelField = document.forms["prizeForm"]["level"].value;
      if (nameField == null || nameField == "" ||
      levelField == null || levelField == "") {
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
      <left class="sectionheader"><h1>Edit Prize</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="prizeForm" onsubmit="return validateForm()" action="./admin_server/update_prizes_server.php" method="post">
        <div class="elem" style="display: none">
          Prize ID: <input class="inputbox" type="text" name="prizeid" value="<?php echo $prize['prizeid']; ?>" readonly><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Name of Prize: <input class="inputbox" type="text" name="name" value="<?php echo $prize['name']; ?>"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Prize Level: <select class="ui search dropdown" name="level">
            <option value="">Choose a level</option>
            <option value="1">Gold</option>
            <option value="2">Silver</option>
            <option value="3">Bronze</option>
          </select>
          <br><br>
        </div>
        <input class="ui green button" type="submit" value="Update Prize">
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
