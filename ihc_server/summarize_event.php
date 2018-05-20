<!DOCTYPE HTML>

<?php require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '1G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $eventid = $_GET['eventid'];
  $stmt = $mysqli->prepare("SELECT * FROM ihc_events WHERE eventid=?");
  $stmt->bind_param('i', $eventid);
  $stmt->execute();
  $result = $stmt->get_result();
  if ($result->num_rows > 0) {
    $event = $result->fetch_assoc();
  }

  $stmt = $mysqli->prepare("SELECT * FROM ihc_completed_events WHERE eventid=?");
  $stmt->bind_param('i', $eventid);
  $stmt->execute();
  $result = $stmt->get_result();
  $numAttendees = $result->num_rows;

  /* Variable Declarations */
  $numFreshmen = $numSophomores = $numJuniors = $numSeniors = $numGrad = $numDoc = 0;
  $numDomStudents = $numIntlStudents = $numFaculty = $numResidents = $numVisitors = 0;
  $ages = $allAttendees = $students = $nonStudents = $allRatings = $studentRatings = $nonStudentRatings = $comments = $studentComments = $nonStudentComments = array();
  $minAge = $maxAge = $minAllRating = $maxAllRating = $minStudentRating = $maxStudentRating = $minNonStudentRating = $maxNonStudentRating = 0;
  $avgAllRating = $avgStudentRating = $avgNonStudentRating = 0;

  /* Get Tuples from Completed Events for This Event
  and Get the Information of the Users Who Attended This Event */
  while ($row = $result->fetch_assoc()) {
    $userid = $row['userid'];
    $stmt = $mysqli->prepare("SELECT * FROM ihc_users WHERE id=?");
    $stmt->bind_param('i', $userid);
    $stmt->execute();
    $res = $stmt->get_result();
    if ($res->num_rows > 0) {
      $user = $res->fetch_assoc();
      $name = $user['firstname'] . " " . $user['lastname'];
      $allAttendees[] = $user;
      $grade = $user['grade'];
      $today = date("Y-m-d h:i:s");
      $birthdate = date("Y-m-d h:i:s", strtotime($user['birthdate']));
      $userAge = floor($today - $birthdate);
      //echo $today . "//" . $birthdate . "//" . $userAge . "<br>";
      $ages[] = $userAge;
      if ($row['rating'] > 0) {
        $allRatings[] = $row['rating'];
      }

      $usertype = $user['type'];
      $types = array('Domestic Student', 'International Student', 'Faculty/Staff', 'Resident', 'Visitor');
      $grades = array('N/A', 'Freshman', 'Sophomore', 'Junior', 'Senior', 'Graduate Student', 'Doctoral Student', 'Faculty/Staff');
      $typeString = $types[$usertype];
      $gradeString = $grades[$grade];

      if ($row['rating'] != 0) {
        //$comments[] = $row['comment'];
        $comments[] = array("userid" => $user['id'], "dateandtime" => $row['dateandtime'], "name" => $name, "type" => $typeString, "rating" => $row['rating'], "comment" => $row['comment']);
      }

      if ($grade == 1) { $numFreshmen++; }
      else if ($grade == 2) { $numSophomores++; }
      else if ($grade == 3) { $numJuniors++; }
      else if ($grade == 4) { $numSeniors++; }
      else if ($grade == 5) { $numGrad++; }
      else if ($grade == 6) { $numDoc++; }

      if ($usertype < 3) {
        $students[] = $user;
        if ((int)$row['rating'] > 0) {
          $studentRatings[] = $row['rating'];
          $studentComments[] = array("userid" => $user['id'], "dateandtime" => $row['dateandtime'], "name" => $name, "studentid" => $user['studentid'], "onid" => $user['onid'], "grade" => $gradeString, "type" => $typeString, "rating" => $row['rating'], "comment" => $row['comment']);
        }
      }
      else {
        $nonStudents[] = $user;
        if ((int)$row['rating'] > 0) {
          $nonStudentRatings[] = $row['rating'];
          $nonStudentComments[] = array("userid" => $user['id'], "dateandtime" => $row['dateandtime'], "name" => $name, "studentid" => $user['studentid'], "onid" => $user['onid'], "grade" => $gradeString, "type" => $typeString, "rating" => $row['rating'], "comment" => $row['comment']);
        }
      }
      if ($usertype == 0) { $numDomStudents++; }
      else if ($usertype == 1) { $numIntlStudents++; }
      else if ($usertype == 2) { $numFaculty++; }
      else if ($usertype == 3) { $numResidents++; }
      else if ($usertype == 4) { $numVisitors++; }
    }
  }
  $numStudents = $numDomStudents + $numIntlStudents;
  $minAge = min($ages);
  $maxAge = max($ages);
  $minAllRating = min($allRatings);
  $maxAllRating = max($allRatings);
  $minStudentRating = min($studentRatings);
  $maxStudentRating = max($studentRatings);
  $avgAllRating = array_sum($allRatings) / count($allRatings);
  $avgStudentRating = array_sum($studentRatings) / count($studentRatings);
  $avgNonStudentRating = array_sum($nonStudentRatings) / count($nonStudentRatings);

  $avgAllRating = number_format($avgAllRating, 2);
  $avgStudentRating = number_format($avgStudentRating, 2);
  $avgNonStudentRating = number_format($avgNonStudentRating, 2);
  ?>

  <html>
  <head>
    <title>Event Summary: <?php echo $event['name']; ?> - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script>
    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    </script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
      $("#general_btn").click(function() {
        document.getElementById("siteheader").scrollIntoView();
      });
      $("#allusers_btn").click(function() {
        document.getElementById("allusers").scrollIntoView();
      });
      $("#students_faculty_btn").click(function() {
        document.getElementById("students_faculty").scrollIntoView();
      });
      $("#nonstudents_btn").click(function() {
        document.getElementById("nonstudents").scrollIntoView();
      });

      /* ALL ATTENDEES BY TYPE */
      var myConfig = {
        backgroundColor:'transparent',
        type: "ring",
        title: {
          text: "All Attendees By Type",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          slice:'40%',
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        },
          valueBox: [
            {
              type:'all',
              text:'%t',
              placement:'out'
            },
            {
              type:'all',
              text:'%npv%',
              placement:'in'
            }
          ]
        },
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          text:'<br><span style="color:black">%v Users</span>',
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
          borderWidth: 0,
          borderRadius: "0 0 0 10",
          margin: "70 0 10 0"
        },
        scaleR:{
          refAngle:270
        },
        series:[
          {
            text:"OSU-Affiliated",
            values: [<?php echo $numStudents + $numFaculty; ?>],
            lineColor: "#4747FF",
            backgroundColor: "#4747FF",
            lineWidth: 1,
            marker: {
              backgroundColor: '#4747FF'
            }
          },
          {
            text:"Residents and Visitors",
            values: [<?php echo $numResidents + $numVisitors; ?>],
            lineColor: "#FF4747",
            backgroundColor: "#FF4747",
            lineWidth: 1,
            marker: {
              backgroundColor: '#FF4747'
            }
          }
        ]
      };

      zingchart.render({
        id : 'all_attendees_donutchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

      /* STUDENT/FACULTY ATTENDEES BY TYPE */
      myConfig = {
        backgroundColor:'transparent',
        type: "ring",
        title: {
          text: "OSU-Affiliated Attendees By Type",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          slice:'40%',
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        },
          valueBox: [
            {
              type:'all',
              text:'%t',
              placement:'out'
            },
            {
              type:'all',
              text:'%npv%',
              placement:'in'
            }
          ]
        },
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          text:'<br><span style="color:black">%v Users</span>',
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
          borderWidth: 0,
          borderRadius: "0 0 0 10",
          margin: "70 0 10 0"
        },
        scaleR:{
          refAngle:270
        },
        series:[
          {
            text:"Domestic Students",
            values: [<?php echo $numDomStudents; ?>],
            lineColor: "#4747FF",
            backgroundColor: "#4747FF",
            lineWidth: 1,
            marker: {
              backgroundColor: '#4747FF'
            }
          },
          {
            text:"International Students",
            values: [<?php echo $numIntlStudents; ?>],
            lineColor: "#FF4747",
            backgroundColor: "#FF4747",
            lineWidth: 1,
            marker: {
              backgroundColor: '#FF4747'
            }
          },
          {
            text:"Faculty/Staff",
            values: [<?php echo $numFaculty; ?>],
            lineColor: "#00C62F",
            backgroundColor: "#00C62F",
            lineWidth: 1,
            marker: {
              backgroundColor: '#00C62F'
            }
          }
        ]
      };

      zingchart.render({
        id : 'student_attendees_donutchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

      /* NON-STUDENT ATTENDEES BY TYPE */
      myConfig = {
        backgroundColor:'transparent',
        type: "ring",
        title: {
          text: "Non-OSU-Affiliated Attendees By Type",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          slice:'40%',
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        },
          valueBox: [
            {
              type:'all',
              text:'%t',
              placement:'out'
            },
            {
              type:'all',
              text:'%npv%',
              placement:'in'
            }
          ]
        },
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          text:'<br><span style="color:black">%v Users</span>',
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
          borderWidth: 0,
          borderRadius: "0 0 0 10",
          margin: "70 0 10 0"
        },
        scaleR:{
          refAngle:270
        },
        series:[
          {
            text:"Residents",
            values: [<?php echo $numResidents; ?>],
            lineColor: "#4747FF",
            backgroundColor: "#4747FF",
            lineWidth: 1,
            marker: {
              backgroundColor: '#4747FF'
            }
          },
          {
            text:"Visitors",
            values: [<?php echo $numVisitors; ?>],
            lineColor: "#FF4747",
            backgroundColor: "#FF4747",
            lineWidth: 1,
            marker: {
              backgroundColor: '#FF4747'
            }
          }
        ]
      };

      zingchart.render({
        id : 'nonstudent_attendees_donutchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

      /* STUDENT ATTENDEES BY CLASS STANDING */
      myConfig = {
        backgroundColor:'transparent',
        type: "ring",
        title: {
          text: "Student Attendees By Class Standing",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          slice:'40%',
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        },
          valueBox: [
            {
              type:'all',
              text:'%t',
              placement:'out'
            },
            {
              type:'all',
              text:'%npv%',
              placement:'in'
            }
          ]
        },
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          text:'<br><span style="color:black">%v Users</span>',
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
          borderWidth: 0,
          borderRadius: "0 0 0 10",
          margin: "70 0 10 0"
        },
        scaleR:{
          refAngle:270
        },
        series:[
          {
            text:"Freshmen",
            values: [<?php echo $numFreshmen; ?>],
            lineColor: "#4747FF",
            backgroundColor: "#4747FF",
            lineWidth: 1,
            marker: {
              backgroundColor: '#4747FF'
            }
          },
          {
            text:"Sophomores",
            values: [<?php echo $numSophomores; ?>],
            lineColor: "#FF4747",
            backgroundColor: "#FF4747",
            lineWidth: 1,
            marker: {
              backgroundColor: '#FF4747'
            }
          },
          {
            text:"Juniors",
            values: [<?php echo $numJuniors; ?>],
            lineColor: "#00C62F",
            backgroundColor: "#00C62F",
            lineWidth: 1,
            marker: {
              backgroundColor: '#00C62F'
            }
          },
          {
            text:"Seniors",
            values: [<?php echo $numSeniors; ?>],
            lineColor: "#FF8800",
            backgroundColor: "#FF8800",
            lineWidth: 1,
            marker: {
              backgroundColor: '#FF8800'
            }
          },
          {
            text:"Graduate Students",
            values: [<?php echo $numGrad; ?>],
            lineColor: "#9100FF",
            backgroundColor: "#9100FF",
            lineWidth: 1,
            marker: {
              backgroundColor: '#9100FF'
            }
          },
          {
            text:"Doctoral Students",
            values: [<?php echo $numDoc; ?>],
            lineColor: "#C400A2",
            backgroundColor: "#C400A2",
            lineWidth: 1,
            marker: {
              backgroundColor: '#C400A2'
            }
          }
        ]
      };

      zingchart.render({
        id : 'student_status_donutchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

      myConfig = {
        backgroundColor:'transparent',
        type: "bar",
        title: {
          text: "Attendee Age Spread",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        }
        },
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
        },
        scaleX:{
          values:[
            <?php for ($i = $minAge; $i < $maxAge; $i++) { ?>
              "<?php echo $i; ?>",
            <?php } ?>
            "<?php echo $maxAge; ?>"
          ]
        },
        series:[
            {
              values:[
                <?php for ($i = $minAge; $i < $maxAge; $i++) { ?>
                  <?php echo count(array_keys($ages, $i)); ?>,
                <?php } ?>
                <?php echo count(array_keys($ages, $maxAge)); ?>
              ],
              lineColor: "#4747FF",
              backgroundColor: "#4747FF",
              lineWidth: 1,
              marker: {
                backgroundColor: '#4747FF'
              }
            },
        ]
      };

      zingchart.render({
        id : 'attendee_age_columnchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

      myConfig = {
        backgroundColor:'transparent',
        type: "hbar",
        title: {
          text: "Overall Rating Spread",
          fontFamily: 'Lato',
          fontSize: 18,
          padding: "15",
          fontColor: '#000000'
        },
        plot: {
          stacked: true,
          borderWidth:0,
          backgroundColor:'transparent',
          animation:{
 	          effect:2,
 	          sequence:3
 	        }
        },
        legend: {},
        tooltip:{
          fontSize:16,
          anchor:'c',
          x:'50%',
          y:'50%',
          sticky:true,
          backgroundColor:'none',
          borderWidth:0,
          mediaRules:[
            {
              maxWidth:500,
              y:'54%',
            }
          ]
        },
        plotarea: {
          backgroundColor: 'transparent',
        },
        scaleX:{
          values: ["1", "2", "3", "4", "5"]
        },
        series:[
            {
              values: [<?php echo count(array_keys($studentRatings, 1)); ?>,
                <?php echo count(array_keys($studentRatings, 2)); ?>,
                <?php echo count(array_keys($studentRatings, 3)); ?>,
                <?php echo count(array_keys($studentRatings, 4)); ?>,
                <?php echo count(array_keys($studentRatings, 5)); ?>],
              text: "OSU-Affiliated Ratings",
              lineColor: "#FF4747",
              backgroundColor: "#FF4747",
              lineWidth: 1,
              marker: {
                backgroundColor: '#FF4747'
              }
            },
            {
              values: [<?php echo count(array_keys($nonStudentRatings, 1)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 2)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 3)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 4)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 5)); ?>],
              text: "Non-OSU-Affiliated Ratings",
              lineColor: "#00C62F",
              backgroundColor: "#00C62F",
              lineWidth: 1,
              marker: {
                backgroundColor: '#00C62F'
              }
            }
        ]
      };

      zingchart.render({
        id : 'all_ratings_columnchart',
        data: {
          gui:{
            contextMenu:{
              button:{
                visible: true,
                lineColor: "#2D66A4",
                backgroundColor: "#2D66A4"
              },
              gear: {
                alpha: 1,
                backgroundColor: "#2D66A4"
              },
              position: "right",
              backgroundColor:"#306EAA",
              docked: true,
              item:{
                backgroundColor: "#306EAA",
                borderColor:"#306EAA",
                borderWidth: 0,
                fontFamily: "Lato",
                color: "#000000"
              }
            }
          },
          graphset: [myConfig]
        }
      });

    });
    </script>
  </head>
  <body>
    <div class="siteheader" id="siteheader"></div>

    <div class="mainbody">
      <left class="sectionheader"><h1>Event Summary: <?php echo $event['name']; ?></h1></left><br>
      <div class="quicknav">
        <button class="ui orange button ihc" id="general_btn">General</button>
        <button class="ui orange button ihc" id="allusers_btn">All Feedback</button>
        <button class="ui orange button ihc" id="students_faculty_btn">OSU-Affiliated Feedback</button>
        <button class="ui orange button ihc" id="nonstudents_btn">Resident/Visitor Feedback</button>
      </div>
      <div class="ui divider"></div><br>

      <div id="summarybody">
        <h2>Event Information</h2>
        <h4>Location: <?php echo $event['location']; ?></h4>
        <h4>Date and Time:
          <?php
          if ($event['startdt'] == '1900-01-01 00:00:00' && $event['enddt'] == '2099-12-31 23:59:59') {
            echo 'Anytime';
          }
          else {
            $startdt = date('M d, Y g:i A', strtotime($event['startdt']));
            $enddt = date('M d, Y g:i A', strtotime($event['enddt']));
            echo $startdt . " - " . $enddt;
          }
          ?>
        </h4>
      </div><br>
      <div class="ui divider"></div><br>

      <div id="general">
        <h2>General Statistics</h2>
        <h4>Number of Attendees: <?php echo $numAttendees; ?></h4>
        <h4>Number of OSU-Affiliated Attendees: <?php echo $numStudents + $numFaculty; ?></h4>
        <h4>Number of Resident/Visitor Attendees: <?php echo $numResidents + $numVisitors; ?></h4>
        <?php if ($numAttendees > 0) { ?>

          <!-- ALL EVENT AND USER STATS GO IN HERE -->
          <h4>Average Overall Rating: <?php echo $avgAllRating; ?></h4>
          <h4>Average OSU-Affiliated Rating: <?php echo $avgStudentRating; ?></h4>
          <h4>Average Resident/Visitor Rating: <?php echo $avgNonStudentRating; ?></h4>
          <center><table>
            <tr>
              <td><div id="all_attendees_donutchart" style="width: 50vw; height: 50vw;"></div></td>
              <td>
                <h4>All Attendees</h4>
                <table style="width: 100%; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                  <tr>
                    <td>
                      <table class="ui celled padded table">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Email</th>
                          </tr>
                        </thead>
                        <tbody>
                          <?php foreach($allAttendees as $attendee) { ?>
                              <tr>
                                <td>
                                  <span><?php echo $attendee['firstname'] . " " . $attendee['lastname']; ?></span>
                                </td>
                                <td>
                                  <span><?php echo $attendee['email']; ?></span>
                                </tr>
                              </tr>
                          <?php } ?>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table></center><br>

          <div class="ui divider"></div><br>

          <center><table>
            <tr>
              <td><div id="student_attendees_donutchart" style="width: 50vw; height: 50vw;"></div></td>
              <td>
                <h4>OSU-Affiliated Attendees</h4>
                <table style="width: 100%; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                  <tr>
                    <td>
                      <table class="ui celled padded table">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Email</th>
                          </tr>
                        </thead>
                        <tbody>
                          <?php foreach($students as $student) { ?>
                            <tr>
                              <td>
                                <span><?php echo $student['firstname'] . " " . $student['lastname']; ?></span>
                              </td>
                              <td>
                                <span><?php echo $student['email']; ?></span>
                              </td>
                            </tr>
                          <?php } ?>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table></center><br>

          <div class="ui divider"></div><br>

          <center><table>
            <tr>
              <td><div id="nonstudent_attendees_donutchart" style="width: 50vw; height: 50vw;"></div></td>
              <td>
                <h4>Resident/Visitor Attendees</h4>
                <table style="width: 100%; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                  <tr>
                    <td>
                      <table class="ui celled padded table">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Email</th>
                          </tr>
                        </thead>
                        <tbody>
                          <?php foreach($nonStudents as $nonStudent) { ?>
                            <tr>
                              <td>
                                <span><?php echo $nonStudent['firstname'] . " " . $nonStudent['lastname']; ?></span>
                              </td>
                              <td>
                                <span><?php echo $nonStudent['email']; ?></span>
                              </td>
                            </tr>
                          <?php } ?>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table></center><br>

          <div class="ui divider"></div><br>

          <center><table>
            <tr>
              <td><div id="student_status_donutchart" style="width: 50vw; height: 50vw;">  </div></td>
            </tr>
          </table></center><br>

          <div class="ui divider"></div><br>

          <center><table>
            <tr>
              <td><div id="attendee_age_columnchart" style="width: 40vw; height: 40vw;"></div></td>
              <td>
                <div id="all_ratings_columnchart" style="width: 40vw; height: 40vw;"></div>
              </td>
            </tr>
          </table></center>

          <!-- EVENT FEEDBACK -->
            <div class="ui divider"></div><br>

            <div id="allusers">
              <?php if (count($comments) > 0) { ?>
              <h2>Event Feedback: All Feedback</h2>
              <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                <thead>
                  <tr>
                    <th class="single line">Name</th>
                    <th>Date and Time</th>
                    <th>User Type</th>
                    <th>Event Rating</th>
                    <th>Comment</th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach($comments as $tuple) { ?>
                    <tr>
                      <td><?php echo $tuple['name']; ?></td>
                      <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                      <td><?php echo $tuple['type']; ?></td>
                      <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                      <td><?php echo $tuple['comment']; ?></td>
                    </tr>
                  <?php } ?>
                </tbody>
              </table>
            </div><br>
          <?php } ?>

            <div class="ui divider"></div><br>

            <div id="students_faculty">
              <h2>Event Feedback: OSU-Affiliated Feedback</h2>
              <?php if (count($studentComments) > 0) { ?>
              <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                <thead>
                  <tr>
                    <th class="single line">Name</th>
                    <th>Student ID #</th>
                    <th>ONID Username</th>
                    <th>Date and Time</th>
                    <th>User Type</th>
                    <th>Class Standing</th>
                    <th>Event Rating</th>
                    <th>Comment</th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach($studentComments as $tuple) { ?>
                    <tr>
                      <td><?php echo $tuple['name']; ?></td>
                      <td><?php echo $tuple['studentid']; ?></td>
                      <td><?php echo $tuple['onid']; ?></td>
                      <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                      <td><?php echo $tuple['type']; ?></td>
                      <td><?php echo $tuple['grade']; ?></td>
                      <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                      <td><?php echo $tuple['comment']; ?></td>
                    </tr>
                  <?php } ?>
                </tbody>
              </table>
            </div>
          <?php } ?>

            <div class="ui divider"></div><br>

            <div id="nonstudents">
              <?php if (count($nonStudentComments) > 0) { ?>

              <h2>Event Feedback: Non-OSU-Affiliated Feedback</h2>
              <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
                <thead>
                  <tr>
                    <th class="single line">Name</th>
                    <th>Date and Time</th>
                    <th>User Type</th>
                    <th>Event Rating</th>
                    <th>Comment</th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach($nonStudentComments as $tuple) { ?>
                    <tr>
                      <td><?php echo $tuple['name']; ?></td>
                      <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                      <td><?php echo $tuple['type']; ?></td>
                      <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                      <td><?php echo $tuple['comment']; ?></td>
                    </tr>
                  <?php } ?>
                </tbody>
              </table>
            </div>
          <?php } ?>

        <?php } ?>
      </div>

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
