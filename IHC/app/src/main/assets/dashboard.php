<!DOCTYPE html>
<html>
<head>
   <title>Home</title>
   <link type="text/css" rel="stylesheet" href="./Semantic-UI-CSS-master/semantic.css"/>
   <link type="text/css" rel="stylesheet" href="./stylesheet.css"/>
   <script type="text/javascript" src="../../../../node_modules/jquery/dist/jquery.min.js"></script>
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
      <a class="item sidebar" id="closebutton">
         <left><i class="home icon ihc"></i></left>
         Dashboard
      </a>
      <a class="item sidebar" href="passport.php">
         <left><i class="travel icon ihc"></i></left>
         Passport
      </a>
      <a class="item sidebar" href="events.php">
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
         <br>
         <div class="navbar">
            <br>
            <div>
               <!--<div id="sidebar-button">
                  <i class="sidebar icon ihc"></i>
               </div>-->
               <div id="pagename">
                  <i class="sidebar icon ihc" id="sidebar-button"></i>
                  <center><strong>Dashboard</strong></center>
               </div>
            </div>
            <br>
         </div>
      </div>
      <div class="quicklinks" id="app-body">
         <a class="ui card quick" style="background-color:#c9ae5d" href="events.php">
            <div class="content">
               <div class="header ui large">EVENTS</div>
               <div class="progress">Only 4 events from silver!</div>
               <h1 class="go-to-passport">
                  GO TO EVENT LIST
                  <i class="caret right icon"></i>
               </h1>
            </div>
         </a>
         <div class="ui cards quick">
            <a class="card small quick left" href="passport.php">
               <div class="content">
                  <br><br>
                  <div class="header ui small">PASSPORT</div>
                  <br><br>
               </div>
            </a>
            <a class="card small quick right" href="leaderboard.php">
               <div class="content">
                  <br><br>
                  <div class="header ui small">LEADERBOARD</div>
                  <br><br>
               </div>
            </a>
         </div>
         <a class="ui card quick" href="resources.php">
            <div class="content">
               <br>
               <div class="header ui medium"><center>RESOURCES</center></div>
               <br>
            </div>
         </a>
         <a class="ui card quick" href="about.php">
            <div class="content">
               <br>
               <div class="header ui medium"><center>ABOUT US</center></div>
               <br>
            </div>
         </a>
      </div>
   </div>
</body>
</html>