<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <html>
  <head>
    <title>Add to Resource Page - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var titleField = document.forms["resourceForm"]["title"].value;
      var descriptionField = document.forms["resourceForm"]["description"].value;
      var imageField = document.forms["resourceForm"]["image"].value;
      if (titleField == null || titleField == "" ||
        descriptionField == null || descriptionField == "" ||
        imageField == null || imageField == "") {
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
      <left class="sectionheader"><h1>Add a Resource to the Resource Page</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="resourceForm" onsubmit="return validateForm()" action="./admin_server/add_primary_resource_server.php" method="post" enctype="multipart/form-data">
        <div class="elem">
          <span class="requirednote">*</span>
          Resource Title: <input class="inputbox" type="text" name="title"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Description: <textarea class="inputbox" rows="4" cols="50" name="description"></textarea><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Cover Image: <input class="ui button" type="file" name="image"><br><br>
        </div>
        <div class="elem">
          Link: <input class="inputbox" type="text" name="link"><br><br>
        </div>
        <input class="ui green button" type="submit" value="Create Resource">
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
