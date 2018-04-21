<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $result = $mysqli->query("SELECT * FROM ihc_prizes");
  $ihc_prizes = array();
  $temp = array();
  while ($prize = $result->fetch_assoc()) {
    $ihc_prizes[] = $prize;
  }

  $indexLeft = 0;
  $pointer = 0;
  $i = 0;
  $indexRight = 1;

  // INSERTION SORT TO SORT PRIZES IN DESCENDING ORDER FROM GOLD TO BRONZE
  for ($i = 0; $i < count($ihc_prizes)-1; $i++) {
    while ($indexLeft >= 0
    && (($ihc_prizes[$indexLeft]["level"] == "silver" && $ihc_prizes[$indexRight]["level"] == "gold")
    || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "gold")
    || ($ihc_prizes[$indexLeft]["level"] == "bronze" && $ihc_prizes[$indexRight]["level"] == "silver"))) {
      $temp = $ihc_prizes[$indexLeft];
      $ihc_prizes[$indexLeft] = $ihc_prizes[$indexRight];
      $ihc_prizes[$indexRight] = $temp;
      $indexLeft--;
      $indexRight--;
    }
    $pointer++;
    $indexLeft = $pointer;
    $indexRight = $pointer + 1;
  }
  ?>

  <html>
  <head>
    <title>Manage Prizes - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Manage Prizes</h1></left><br>
      <div class="ui divider"></div><br>

      <table class="ui celled padded table">
        <thead>
          <tr>
            <th class="single line">Name</th>
            <th>Level</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <?php foreach($ihc_prizes as $prize): ?>
            <tr>
              <td><?php echo $prize['name']; ?></td>
              <td><?php echo $prize['level']; ?></td>
              <td>
                <a href="edit_prize.php?prizeid=<?php echo $prize['prizeid'] ?>" class="ui blue button">Edit</a>
                <a onclick="return confirm('Are you sure you want to delete this prize?')" href="./admin_server/delete_prize.php?prizeid=<?php echo $prize['prizeid'] ?>" class='ui red button'>Delete</a>
              </td>
            </tr>
          <?php endforeach; ?>
        </tbody>
      </table>
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
