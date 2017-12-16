<!DOCTYPE html>

<?php include "./onidlogin.php" ?>
<?php include "./reslogin.php" ?>

<?php
if (checkStudentAuth(true) == "") {
   header("Location: ./index.html");
   exit();
}

else {
?>

<html>
<head>
   <title>Events</title>
   <link type="text/css" rel="stylesheet" href="./Semantic-UI-CSS-master/semantic.css"/>
   <link type="text/css" rel="stylesheet" href="./stylesheet.css"/>
   <script type="text/javascript" src="./node_modules/jquery/dist/jquery.min.js"></script>
</head>
<script>

$(document).ready(function() {
   $("#sidebar-button").click(function(e) {
      e.stopPropagation();
      $("#sidebar-menu").width("60%");
   });
   $(".pusher,#closebutton").click(function(e) {
      $('#sidebar-menu').width(0);
   });
});

</script>
<body>
   <div class="ui inverted vertical menu" id="sidebar-menu">
      <a class="item sidebar" id="closebutton">
         <right><i class="remove icon ihc"></i></right>
      </a>
      <a class="item sidebar" href="dashboard.php">
         <left><i class="home icon ihc"></i></left>
         Dashboard
      </a>
      <a class="item sidebar" href="passport.php">
         <left><i class="travel icon ihc"></i></left>
         Passport
      </a>
      <a class="item sidebar" id="closebutton">
         <left><i class="calendar icon ihc"></i></left>
         Events
      </a>
      <a class="item sidebar" href="leaderboard.php">
         <left><i class="ordered list icon ihc"></i></left>
         Leaderboard
      </a>
      <a class="item sidebar" href="resources.php">
         <left><i class="info icon ihc"></i></left>
         Resources
      </a>
      <a class="item sidebar" href="about.php">
         <left><i class="info icon ihc"></i></left>
         About Us
      </a>
      <a class="item sidebar" href="settings.php">
         <left><i class="setting icon ihc"></i></left>
         Settings
      </a>
   </div>
   <div class="pusher">
      <div class="app-header">
         <center class="ihclogo">
            <strong>I</strong>
            <i class="heart icon ihc"></i>
            <strong>CORVALLIS</strong>
         </center>
         <div class="navbar">
            <br><br>
               <div id="pagename">
                  <i class="sidebar icon ihc" id="sidebar-button"></i>
                  <center><strong>Events</strong></center>
               </div>
            <br><br>
         </div>
      </div>
      <div class="eventlist">
         <?php for ($i = 1; $i <= 10; $i++) { ?>
            <div class="ui icon message ihc">
               <?php if ($i <= 3) { ?>
                  <i class="repeat icon ihc"></i>
               <?php } else { ?>
                  <div id="event-date">
                     <center><h1>DEC</h1></center>
                     <center><h1><?php echo $i + 20 ?></h1></center>
                  </div>
               <?php } ?>
               <div class="content" id="event-info">
                  <div class="header" id="event-name">EXAMPLE EVENT #<?php echo $i ?></div>
                  <p id="event-location">Example Location #<?php echo $i ?></p>
               </div>
            </div>
         <?php } ?>
      </div>
   </div>
</body>
</html>

<?php } ?>
