<!DOCTYPE HTML>

<?php
require "./admin_server/login.php";
ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '2G');
?>

<?php if (isset($_SESSION["id"]) && $_SESSION["id"] != null) { ?>

  <?php
  require './admin_server/db.php';
  $allTuples = $studentTuples = $nonStudentTuples = array();
  $sumAllRatings = $sumStudentRatings = $sumNonStudentRatings = 0;
  $avgAllRating = $avgStudentRating = $avgNonStudentRating = 0;
  $numAllZeroes = $numStudentZeroes = $numNonStudentZeroes = 0;

  $stmt = $mysqli->prepare("SELECT U.*, F.dateandtime, F.rating, F.comment FROM ihc_users U, ihc_feedback F WHERE F.userid=U.id");
  $stmt->execute();
  $res = $stmt->get_result();
  while ($row = $res->fetch_assoc()) {
    if ($row['type'] == 0) $row['type'] = "Domestic Student";
    else if ($row['type'] == 1) $row['type'] = "International Student";
    else if ($row['type'] == 2) $row['type'] = "Faculty/Staff";
    else if ($row['type'] == 3) $row['type'] = "Resident";
    else if ($row['type'] == 4) $row['type'] = "Visitor";
    if ($row['grade'] == 0) $row['grade'] = "N/A";
    else if ($row['grade'] == 1) $row['grade'] = "Freshman";
    else if ($row['grade'] == 2) $row['grade'] = "Sophomore";
    else if ($row['grade'] == 3) $row['grade'] = "Junior";
    else if ($row['grade'] == 4) $row['grade'] = "Senior";
    else if ($row['grade'] == 5) $row['grade'] = "Graduate Student";
    else if ($row['grade'] == 6) $row['grade'] = "Doctoral Student";
    else if ($row['grade'] == 7) $row['grade'] = "Faculty/Staff";

    if ($row['rating'] != 0 || $row['comment'] != "") {
      $allTuples[] = $row;
      $sumAllRatings += $row['rating'];
      if ($row['rating'] == 0) $numAllZeroes++;
      if ($row['type'] == "Domestic Student" || $row['type'] == "International Student" || $row['type'] == "Faculty/Staff") {
        $studentTuples[] = $row;
        $sumStudentRatings += $row['rating'];
        if ($row['rating'] == 0) $numStudentZeroes++;
      }
      else {
        $nonStudentTuples[] = $row;
        $sumNonStudentRatings += $row['rating'];
        if ($row['rating'] == 0) $numNonStudentZeroes++;
      }
    }

  }

  $avgAllRating = $sumAllRatings / (count($allTuples) - $numAllZeroes);
  $avgStudentRating = $sumStudentRatings / (count($studentTuples) - $numStudentZeroes);
  $avgNonStudentRating = $sumNonStudentRatings / (count($nonStudentTuples) - $numNonStudentZeroes);
  $avgAllRating = number_format($avgAllRating, 2);
  $avgStudentRating = number_format($avgStudentRating, 2);
  $avgNonStudentRating = number_format($avgNonStudentRating, 2);
  $allRatingCounts = array_count_values(array_column($allTuples, 'rating'));
  $studentRatingCounts = array_count_values(array_column($studentTuples, 'rating'));
  $nonStudentRatingCounts = array_count_values(array_column($nonStudentTuples, 'rating'));
  $allRatings = array_column($allTuples, 'rating');
  for ($i = 1; $i <= 5; $i++) {
    if (!array_key_exists("$i", $allRatingCounts)) $allRatingCounts["$i"] = 0;
    if (!array_key_exists("$i", $studentRatingCounts)) $studentRatingCounts["$i"] = 0;
    if (!array_key_exists("$i", $nonStudentRatingCounts)) $nonStudentRatingCounts["$i"] = 0;
  }

  ?>

  <html>
  <head>
    <title>View App Feedback - I Heart Corvallis Administrative Suite</title>
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
      $("#top_btn").click(function() {
        document.getElementById("feedbackbody").scrollIntoView();
      })
      $("#allusers_btn").click(function() {
        document.getElementById("allusers").scrollIntoView();
      });
      $("#students_faculty_btn").click(function() {
        document.getElementById("students_faculty").scrollIntoView();
      });
      $("#nonstudents_btn").click(function() {
        document.getElementById("nonstudents").scrollIntoView();
      });
      myConfig = {
        backgroundColor:'transparent',
        type: "hbar",
        title: {
          text: "App Rating Spread",
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
        legend:{},
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
              values: [<?php echo $studentRatingCounts['1']; ?>,
                <?php echo $studentRatingCounts['2']; ?>,
                <?php echo $studentRatingCounts['3']; ?>,
                <?php echo $studentRatingCounts['4']; ?>,
                <?php echo $studentRatingCounts['5']; ?>],
              text: "OSU-Affiliated Ratings",
              lineColor: "#FF4747",
              backgroundColor: "#FF4747",
              lineWidth: 1,
              marker: {
                backgroundColor: '#FF4747'
              }
            },
            {
              values: [<?php echo $nonStudentRatingCounts['1']; ?>,
                <?php echo $nonStudentRatingCounts['2']; ?>,
                <?php echo $nonStudentRatingCounts['3']; ?>,
                <?php echo $nonStudentRatingCounts['4']; ?>,
                <?php echo $nonStudentRatingCounts['5']; ?>],
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

    <div class="mainbody" id="mainbody">
      <left class="sectionheader"><h1>View App Feedback</h1></left></br>
      <div class="quicknav">
        <button class="ui orange button ihc" id="top_btn">App Rating</button>
        <button class="ui orange button ihc" id="allusers_btn">All Feedback</button>
        <button class="ui orange button ihc" id="students_faculty_btn">OSU-Affiliated Feedback</button>
        <button class="ui orange button ihc" id="nonstudents_btn">Resident/Visitor Feedback</button>
      </div>

      <div class="ui divider"></div><br>

      <div>
        <h2>App Rating</h2>
        <h4>Overall Average Ratings: <?php echo $avgAllRating; ?></h4>
        <h4>Total Number of Ratings: <?php echo (count($allTuples) - $numAllZeroes); ?></h4><br>
        <h4>Average OSU-Affiliated Rating: <?php echo $avgStudentRating; ?></h4>
        <h4>Number of OSU-Affiliated Ratings: <?php echo (count($studentTuples) - $numStudentZeroes); ?></h4><br>
        <h4>Average Resident/Visitor Rating: <?php echo $avgNonStudentRating; ?></h4>
        <h4>Number of Resident/Visitor Ratings: <?php echo (count($nonStudentTuples) - $numNonStudentZeroes); ?></h4>

        <?php if (count($allTuples) > 0) { ?>

          <!-- APP RATING DISTRIBUTION -->
          <center><div id="all_ratings_columnchart" style="width: 50vw; height: 30vw;"></div></center><br>

          <div class="ui divider"></div><br>

          <div id="allusers">
            <h2>Feedback: All Users</h2>
            <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Date and Time</th>
                  <th>User Type</th>
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($allTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['firstname'] . " " . $tuple['lastname']; ?></td>
                    <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                    <td><?php echo $tuple['type']; ?></td>
                    <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                    <td><?php echo $tuple['comment']; ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div><br>

          <div class="ui divider"></div><br>

          <div id="students_faculty">
            <h2>Feedback: OSU-Affiliated Users</h2>
            <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Student ID #</th>
                  <th>ONID Username</th>
                  <th>Date and Time</th>
                  <th>User Type</th>
                  <th>Class Standing</th>
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($studentTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['firstname'] . " " . $tuple['lastname']; ?></td>
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
          </div><br>

          <div class="ui divider"></div><br>

          <div id="nonstudents">
            <h2>Feedback: Residents/Visitors</h2>
            <table class="ui celled padded table" style="width: 80vw; height: 50vw; display: block; overflow-y:auto; overflow-x:auto">
              <thead>
                <tr>
                  <th class="single line">Name</th>
                  <th>Date and Time</th>
                  <th>User Type</th>
                  <th>Rating</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                <?php foreach ($nonStudentTuples as $tuple) { ?>
                  <tr>
                    <td><?php echo $tuple['firstname'] . " " . $tuple['lastname']; ?></td>
                    <td><?php echo date('M d, Y g:i A', strtotime($tuple['dateandtime'])); ?></td>
                    <td><?php echo $tuple['type']; ?></td>
                    <td><?php if ($tuple['rating'] != 0) echo $tuple['rating']; ?></td>
                    <td><?php echo $tuple['comment']; ?></td>
                  </tr>
                <?php } ?>
              </tbody>
            </table>
          </div>
        <?php } else { ?>
          <h4>No feedback has been received yet.</h4>
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
