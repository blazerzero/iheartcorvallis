<!DOCTYPE HTML>
<html>
   <head>
      <title>Add an Event - I Heart Corvallis Administrative Suite</title>
      <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
      <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
      <script>
      $(document).ready(function() {
         $("#pin_generator").click(function() {
            $("#pin_holder").val((Math.floor((Math.random() * 9000) + 1000)).toString());
         });
      });

      function validateForm() {
         var nameField = document.forms["eventForm"]["name"].value;
         var locationField = document.forms["eventForm"]["location"].value;
         var streetAdressField = document.forms["eventForm"]["streetaddress"].value;
         var cityField = document.forms["eventForm"]["city"].value;
         var stateField = document.forms["eventForm"]["state"].value;
         var zipField = document.forms["eventForm"]["zip"].value;
         var dateField = document.forms["eventForm"]["date"].value;
         var timeField = document.forms["eventForm"]["time"].value;
         var descriptionField = document.forms["eventForm"]["description"].value;
         var imageField = document.forms["eventForm"]["image"].value;
         if (nameField == null || nameField == "" ||
            locationField == null || locationField == "" ||
            streetAddressField == null || streetAddressField == "" ||
            cityField == null || cityField == "" ||
            stateField == null || stateField == "" ||
            zipField == null || zipField == "" ||
            dateField == null || dateField == "" ||
            timeField == null || timeField == "" ||
            descriptionField == null || descriptionField == "") {
               alert("Please fill all required fields before submitting!");
               return false;
         }
         else {
            return true;
         }
      }
      </script>
   </head>
   <body>
      <div class="siteheader">
         <br><br>
         <left class="sitenametop">I HEART CORVALLIS</left>
         <br><br>
         <left class="sitenamebottom">Administrative Suite</left>
         <br><br>
      </div>
      <ul class="navbar">
         <div class="ui pointing dropdown link item">
            <a class="text">Home</a>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Events</a>
            <div class="menu">
               <div class="item">Add an Event</div>
               <div class="item">Manage Events</div>
            </div>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Resources</a>
            <div class="menu">
               <div class="item">Manage Primary Resources</div>
               <div class="item">Manage Resource Map</div>
            </div>
         </div>
         <div class="ui pointing dropdown link item">
            <a class="text">Prizes</a>
            <div class="menu">
               <div class="item">Add a Prize</div>
               <div class="item">Manage Prizes</div>
               <div class="item">Manage Prize Thresholds</div>
            </div>
         </div>
      </ul>

      <div class="mainbody">
         <left class="sectionheader">Add an Event</left>
         <br>
         <br><p class="requirednote">* Denotes a required field</p><br>
         <form name="eventForm" onsubmit="return validateForm()" action="../admin_server/add_events_server.php" method="post">
            <div class="elem">
               <span class="requirednote">*</span>Name of Event: <input class="inputbox" type="text" name="name"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Name of Location: <input class="inputbox" type="text" name="location"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Street Address: <input class="inputbox" type="text" name="streetaddress"><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>City: <input class="inputbox" type="text" name="city"><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>State: <input class="inputbox" type="text" name="state"><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>ZIP: <input class="inputbox" type="text" name="zip"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Date: <input class="inputbox" type="date" name="date"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Time: <input class="inputbox" type="time" name="time"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Description of Event: <input class="inputbox" type="text" name="description"><br><br>
            </div>
            <div class="elem">
               <span class="requirednote">*</span>Cover Image: <input class="ui button" type="file" name="image"><br><br>
            </div>
            <div class="elem">
               Link 1: <input class="inputbox" type="text" name="link1"><br><br>
            </div>
            <div class="elem">
               Link 2: <input class="inputbox" type="text" name="link2"><br><br>
            </div>
            <div class="elem">
               Link 3: <input class="inputbox"type="text" name="link3"><br><br>
            </div>
            <div class="elem">
               Event PIN:
               <input class="inputbox" type="text" name="pin" id="pin_holder">
               <button class="ui button" id="pin_generator" type="button">Generate PIN</button><br><br>
            </div>
            <input class="ui button" type="submit">
         </form>
      </div>
   </body>
</html>
