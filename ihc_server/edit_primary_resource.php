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
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resource_info WHERE id=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $resource = $result->fetch_assoc();
  }
  ?>

  <html>
  <head>
    <title>Edit Resource - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var titleField = document.forms["resourceForm"]["title"].value;
      var descriptionField = document.forms["resourceForm"]["description"].value;
      var changeImageField = document.forms["resourceForm"]["changeimage"].value;
      var imageField = document.forms["resourceForm"]["image"].value;
      if (titleField == null || titleField == "" ||
        descriptionField == null || descriptionField == "" ||
        changeImageField == null || changeImageField == "") {
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
      <left class="sectionheader"><h1>Edit Resource</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="resourceForm" onsubmit="return validateForm()" action="./admin_server/update_primary_resource_server.php" method="post" enctype="multipart/form-data">
        <div class="elem" style="display: none;">
          Resource ID: <input class="inputbox" type="text" name="id" value="<?php echo $resource['id']; ?>"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Resource Title: <input class="inputbox" type="text" name="title" value="<?php echo $resource['title']; ?>"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Description: <textarea class="inputbox" rows="4" cols="50" name="description"><?php echo $resource['description']; ?></textarea><br><br>
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
          Cover Image: <input class="ui button" type="file" name="image" value="<?php echo $resource['image']; ?>"><br><br>
        </div>
        <div class="elem">
          Link: <input class="inputbox" type="text" name="link" value="<?php echo $resource['link']; ?>"><br><br>
        </div>
        <input class="ui green button" type="submit" value="Update Resource">
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
