<!DOCTYPE HTML>

<?php require "./admin_server/login.php"; ?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $host = $_GET['host'];
  $events = array();
  $stmt = $mysqli->prepare("SELECT * FROM ihc_events WHERE host=?");
  $stmt->bind_param('s', $host);
  $stmt->execute();
  $eventres = $stmt->get_result();
  if ($eventres->num_rows > 0) {
    while ($event = $eventres->fetch_assoc()) {
      $events[] = $event;
    }
  }
  $attendees = $studentAttendees = $nonStudentAttendees = array();
  $allRatings = $studentRatings = $nonStudentRatings = array();
  $stmt = $mysqli->prepare("SELECT CE.*, U.type FROM ihc_completed_events CE, ihc_events E, ihc_users U WHERE CE.eventid=E.eventid AND CE.userid=U.id AND E.host=?");
  $stmt->bind_param('s', $host);
  $stmt->execute();
  $completedres = $stmt->get_result();
  if ($completedres->num_rows > 0) {
    while ($listing = $completedres->fetch_assoc()) {
      $attendees[] = $listing;
      $allRatings[] = $listing['rating'];
      if ($listing['type'] < 3) {
        $studentAttendees[] = $listing;
        $studentRatings[] = $listing['rating'];
      }
      else {
        $nonStudentAttendees[] = $listing;
        $nonStudentRatings[] = $listing['rating'];
      }
    }
  }

  $avgAllRating = array_sum($allRatings) / count($allRatings);
  $minAllRating = min($allRatings);
  $maxAllRating = max($allRatings);
  $avgStudentRating = array_sum($studentRatings) / count($studentRatings);
  $minStudentRating = min($studentRatings);
  $maxStudentRating = max($studentRatings);
  $avgNonStudentRating = array_sum($nonStudentRatings) / count($nonStudentRatings);
  $minNonStudentRating = min($nonStudentRatings);
  $maxNonStudentRating = max($nonStudentRatings);

  ?>

  <html>
  <head>
    <title>Organization Summary: <?php echo $host; ?> - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script>
    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    </script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
      var myConfig = {
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
          values: ["1", "2", "3", "4", "5"]
        },
        series:[
            {
              values: [<?php echo count(array_keys($allRatings, 1)); ?>,
                <?php echo count(array_keys($allRatings, 2)); ?>,
                <?php echo count(array_keys($allRatings, 3)); ?>,
                <?php echo count(array_keys($allRatings, 4)); ?>,
                <?php echo count(array_keys($allRatings, 5)); ?>],
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

      myConfig = {
        backgroundColor:'transparent',
        type: "hbar",
        title: {
          text: "Student/Faculty Rating Spead",
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
          values: ["1", "2", "3", "4", "5"]
        },
        series:[
            {
              values: [<?php echo count(array_keys($studentRatings, 1)); ?>,
                <?php echo count(array_keys($studentRatings, 2)); ?>,
                <?php echo count(array_keys($studentRatings, 3)); ?>,
                <?php echo count(array_keys($studentRatings, 4)); ?>,
                <?php echo count(array_keys($studentRatings, 5)); ?>],
              lineColor: "#4747FF",
              backgroundColor: "#4747FF",
              lineWidth: 1,
              marker: {
                backgroundColor: '#4747FF'
              }
            }
        ]
      };

      zingchart.render({
        id : 'student_ratings_columnchart',
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
          text: "Non-Student Rating Spread",
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
          values: ["1", "2", "3", "4", "5"]
        },
        series:[
            {
              values: [<?php echo count(array_keys($nonStudentRatings, 1)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 2)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 3)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 4)); ?>,
                <?php echo count(array_keys($nonStudentRatings, 5)); ?>],
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
        id : 'nonstudent_ratings_columnchart',
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
      <left class="sectionheader"><h1>Organization Summary: <?php echo $host; ?></h1></left><br>
      <div class="ui divider"></div><br>

      <div>
        <h2>General</h2>
        <h4>Number of Events: <?php echo count($events); ?></h4>
        <h4>Total Number of Attendees: <?php echo count($attendees); ?></h4>
        <h4>Number of Student/Faculty Attendees: <?php echo count($studentAttendees); ?></h4>
        <h4>Number of Non-Student Attendees: <?php echo count($nonStudentAttendees); ?></h4>
        <?php if (count($attendees) > 0) { ?>
          <br><h4>Average Rating: <?php echo $avgAllRating; ?></h4>
          <h4>Student/Faculty Rating: <?php echo $avgStudentRating; ?></h4>
          <h4>Non-Student Rating: <?php echo $avgNonStudentRating; ?></h4>
          <table>
            <tr>
              <td><div id="all_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></td>
              <?php if (count($studentAttendees) > 0) { ?>
                <td><div id="student_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
            <?php } ?>
            </tr>
            <?php if (count($nonStudentAttendees) > 0) { ?>
              <tr>
                <td><div id="nonstudent_ratings_columnchart" style="width: 40vw; height: 30vw;"></div></td>
              </tr>
            <?php } ?>
          </table><br>

          <div class="ui divider"></div><br>
          <div>
            <h2>Event Summaries</h2>
            <table class="ui celled padded table">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Location</th>
                  <th>Date and Time</th>
                  <th>Summary</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($events as $event) { ?>
                  <tr>
                    <td><?php echo $event['name'] ?></td>
                    <td><?php echo $event['location']; ?></td>
                    <td>
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
                    </td>
                    <td><a href="summarize_event.php?eventid=<?php echo $event['eventid'] ?>" class="ui green button">View</a></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div>
        <?php } ?>
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
