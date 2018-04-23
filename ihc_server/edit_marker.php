<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $id = $_GET['id'];
  $stmt = $mysqli->prepare("SELECT * FROM ihc_resources WHERE id=?");
  $stmt->bind_param('i', $id);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $marker = $result->fetch_assoc();
  }
  ?>

  <html>
  <head>
    <title>Edit Map Resource - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    function validateForm() {
      var nameField = document.forms["markerForm"]["name"].value;
      var addressField = document.forms["markerForm"]["address"]
      var typeField = document.forms["markerForm"]["type"].value;
      if (nameField == null || nameField == "" ||
      addressField == null || addressField == "" ||
      levelField == null || levelField == "") {
        alert("Please fill all fields before submitting!");
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
      <left class="sectionheader"><h1>Edit Map Resource</h1></left><br>
      <div class="ui divider"></div><br>

      <p class="requirednote">* Denotes a required field</p><br>
      <form name="markerForm" onsubmit="return validateForm()" action="./admin_server/update_marker_server.php" method="post">
        <div class="elem" style="display: none">
          Marker ID: <input class="inputbox" type="text" name="eventid" value="<?php echo $prizes['eventid']; ?>" readonly><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Name of Location: <input class="inputbox" type="text" name="name" value="<?php echo $marker['name']; ?>"><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Full Address: <textarea class="inputbox" rows="2" cols="50" name="name"><?php echo $marker['address']; ?></textarea><br><br>
        </div>
        <div class="elem">
          <span class="requirednote">*</span>
          Resource Type: <select class="ui search dropdown" name="type">
            <option value="">Choose a resource type</option>
            <option value="1">Activities and Entertainment</option>
            <option value="2">Grocery Stores</option>
            <option value="3">Restaurants</option>
            <option value="4">Shopping</option>
            <option value="5">City Offices</option>
            <option value="6">OSU Campus</option>
          </select>
          <br><br>
        </div>
        <input class="ui green button" type="submit" value="Update Marker">
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
