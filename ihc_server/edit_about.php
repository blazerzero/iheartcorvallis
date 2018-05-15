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
  $stmt = $mysqli->prepare("SELECT * FROM ihc_about WHERE id=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $about = $result->fetch_assoc();
  }
  ?>

  <html>
  <head>
    <title>Edit About Page - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    $(document).ready(function() {
      $("#pin_generator").click(function() {
        $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
      });
    });

    function validateForm() {
      var infoField = document.forms["aboutForm"]["info"].value;
      var changeImageField = document.forms["aboutForm"]["changeimage"].value;
      var imageField = document.forms["aboutForm"]["image"].value;
      if (infoField == null || infoField == "" ||
          changeImageField == null || changeImageField == "")  {
        alert("Please fill all required fields before submitting!");
        return false;
      }
      else {
        if (changeImageField == 1) {
          if (imageField == null || imageField == "") {
            alert("Please choose a new cover image!");
            return false;
          }
        }
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
      <left class="sectionheader"><h1>Edit About Page</h1></left><br>
      <div class="ui divider"></div><br>

      <br><p class="requirednote">* Denotes a required field</p><br>
      <form name="aboutForm" onsubmit="return validateForm()" action="./admin_server/update_about_server.php" method="post" enctype="multipart/form-data">
        <div class="elem" style="display: none">
          About ID: <input class="inputbox" type="text" name="id" value="<?php echo $about['id']; ?>" readonly><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Description: <textarea class="inputbox" rows="4" cols="50" name="info"><?php echo $about['info']; ?></textarea><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Do you want to change the cover image? <select class="ui search dropdown" name="changeimage">
            <option value="">Select Yes or No</option>
            <option value="1">Yes</option>
            <option value="0">No</option>
          </select>
          <br><br>
        </div>
        <div class="elem">
           <span class="requirednote">*</span>
           Cover Image: <input class="ui button" type="file" name="image"><br><br>
        </div>
        <input class="ui green button" type="submit" value="Update About Page Contents">
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
