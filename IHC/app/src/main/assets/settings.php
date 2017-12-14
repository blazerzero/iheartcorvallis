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
      <a class="item" id="closebutton">
         <right><i class="remove icon"></i></right>
      </a>
      <a class="item sidebar" href="dashboard.php">
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
      <a class="item sidebar" id="closebutton">
         <left><i class="setting icon ihc"></i></left>
         Settings
      </a>
   </div>
   <div class="pusher">
      <div class="app-header">
         <div class="ihclogo">
            <center>
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
                     <center><strong>Settings</strong></center>
                  </div>
               </div>
               <br>
            </div>
         </div>
      </div>

   </div>
</body>
</html>
.php
