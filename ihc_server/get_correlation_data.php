<!DOCTYPE HTML>

<?php

$dbhost="oniddb.cws.oregonstate.edu";
$dbname="habibelo-db";
$dbuser="habibelo-db";
$dbpass="RcAbWdWDkpj7XNTL";

$mysqli = new mysqli($dbhost,$dbuser,$dbpass,$dbname);
//Output any connection error
if ($mysqli->connect_error) {
  die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
  $message = "Unable to connect to the event database!";
  echo "<script type='text/javascript'>alert('$message');</script>";
  header("Location: ../index.html");
  exit;
}

$stat1 = $stat2 = $res = $res1 = $res2 = $metric1 = $metric2 = $xAxis = $yAxis = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $stat1 = $_POST["stat1"];
  $stat2 = $_POST["stat2"];

  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();
  $questions = array();
  while ($question = $result->fetch_assoc()) {
    $questions[] = $question;
  }

  if ($stat1 == 1) {
    $metric1 = "Change in Time";
    $xAxis = "Days Since First Completed Event";
  }
  else if ($stat1 == 2) $metric1 = $xAxis = "Grade";
  else if ($stat1 == 3) $metric1 = $xAxis = "User Type";
  else if ($stat1 == 4) $metric1 = $xAxis = "Number of Completed Events";
  if ($stat2 == 5) $metric2 = $yAxis = "Number of Completed Events";
  else {
    for ($i = 1; $i <= count($questions); $i++) {
      if ($stat2 == $i + 5) {
        $metric2 = "Responses to Survey Question:<br>" . $questions[$i-1]["question"];
        $yAxis = "Response Value";
      }
    }
  }

  $allData = array();
  $startTimes = array();
  $numDataPerUser = array();
  $choices = array();
  $newChoices = array();
  $xValues = array();

  if ($stat1 == 1) {      // Second variable being tested against time
    if ($stat2 == 5) {    // Time vs. Number of Completed Events
      $stmt = $mysqli->prepare("SELECT * FROM ihc_completed_events ORDER BY userid ASC, dateandtime ASC");
      $stmt->execute();
      $res = $stmt->get_result();
      if ($res->num_rows > 0) {
        while ($row = $res->fetch_assoc()) {
          if (!array_key_exists($row['userid'], $startTimes)) {
            $startTimes[$row['userid']] = $row['dateandtime'];
            $numDataPerUser[$row['userid']] = 0;
          }
          $elapsed = strtotime($row['dateandtime']) - strtotime($startTimes[$row['userid']]);
          $days = $elapsed / 86400;
          $allData[] = array('userid' => $row['userid'], 'x' => $days, 'y' => $numDataPerUser[$row['userid']] + 1);
          $numDataPerUser[$row['userid']]++;
        }
      }
    }
    else {
      for ($i = 1; $i <= count($questions); $i++) {
        if ($stat2 == $i + 5) {

          $choicesStr = $questions[$i-1]['choices'];
          $choiceVal = 0;
          $token = strtok($choicesStr, ",");
          while ($token !== false) {
            $choices[] = $token;
            $token = strtok(",");
          }
          for ($j = count($choices)-1; $j >= 0; $j--) {
            $newChoices[] = $choices[$j];
          }
          $choices = $newChoices;

          $stmt = $mysqli->prepare("SELECT * FROM ihc_survey_responses WHERE questionid=? ORDER BY userid ASC, dateandtime ASC");
          $stmt->bind_param('i', $questions[$i-1]['id']);
          $stmt->execute();
          $res = $stmt->get_result();
          if ($res->num_rows > 0) {
            while ($row = $res->fetch_assoc()) {
              if (!array_key_exists($row['userid'], $startTimes)) {
                $startTimes[$row['userid']] = $row['dateandtime'];
              }
              $elapsed = strtotime($row['dateandtime']) - strtotime($startTimes[$row['userid']]);
              $days = $elapsed / 86400;

              for ($j = 0; $j < count($choices); $j++) {
                if ($row['response'] == $choices[$j]) {
                  $choiceVal = $j+1;
                  break;
                }
              }

              $allData[] = array('userid' => $row['userid'], 'x' => $days, 'y' => $choiceVal, 'response' => $row['response']);
            }
          }
        }
      }
    }
  }
  else {
    $xData = $yData = array();
    $orderByVar = "";
    if ($stat1 == 2) {
      $orderByVar = "grade";
      $stmt1 = $mysqli->prepare("SELECT id, grade FROM ihc_users ORDER BY grade ASC");
      $stmt1->execute();
      $res1 = $stmt1->get_result();
      if ($res1->num_rows > 0) {
        while ($row = $res1->fetch_assoc()) {
          $xData[] = array('userid' => $row['id'], 'xval' => $row['grade']);
        }
      }
      $xValues = array("N/A", "Freshman", "Sophomore", "Junior", "Senior", "Graduate Student", "Doctoral Student", "Faculty");
    }
    else if ($stat1 == 3) {
      $orderByVar = "type";
      $stmt1 = $mysqli->prepare("SELECT id, type FROM ihc_users ORDER BY type ASC");
      $stmt1->execute();
      $res1 = $stmt1->get_result();
      if ($res1->num_rows > 0) {
        while ($row = $res1->fetch_assoc()) {
          $xData[] = array('userid' => $row['id'], 'xval' => $row['type']);
        }
      }
      $xValues = array("Domestic Student", "International Student", "Faculty", "Resident", "Visitor");
    }
    else if ($stat1 == 4) {
      $orderByVar = "stampcount";
      $stmt1 = $mysqli->prepare("SELECT id, stampcount FROM ihc_users ORDER BY stampcount ASC");
      $stmt1->execute();
      $res1 = $stmt1->get_result();
      if ($res1->num_rows > 0) {
        while ($row = $res1->fetch_assoc()) {
          $xData[] = array('userid' => $row['id'], 'xval' => $row['stampcount']);
        }
      }
    }
    if ($stat2 == 5) {
      $stmt2 = $mysqli->prepare("SELECT id, stampcount FROM ihc_users ORDER BY stampcount ASC");
      $stmt2->execute();
      $res2 = $stmt2->get_result();
      if ($res2->num_rows > 0) {
        while ($row = $res2->fetch_assoc()) {
          $yData[] = array('userid' => $row['id'], 'yval' => $row['stampcount']);
        }
      }
    }
    else {
      for ($i = 1; $i <= count($questions); $i++) {
        if ($stat2 == $i + 5) {
          $choicesStr = $questions[$i-1]['choices'];
          $choiceVal = 0;
          $token = strtok($choicesStr, ",");
          while ($token !== false) {
            $choices[] = $token;
            $token = strtok(",");
          }
          for ($j = count($choices)-1; $j >= 0; $j--) {
            $newChoices[] = $choices[$j];
          }
          $choices = $newChoices;

          $stmt2 = $mysqli->prepare("SELECT DISTINCT userid, response FROM ihc_survey_responses WHERE questionid=? ORDER BY dateandtime ASC");
          $stmt2->bind_param('i', $i);
          $stmt2->execute();
          $res2 = $stmt2->get_result();
          if ($res2->num_rows > 0) {
            while ($row = $res2->fetch_assoc()) {

              for ($j = 0; $j < count($choices); $j++) {
                if ($row['response'] == $choices[$j]) {
                  $choiceVal = $j+1;
                  break;
                }
              }

              $yData[] = array('userid' => $row['userid'], 'yval' => $choiceVal);
            }
          }
          break;
        }
      }
    }
    for ($i = 0; $i < count($xData); $i++) {
      for ($j = 0; $j < count($yData); $j++) {
        if ($xData[$i]['userid'] == $yData[$j]['userid']) {
          $allData[] = array('x' => $xData[$i]['xval'], 'y' => $yData[$j]['yval']);
          break;
        }
      }
    }
  }

  //print_r($allData);

  ?>

  <html>
  <head>
    <title>View Correlation - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(drawRegressionChart);

    function drawRegressionChart() {
      var data = new google.visualization.DataTable();
      data.addColumn('number', '<?php echo $xAxis; ?>');
      data.addColumn('number', '<?php echo $yAxis; ?>');
      <?php for ($i = 0; $i < count($allData); $i++) { ?>
        data.addRows([
          [<?php echo $allData[$i]['x']; ?>, <?php echo $allData[$i]['y']; ?>]
        ]);
        <?php } ?>

        var options = {
          chart: {
            title: '<?php echo $metric1; ?> vs. <?php echo $metric2; ?>'
          },
          hAxis: {title: '<?php echo $xAxis; ?>', minValue: 0},
          vAxis: {title: '<?php echo $yAxis; ?>', minValue: 0},
          trendlines: {
            0: {
              visibleInLegend: true,
              showR2: true,
              color: '#d73f09'
            }
          }
        };

        var chart = new google.visualization.ScatterChart(document.getElementById('regression_analysis_chart'));
        chart.draw(data, options);
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
        <left class="sectionheader"><h1>Analysis Center</h1></left><br>
        <form action="./analyze.php">
          <button class="ui red button">
            <i class="arrow left icon"></i>
            Back to Selection Prompt
          </button>
        </form>
      </div>

      <div class="ui divider"></div><br>
      <center><h2>
        View Correlation: <?php echo $metric1 . " vs. " . $metric2; ?>

        <div id="regression_analysis_chart" style="width: 100vw; height: 50vw;"></div>
      </h2></center>
      <?php if (($stat1 == 1 || $stat1 == 4) && $stat2 != 5) { ?>
        <div><center>
          <h4>Y-Value Interpretation</h4>
          <?php for ($i = 0; $i < count($choices); $i++) {
            echo $i+1 . ": " . $choices[$i] . "<br>";
          } ?>
        </center></div>
      <?php }
      else if (($stat1 == 2 || $stat1 == 3) && $stat2 != 5) { ?>
        <center><table>
          <tr>
            <td><div><center>
              <h4>X-Value Interpretation</h4>
              <?php for ($i = 0; $i < count($xValues); $i++) {
                echo $i . ": " . $xValues[$i] . "<br>";
              } ?>
            </center></div></td>
            <td><div><center>
              <h4>Y-Value Interpretation</h4>
              <?php for ($i = 0; $i < count($choices); $i++) {
                echo $i+1 . ": " . $choices[$i] . "<br>";
              } ?>
            </center></div></td>
          </tr>
        </table></center>
      <?php } ?>

      <?php
    }
    ?>
