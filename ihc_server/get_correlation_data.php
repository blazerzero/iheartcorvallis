<!DOCTYPE HTML>

<?php

ini_set('display_errors', 1);
error_reporting(E_ERROR);
ini_set('memory_limit', '4G');
ini_set('max_execution_time', 300);

function regressionStats($arrX, $arrY) {
  $avgX = array_sum($arrX)/count($arrX);
  $avgY = array_sum($arrY)/count($arrY);

  $ssXX = $ssYY = $varX2 = $varY2 = $spXY = $covXY = $b0 = $b1 = 0;
  foreach ($arrX as $x):
    $ssXX += pow(($x - $avgX), 2);
  endforeach;
  $varX = $ssXX / (count($arrX) - 1);

  foreach ($arrY as $y):
    $ssYY += pow(($y - $avgY), 2);
  endforeach;
  $varY = $ssYY / (count($arrY) - 1);

  for ($i = 0; $i < count($arrX); $i++) {
    $spXY += (($arrX[$i] - $avgX) * ($arrY[$i] - $avgY));
  }
  $covXY = $spXY / (count($arrX) - 1);
  $b1 = $spXY / $ssXX;
  $b0 = $avgY - ($b1 * $avgX);
  $r = $covXY / (sqrt($varX) * sqrt($varY));
  return array('avgX' => $avgX, 'ssXX' => $ssXX, 'varX' => $varX, 'avgY' => $avgY, 'ssYY' => $ssYY, 'varY' => $varY, 'spXY' => $spXY, 'covXY' => $covXY, 'b1' => $b1, 'b0' => $b0, 'r' => $r);
}

function tScore($arrX, $arrY, $ssXX, $b0, $b1) {
  $ssE = 0;
  for ($i = 0; $i < count($arrY); $i++) {
    $ssE += pow(($arrY[$i] - ($b0 + ($b1 * $arrX[$i]))), 2);
  }

  $se = sqrt(($ssE/ (count($arrY) - 2)) / $ssXX);
  $t0 = ($b1 - 0) / $se;
  return $t0;
}

function fScore($arrX, $arrY, $avgY, $ssYY, $b0, $b1) {
  $ssE = 0;
  for ($i = 0; $i < count($arrY); $i++) {
    $ssE += pow(($arrY[$i] - ($b0 + ($b1 * $arrX[$i]))), 2);
  }

  $ssT = $ssYY;
  $msT = $ssT / (count($arrY) - 1);
  $ssR = 0;
  for ($i = 0; $i < count($arrX); $i++) {
    $ssR += pow((($b0 + ($b1 * $arrX[$i])) - $avgY), 2);
  }
  $f_ssT = $ssR + $ssE;
  $msE = $ssE / (count($arrY) - 2);
  $msR = $ssR / 1;

  $f0 = $msR / $msE;
  return $f0;
}

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

$stat1 = $stat2 = $graphChoice = $res = $res1 = $res2 = $metric1 = $metric2 = $xAxis = $yAxis = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $stat1 = $_POST["stat1"];
  $stat2 = $_POST["stat2"];
  $graphChoice = $_POST["graphchoice"];

  $stmt = $mysqli->prepare("SELECT * FROM ihc_survey");
  $stmt->execute();
  $result = $stmt->get_result();
  $questions = array();
  while ($question = $result->fetch_assoc()) {
    $questions[] = $question;
  }

  // SETTING METRICS AND AXES //

  $metric1 = "Change in Time";
  if ($stat2 == 3) {    // y-Axis: number of completed events
    $xAxis = "Number of Days Since First Completed Event";
    $metric2 = $yAxis = "Number of Completed Events";
  }
  else {  // y-Axis: responses to survey question
    for ($i = 1; $i <= count($questions); $i++) {
      if ($stat2 == $i + 3) {
        $xAxis = "Number of Days Since First Survey Response";
        $metric2 = "Responses to Survey Question:<br>" . $questions[$i-1]["question"];
        $yAxis = "Response Value";
      }
    }
  }

  $frData = $soData = $jrData = $srData = $grData = $drData = array();
  $domData = $intlData = $facData = $resData = $visitorData = array();
  $startTimes = $numDataPerUser = array();
  $frX = $soX = $jrX = $srX = $grX = $drX = array();  // grade x-Axis data
  $frY = $soY = $jrY = $srY = $grY = $drY = array();  // grade y-Axis data
  $domX = $intlX = $facX = $resX = $visitorX = array(); // type x-Axis data
  $domY = $intlY = $facY = $resY = $visitorY = array(); // type y-Axis data

  $choices = array();
  $newChoices = array();

  // RETRIEVING DATA //
  //if ($stat1 == 1) {
  if ($stat2 == 3) {
    $stmt = $mysqli->prepare("SELECT CE.*, U.grade, U.type FROM ihc_completed_events CE, ihc_users U WHERE CE.userid=U.id ORDER BY U.type ASC, U.grade ASC, CE.userid ASC, CE.dateandtime ASC");
    $stmt->execute();
    $res = $stmt->get_result();
    if ($res->num_rows > 0) {
      $counter = 0;
      while ($row = $res->fetch_assoc()) {
        if (!array_key_exists($row['userid'], $startTimes)) {
          $startTimes[$row['userid']] = $row['dateandtime'];
          $numDataPerUser[$row['userid']] = 0;
        }
        $elapsed = strtotime($row['dateandtime']) - strtotime($startTimes[$row['userid']]);
        $days = $elapsed / 86400;
        $numDataPerUser[$row['userid']]++;

        $newListing = array('userid' => $row['userid'], 'x' => floor($days), 'y' => $numDataPerUser[$row['userid']]);

        if ($counter % 40 == 0) {
          if ($row['grade'] == 1) $frData[] = $newListing;
          else if ($row['grade'] == 2) $soData[] = $newListing;
          else if ($row['grade'] == 3) $jrData[] = $newListing;
          else if ($row['grade'] == 4) $srData[] = $newListing;
          else if ($row['grade'] == 5) $grData[] = $newListing;
          else if ($row['grade'] == 6) $drData[] = $newListing;

          if ($row['type'] == 0) $domData[] = $newListing;
          else if ($row['type'] == 1) $intlData[] = $newListing;
          else if ($row['type'] == 2) $facData[] = $newListing;
          else if ($row['type'] == 3) $resData[] = $newListing;
          else if ($row['type'] == 4) $visitorData[] = $newListing;
        }
        $counter++;
      }
    }
  }
  else {
    for ($i = 1; $i <= count($questions); $i++) {
      if ($stat2 == $i + 3) {

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

        $stmt = $mysqli->prepare("SELECT SR.*, U.grade, U.type FROM ihc_survey_responses SR, ihc_users U WHERE SR.userid=U.id AND SR.questionid=? ORDER BY U.type ASC, U.grade ASC, SR.userid ASC, SR.dateandtime ASC");
        $stmt->bind_param('i', $questions[$i-1]['id']);
        $stmt->execute();
        $res = $stmt->get_result();
        if ($res->num_rows > 0) {
          $counter = 0;
          while ($row = $res->fetch_assoc()) {
            if (!array_key_exists($row['userid'], $startTimes)) {
              $startTimes[$row['userid']] = $row['dateandtime'];
            }
            $elapsed = strtotime($row['dateandtime']) - strtotime($startTimes[$row['userid']]);
            $days = $elapsed / 86400;

            for ($j = 0; $j < count($choices); $j++) {
              if (trim($row['response']) == trim($choices[$j])) {
                $choiceVal = $j+1;
                break;
              }
            }

            $newListing = array('userid' => $row['userid'], 'x' => floor($days), 'y' => $choiceVal, 'response' => $row['response']);

            if ($counter % 40 == 0) {
              if ($row['grade'] == 1) $frData[] = $newListing;
              else if ($row['grade'] == 2) $soData[] = $newListing;
              else if ($row['grade'] == 3) $jrData[] = $newListing;
              else if ($row['grade'] == 4) $srData[] = $newListing;
              else if ($row['grade'] == 5) $grData[] = $newListing;
              else if ($row['grade'] == 6) $drData[] = $newListing;

              if ($row['type'] == 0) $domData[] = $newListing;
              else if ($row['type'] == 1) $intlData[] = $newListing;
              else if ($row['type'] == 2) $facData[] = $newListing;
              else if ($row['type'] == 3) $resData[] = $newListing;
              else if ($row['type'] == 4) $visitorData[] = $newListing;
            }
            $counter++;
          }
        }

        break;
      }
    }
  }
  //}
  /*else if ($stat1 == 2) {
    if ($stat2 == 3) {
      $stmt = $mysqli->prepare("SELECT CE.*, U.grade, U.type FROM ihc_completed_events CE, ihc_users U WHERE CE.userid=U.id ORDER BY U.type ASC, U.grade ASC, CE.userid ASC, CE.dateandtime ASC");
      $stmt->execute();
      $res = $stmt->get_result();
      if ($res->num_rows > 0) {
        while ($row = $res->fetch_assoc()) {
          if (!array_key_exists($row['userid'], $startTimes)) {
            $startTimes[$row['userid']] = $row['dateandtime'];
            $numDataPerUser[$row['userid']] = 0;
          }

          $numDataPerUser[$row['userid']]++;
          $newListing = array('userid' => $row['userid'], 'x' => $numDataPerUser[$row['userid']], 'y' => $numDataPerUser[$row['userid']], 'grade' => $row['grade'], 'type' => $row['type']);

          if ($row['grade'] == 1) $frData[] = $newListing;
          else if ($row['grade'] == 2) $soData[] = $newListing;
          else if ($row['grade'] == 3) $jrData[] = $newListing;
          else if ($row['grade'] == 4) $srData[] = $newListing;
          else if ($row['grade'] == 5) $grData[] = $newListing;
          else if ($row['grade'] == 6) $drData[] = $newListing;

          if ($row['type'] == 0) $domData[] = $newListing;
          else if ($row['type'] == 1) $intlData[] = $newListing;
          else if ($row['type'] == 2) $facData[] = $newListing;
          else if ($row['type'] == 3) $resData[] = $newListing;
          else if ($row['type'] == 4) $visitorData[] = $newListing;
        }
      }
    }
    else {
      for ($i = 1; $i <= count($questions); $i++) {
        if ($stat2 == $i + 3) {

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

          $allXData = $allYData = array();
          $stmt = $mysqli->prepare("SELECT CE.*, U.grade, U.type FROM ihc_completed_events CE, ihc_users U WHERE CE.userid=U.id ORDER BY U.type ASC, U.grade ASC, CE.userid ASC, CE.dateandtime ASC");
          $stmt->execute();
          $res = $stmt->get_result();
          if ($res->num_rows > 0) {
            while ($row = $res->fetch_assoc()) {
              if (!array_key_exists($row['userid'], $startTimes)) {
                $startTimes[$row['userid']] = $row['dateandtime'];
                $numDataPerUser[$row['userid']] = 0;
              }

              $numDataPerUser[$row['userid']]++;
              $allXData[] = array('userid' => $row['userid'], 'dateandtime' => $row['dateandtime'], 'x' => $numDataPerUser[$row['userid']], 'grade' => $row['grade'], 'type' => $row['type']);
            }
          }

          $stmt2 = $mysqli->prepare("SELECT SR.*, U.grade, U.type FROM ihc_survey_responses SR, ihc_users U WHERE SR.userid=U.id AND SR.questionid=? ORDER BY U.type ASC, U.grade ASC, SR.userid ASC, SR.dateandtime ASC");
          $stmt2->bind_param('i', $questions[$i-1]['id']);
          $stmt2->execute();
          $res2 = $stmt2->get_result();
          if ($res2->num_rows > 0) {
            while ($row = $res2->fetch_assoc()) {

              for ($j = 0; $j < count($choices); $j++) {
                if (trim($row['response']) == trim($choices[$j])) {
                  $choiceVal = $j+1;
                  break;
                }
              }

              $newYListing = array('userid' => $row['userid'], 'dateandtime' => $row['dateandtime'], 'y' => $choiceVal, 'grade' => $row['grade'], 'type' => $row['type']);
              $allYData[] = $newYListing;
            }
          }

          $j = 0;
          $userid = "";
          $userCEList = array();
          $newListing = array();
          while ($j < count($allYData)) {
            if ($userid != $allYData[$j]['userid']) {
              $userid = $allYData[$j]['userid'];
              $userCEList = array();
            }
            echo "userid: " . $userid . "<br>";
            echo "allYData[$j][userid]: " . $allYData[$j]['userid'] . "<br>";
            echo "allXData[0][userid]: " . $allXData[0]['userid'] . "<br>";
            if (in_array($allYData[$j]['userid'], array_column($allXData, "userid"))) {
              while ($userid != $allXData[0]['userid']) {
                $removedCE = array_shift($allXData);
              }
              while ($userid == $allXData[0]['userid']) {
                echo "allXData[0]: "; print_r($allXData[0]); echo "<br>";
                $userCEList[] = $allXData[0];
                $removedCE = array_shift($allXData);
              }
              print_r($userCEList); echo "<br>";
              if (count($userCEList) > 0 && strtotime($allYData[$j]['dateandtime']) - strtotime($userCEList[0]['dateandtime']) < 0) {
                $newListing = array('userid' => $allYData[$j]['userid'], 'x' => 0, 'y' => $allYData[$j]['y']);
                if ($allYData[$j]['grade'] == 1) $frData[] = $newListing;
                else if ($allYData[$j]['grade'] == 2) $soData[] = $newListing;
                else if ($allYData[$j]['grade'] == 3) $jrData[] = $newListing;
                else if ($allYData[$j]['grade'] == 4) $srData[] = $newListing;
                else if ($allYData[$j]['grade'] == 5) $grData[] = $newListing;
                else if ($allYData[$j]['grade'] == 6) $drData[] = $newListing;

                if ($allYData[$j]['type'] == 0) $domData[] = $newListing;
                else if ($allYData[$j]['type'] == 1) $intlData[] = $newListing;
                else if ($allYData[$j]['type'] == 2) $facData[] = $newListing;
                else if ($allYData[$j]['type'] == 3) $resData[] = $newListing;
                else if ($allYData[$j]['type'] == 4) $visitorData[] = $newListing;
                $j++;
              }
              else {
                for ($k = 0; $k < count($userCEList); $k++) {
                  if (strtotime($allYData[$j]['dateandtime']) - strtotime($userCEList[$k]['dateandtime']) >= 0) {
                    $newListing = array('userid' => $allYData[$j]['userid'], 'x' => k+1, 'y' => $allYData[$j]['y']);
                    if ($allYData[$j]['grade'] == 1) $frData[] = $newListing;
                    else if ($allYData[$j]['grade'] == 2) $soData[] = $newListing;
                    else if ($allYData[$j]['grade'] == 3) $jrData[] = $newListing;
                    else if ($allYData[$j]['grade'] == 4) $srData[] = $newListing;
                    else if ($allYData[$j]['grade'] == 5) $grData[] = $newListing;
                    else if ($allYData[$j]['grade'] == 6) $drData[] = $newListing;

                    if ($allYData[$j]['type'] == 0) $domData[] = $newListing;
                    else if ($allYData[$j]['type'] == 1) $intlData[] = $newListing;
                    else if ($allYData[$j]['type'] == 2) $facData[] = $newListing;
                    else if ($allYData[$j]['type'] == 3) $resData[] = $newListing;
                    else if ($allYData[$j]['type'] == 4) $visitorData[] = $newListing;
                    $j++;
                  }
                }
              }
            }
            else {
              while ($userid == $allYData[$j]['userid']) {
                $removedY = array_shift($allYData);
                $j++;
              }
            }

          }

          break;
        }
      }

    }

  }*/

  /* SIMPLE LINEAR REGRESSION */
  $frX = array_column($frData, 'x');
  $frY = array_column($frData, 'y');
  $frStats = regressionStats($frX, $frY);
  $frT0 = tScore($frX, $frY, $frStats['ssXX'], $frStats['b0'], $frStats['b1']);
  $frF0 = fScore($frX, $frY, $frStats['avgY'], $frStats['ssYY'], $frStats['b0'], $frStats['b1']);

  $soX = array_column($soData, 'x');
  $soY = array_column($soData, 'y');
  $soStats = regressionStats($soX, $soY);
  $soT0 = tScore($soX, $soY, $soStats['ssXX'], $soStats['b0'], $soStats['b1']);
  $soF0 = fScore($soX, $soY, $soStats['avgY'], $soStats['ssYY'], $soStats['b0'], $soStats['b1']);

  $jrX = array_column($jrData, 'x');
  $jrY = array_column($jrData, 'y');
  $jrStats = regressionStats($jrX, $jrY);
  $jrT0 = tScore($jrX, $jrY, $jrStats['ssXX'], $jrStats['b0'], $jrStats['b1']);
  $jrF0 = fScore($jrX, $jrY, $jrStats['avgY'], $jrStats['ssYY'], $jrStats['b0'], $jrStats['b1']);

  $srX = array_column($srData, 'x');
  $srY = array_column($srData, 'y');
  $srStats = regressionStats($srX, $srY);
  $srT0 = tScore($srX, $srY, $srStats['ssXX'], $srStats['b0'], $srStats['b1']);
  $srF0 = fScore($srX, $srY, $srStats['avgY'], $srStats['ssYY'], $srStats['b0'], $srStats['b1']);

  $grX = array_column($grData, 'x');
  $grY = array_column($grData, 'y');
  $grStats = regressionStats($grX, $grY);
  $grT0 = tScore($grX, $grY, $grStats['ssXX'], $grStats['b0'], $grStats['b1']);
  $grF0 = fScore($grX, $grY, $grStats['avgY'], $grStats['ssYY'], $grStats['b0'], $grStats['b1']);

  $drX = array_column($drData, 'x');
  $drY = array_column($drData, 'y');
  $drStats = regressionStats($drX, $drY);
  $drT0 = tScore($drX, $drY, $drStats['ssXX'], $drStats['b0'], $drStats['b1']);
  $drF0 = fScore($drX, $drY, $drStats['avgY'], $drStats['ssYY'], $drStats['b0'], $drStats['b1']);


  $domX = array_column($domData, 'x');
  $domY = array_column($domData, 'y');
  $domStats = regressionStats($domX, $domY);
  $domT0 = tScore($domX, $domY, $domStats['ssXX'], $domStats['b0'], $domStats['b1']);
  $domF0 = fScore($domX, $domY, $domStats['avgY'], $domStats['ssYY'], $domStats['b0'], $domStats['b1']);

  $intlX = array_column($intlData, 'x');
  $intlY = array_column($intlData, 'y');
  $intlStats = regressionStats($intlX, $intlY);
  $intlT0 = tScore($intlX, $intlY, $intlStats['ssXX'], $intlStats['b0'], $intlStats['b1']);
  $intlF0 = fScore($intlX, $intlY, $intlStats['avgY'], $intlStats['ssYY'], $intlStats['b0'], $intlStats['b1']);

  $facX = array_column($facData, 'x');
  $facY = array_column($facData, 'y');
  $facStats = regressionStats($facX, $facY);
  $facT0 = tScore($facX, $facY, $facStats['ssXX'], $facStats['b0'], $facStats['b1']);
  $facF0 = fScore($facX, $facY, $facStats['avgY'], $facStats['ssYY'], $facStats['b0'], $facStats['b1']);

  $resX = array_column($resData, 'x');
  $resY = array_column($resData, 'y');
  $resStats = regressionStats($resX, $resY);
  $resT0 = tScore($resX, $resY, $resStats['ssXX'], $resStats['b0'], $resStats['b1']);
  $resF0 = fScore($resX, $resY, $resStats['avgY'], $resStats['ssYY'], $resStats['b0'], $resStats['b1']);

  $visitorX = array_column($visitorData, 'x');
  $visitorY = array_column($visitorData, 'y');
  $visitorStats = regressionStats($visitorX, $visitorY);
  $visitorT0 = tScore($visitorX, $visitorY, $visitorStats['ssXX'], $visitorStats['b0'], $visitorStats['b1']);
  $visitorF0 = fScore($visitorX, $visitorY, $visitorStats['avgY'], $visitorStats['ssYY'], $visitorStats['b0'], $visitorStats['b1']);

  /*
  $allData = array();
  $startTimes = array();
  $numDataPerUser = array();
  $choices = array();
  $newChoices = array();
  $xValues = array();
  $xData = $yData = array();

  if ($stat1 == 1) {      // Second variable being tested against time
    if ($stat2 == 3) {    // Time vs. Number of Completed Events
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
          $xData[] = array('userid' => $row['userid'], 'x' => $days);
          $yData[] = array('userid' => $row['userid'], 'y' => $numDataPerUser[$row['userid']] + 1);
          $numDataPerUser[$row['userid']]++;
        }
      }
    }
    else {
      for ($i = 1; $i <= count($questions); $i++) {
        if ($stat2 == $i + 3) {

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
                if (trim($row['response']) == trim($choices[$j])) {
                  $choiceVal = $j+1;

                  break;
                }
              }

              $allData[] = array('userid' => $row['userid'], 'x' => $days, 'y' => $choiceVal, 'response' => $row['response']);
              $xData[] = array('userid' => $row['userid'], 'x' => $days);
              $yData[] = array('userid' => $row['userid'], 'y' => $choiceVal);
            }
          }
        }
      }
    }
  }
  else {
    $stmt1 = $mysqli->prepare("SELECT id, stampcount FROM ihc_users ORDER BY stampcount ASC");
    $stmt1->execute();
    $res1 = $stmt1->get_result();
    if ($res1->num_rows > 0) {
      while ($row = $res1->fetch_assoc()) {
        $xData[] = array('userid' => $row['id'], 'xval' => $row['stampcount']);
      }
    }
    if ($stat2 == 3) {
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
        if ($stat2 == $i + 3) {
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

          $stmt2 = $mysqli->prepare("SELECT userid, response FROM ihc_survey_responses WHERE questionid=? ORDER BY dateandtime ASC");
          $stmt2->bind_param('i', $i);
          $stmt2->execute();
          $res2 = $stmt2->get_result();
          if ($res2->num_rows > 0) {
            while ($row = $res2->fetch_assoc()) {

              for ($j = 0; $j < count($choices); $j++) {
                if (trim($row['response']) == trim($choices[$j])) {
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
  }*/

  /* SIMPLE LINEAR REGRESSION ANALYSIS */
  /*$arrX = array_column($allData, 'x');
  $arrY = array_column($allData, 'y');
  $avgX = array_sum($arrX)/count($arrX);
  $avgY = array_sum($arrY)/count($arrY);

  $ssXX = $ssYY = $varX2 = $varY2 = $spXY = $covXY = $b0 = $b1 = 0;
  foreach ($arrX as $x):
    $ssXX += pow(($x - $avgX), 2);
  endforeach;
  $varX = $ssXX / (count($arrX) - 1);

  foreach ($arrY as $y):
    $ssYY += pow(($y - $avgY), 2);
  endforeach;
  $varY = $ssYY / (count($arrY) - 1);

  for ($i = 0; $i < count($arrX); $i++) {
    $spXY += (($arrX[$i] - $avgX) * ($arrY[$i] - $avgY));
  }
  $covXY = $spXY / (count($arrX) - 1);
  $b1 = $spXY / $ssXX;
  $b0 = $avgY - ($b1 * $avgX);

  $minX = min($arrX);
  $minY = $b0 + ($b1 * $minX);
  $maxX = max($arrX);
  $maxY = $b0 + ($b1 * $maxX);*/

  /* t-Test */
  /*$ssE = 0;
  for ($i = 0; $i < count($arrY); $i++) {
    $ssE += pow(($arrY[$i] - ($b0 + ($b1 * $arrX[$i]))), 2);
  }

  $se = sqrt(($ssE/ (count($arrY) - 2)) / $ssXX);
  $t0 = ($b1 - 0) / $se;*/

  /* ANOVA Test */
  /*$ssT = $ssYY;
  $msT = $ssT / (count($arrY) - 1);
  $ssR = 0;
  for ($i = 0; $i < count($arrX); $i++) {
    $ssR += pow((($b0 + ($b1 * $arrX[$i])) - $avgY), 2);
  }
  $f_ssT = $ssR + $ssE;
  $msE = $ssE / (count($arrY) - 2);
  $msR = $ssR / 1;

  $f0 = $msR / $msE;*/

  ?>

  <html>
  <head>
    <title>View Correlation - I Heart Corvallis Administrative Suite</title>
    <link type="text/css" rel="stylesheet" href="./css/Semantic-UI-CSS-master/semantic.css"/>
    <link type="text/css" rel="stylesheet" href="./css/stylesheet.css"/>
    <script type="text/javascript" src="./css/Semantic-UI-CSS-master/semantic.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
    <script src= "https://cdn.zingchart.com/zingchart.min.js"></script>
    <script>
    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    </script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/jstat@latest/dist/jstat.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script>
    $(document).ready(function() {
      $("#siteheader").load("siteheader.html");
      var fr_t_pValue, fr_f_pValue, so_t_pValue, so_f_pValue, jr_t_pValue, jr_f_pValue, sr_t_pValue, sr_f_pValue, gr_t_pValue, gr_f_pValue, dr_t_pValue, dr_f_pValue;
      var dom_t_pValue, dom_f_pValue, intl_t_pValue, intl_f_pValue, fac_t_pValue, fac_f_pValue, res_t_pValue, res_f_pValue, visitor_t_pValue, visitor_f_pValue;

      <?php if ($graphChoice == 1) { ?>

        fr_t_pValue = jStat.ttest(<?php echo $frT0; ?>, <?php echo count($frY); ?>);
        document.getElementById("fr_t-pvalue").innerText = "Two-sided p-value: " + fr_t_pValue.toFixed(4);
        fr_f_pValue = jStat.ftest(<?php echo $frF0; ?>, 1, <?php echo count($frY) - 2; ?>);
        document.getElementById("fr_f-pvalue").innerText = "p-value: " + fr_f_pValue.toFixed(4);
        so_t_pValue = jStat.ttest(<?php echo $soT0; ?>, <?php echo count($soY); ?>);
        document.getElementById("so_t-pvalue").innerText = "Two-sided p-value: " + so_t_pValue.toFixed(4);
        so_f_pValue = jStat.ftest(<?php echo $soF0; ?>, 1, <?php echo count($soY) - 2; ?>);
        document.getElementById("so_f-pvalue").innerText = "p-value: " + so_f_pValue.toFixed(4);
        jr_t_pValue = jStat.ttest(<?php echo $jrT0; ?>, <?php echo count($jrY); ?>);
        document.getElementById("jr_t-pvalue").innerText = "Two-sided p-value: " + jr_t_pValue.toFixed(4);
        jr_f_pValue = jStat.ftest(<?php echo $jrF0; ?>, 1, <?php echo count($jrY) - 2; ?>);
        document.getElementById("jr_f-pvalue").innerText = "p-value: " + jr_f_pValue.toFixed(4);
        sr_t_pValue = jStat.ttest(<?php echo $srT0; ?>, <?php echo count($srY); ?>);
        document.getElementById("sr_t-pvalue").innerText = "Two-sided p-value: " + sr_t_pValue.toFixed(4);
        sr_f_pValue = jStat.ftest(<?php echo $srF0; ?>, 1, <?php echo count($srY) - 2; ?>);
        document.getElementById("sr_f-pvalue").innerText = "p-value: " + sr_f_pValue.toFixed(4);
        gr_t_pValue = jStat.ttest(<?php echo $grT0; ?>, <?php echo count($grY); ?>);
        document.getElementById("gr_t-pvalue").innerText = "Two-sided p-value: " + gr_t_pValue.toFixed(4);
        gr_f_pValue = jStat.ftest(<?php echo $grF0; ?>, 1, <?php echo count($grY) - 2; ?>);
        document.getElementById("gr_f-pvalue").innerText = "p-value: " + gr_f_pValue.toFixed(4);
        dr_t_pValue = jStat.ttest(<?php echo $drT0; ?>, <?php echo count($drY); ?>);
        document.getElementById("dr_t-pvalue").innerText = "Two-sided p-value: " + dr_t_pValue.toFixed(4);
        dr_f_pValue = jStat.ftest(<?php echo $drF0; ?>, 1, <?php echo count($drY) - 2; ?>);
        document.getElementById("dr_f-pvalue").innerText = "p-value: " + dr_f_pValue.toFixed(4);

      <?php } else if ($graphChoice == 2) { ?>

        dom_t_pValue = jStat.ttest(<?php echo $domT0; ?>, <?php echo count($domY); ?>);
        document.getElementById("dom_t-pvalue").innerText = "Two-sided p-value: " + dom_t_pValue.toFixed(4);
        dom_f_pValue = jStat.ftest(<?php echo $domF0; ?>, 1, <?php echo count($domY) - 2; ?>);
        document.getElementById("dom_f-pvalue").innerText = "p-value: " + dom_f_pValue.toFixed(4);
        intl_t_pValue = jStat.ttest(<?php echo $intlT0; ?>, <?php echo count($intlY); ?>);
        document.getElementById("intl_t-pvalue").innerText = "Two-sided p-value: " + intl_t_pValue.toFixed(4);
        intl_f_pValue = jStat.ftest(<?php echo $intlF0; ?>, 1, <?php echo count($intlY) - 2; ?>);
        document.getElementById("intl_f-pvalue").innerText = "p-value: " + intl_f_pValue.toFixed(4);
        fac_t_pValue = jStat.ttest(<?php echo $facT0; ?>, <?php echo count($facY); ?>);
        document.getElementById("fac_t-pvalue").innerText = "Two-sided p-value: " + fac_t_pValue.toFixed(4);
        fac_f_pValue = jStat.ftest(<?php echo $facF0; ?>, 1, <?php echo count($facY) - 2; ?>);
        document.getElementById("fac_f-pvalue").innerText = "p-value: " + fac_f_pValue.toFixed(4);
        res_t_pValue = jStat.ttest(<?php echo $resT0; ?>, <?php echo count($resY); ?>);
        document.getElementById("res_t-pvalue").innerText = "Two-sided p-value: " + res_t_pValue.toFixed(4);
        res_f_pValue = jStat.ftest(<?php echo $resF0; ?>, 1, <?php echo count($resY) - 2; ?>);
        document.getElementById("res_f-pvalue").innerText = "p-value: " + res_f_pValue.toFixed(4);
        visitor_t_pValue = jStat.ttest(<?php echo $visitorT0; ?>, <?php echo count($visitorY); ?>);
        document.getElementById("visitor_t-pvalue").innerText = "Two-sided p-value: " + visitor_t_pValue.toFixed(4);
        visitor_f_pValue = jStat.ftest(<?php echo $visitorF0; ?>, 1, <?php echo count($visitorY) - 2; ?>);
        document.getElementById("visitor_f-pvalue").innerText = "p-value: " + visitor_f_pValue.toFixed(4);

      <?php } ?>
    });
    </script>
    <script>
    google.charts.load("current", {packages:["corechart"]});
    <?php if ($graphChoice == 1) { ?>
      google.charts.setOnLoadCallback(drawClassStandingRegressionChart);
    <?php } else if ($graphChoice == 2) { ?>
      google.charts.setOnLoadCallback(drawTypeRegressionChart);
    <?php } ?>
    function drawClassStandingRegressionChart() {
      var frData = new google.visualization.DataTable();
      frData.addColumn('number', '<?php echo $xAxis; ?>');
      frData.addColumn('number', 'Freshmen');
      <?php for ($i = 0; $i < count($frX); $i++) { ?>
        frData.addRows([
          [<?php echo $frX[$i]; ?>, <?php echo $frY[$i]; ?>]
        ]);
      <?php } ?>

      var soData = new google.visualization.DataTable();
      soData.addColumn('number', '<?php echo $xAxis; ?>');
      soData.addColumn('number', 'Sophomores');
      <?php for ($i = 0; $i < count($soX); $i++) { ?>
        soData.addRows([
          [<?php echo $soX[$i]; ?>, <?php echo $soY[$i]; ?>]
        ]);
      <?php } ?>

      var jrData = new google.visualization.DataTable();
      jrData.addColumn('number', '<?php echo $xAxis; ?>');
      jrData.addColumn('number', 'Juniors');
      <?php for ($i = 0; $i < count($jrX); $i++) { ?>
        jrData.addRows([
          [<?php echo $jrX[$i]; ?>, <?php echo $jrY[$i]; ?>]
        ]);
      <?php } ?>

      var srData = new google.visualization.DataTable();
      srData.addColumn('number', '<?php echo $xAxis; ?>');
      srData.addColumn('number', 'Seniors');
      <?php for ($i = 0; $i < count($srX); $i++) { ?>
        srData.addRows([
          [<?php echo $srX[$i]; ?>, <?php echo $srY[$i]; ?>]
        ]);
      <?php } ?>

      var grData = new google.visualization.DataTable();
      grData.addColumn('number', '<?php echo $xAxis; ?>');
      grData.addColumn('number', 'Graduate Students');
      <?php for ($i = 0; $i < count($grX); $i++) { ?>
        grData.addRows([
          [<?php echo $grX[$i]; ?>, <?php echo $grY[$i]; ?>]
        ]);
      <?php } ?>

      var drData = new google.visualization.DataTable();
      drData.addColumn('number', '<?php echo $xAxis; ?>');
      drData.addColumn('number', 'Doctoral Students');
      <?php for ($i = 0; $i < count($drX); $i++) { ?>
        drData.addRows([
          [<?php echo $drX[$i]; ?>, <?php echo $drY[$i]; ?>]
        ]);
      <?php } ?>

      var joined2Data = google.visualization.data.join(frData, soData, 'full', [[0, 0]], [1], [1]);
      var joined3Data = google.visualization.data.join(joined2Data, jrData, 'full', [[0, 0]], [1, 2], [1]);
      var joined4Data = google.visualization.data.join(joined3Data, srData, 'full', [[0, 0]], [1, 2, 3], [1]);
      var joined5Data = google.visualization.data.join(joined4Data, grData, 'full', [[0, 0]], [1, 2, 3, 4], [1]);
      var joinedAllData = google.visualization.data.join(joined5Data, drData, 'full', [[0, 0]], [1, 2, 3, 4, 5], [1]);

      var options = {
        chart: {
          title: '<?php echo $metric1; ?> vs. <?php echo $metric2; ?>'
        },
        hAxis: {title: '<?php echo $xAxis; ?>', minValue: 0},
        vAxis: {title: '<?php echo $yAxis; ?>', minValue: 0},
        trendlines: {
          0: {},
          1: {},
          2: {},
          3: {},
          4: {},
          5: {}
        }
      };

      var chart = new google.visualization.ScatterChart(document.getElementById('class_standing_regression_chart'));
      chart.draw(joinedAllData, options);
    }

    function drawTypeRegressionChart() {
      var domData = new google.visualization.DataTable();
      domData.addColumn('number', '<?php echo $xAxis; ?>');
      domData.addColumn('number', 'Domestic Students');
      <?php for ($i = 0; $i < count($domX); $i++) { ?>
        domData.addRows([
          [<?php echo $domX[$i]; ?>, <?php echo $domY[$i]; ?>]
        ]);
      <?php } ?>

      var intlData = new google.visualization.DataTable();
      intlData.addColumn('number', '<?php echo $xAxis; ?>');
      intlData.addColumn('number', 'International Students');
      <?php for ($i = 0; $i < count($intlX); $i++) { ?>
        intlData.addRows([
          [<?php echo $intlX[$i]; ?>, <?php echo $intlY[$i]; ?>]
        ]);
      <?php } ?>

      var facData = new google.visualization.DataTable();
      facData.addColumn('number', '<?php echo $xAxis; ?>');
      facData.addColumn('number', 'Faculty/Staff');
      <?php for ($i = 0; $i < count($facX); $i++) { ?>
        facData.addRows([
          [<?php echo $facX[$i]; ?>, <?php echo $facY[$i]; ?>]
        ]);
      <?php } ?>

      var resData = new google.visualization.DataTable();
      resData.addColumn('number', '<?php echo $xAxis; ?>');
      resData.addColumn('number', 'Residents');
      <?php for ($i = 0; $i < count($resX); $i++) { ?>
        resData.addRows([
          [<?php echo $resX[$i]; ?>, <?php echo $resY[$i]; ?>]
        ]);
      <?php } ?>

      var visitorData = new google.visualization.DataTable();
      visitorData.addColumn('number', '<?php echo $xAxis; ?>');
      visitorData.addColumn('number', 'Visitors');
      <?php for ($i = 0; $i < count($visitorX); $i++) { ?>
        visitorData.addRows([
          [<?php echo $visitorX[$i]; ?>, <?php echo $visitorY[$i]; ?>]
        ]);
      <?php } ?>

      var joined2Data = google.visualization.data.join(domData, intlData, 'full', [[0, 0]], [1], [1]);
      var joined3Data = google.visualization.data.join(joined2Data, facData, 'full', [[0, 0]], [1, 2], [1]);
      var joined4Data = google.visualization.data.join(joined3Data, resData, 'full', [[0, 0]], [1, 2, 3], [1]);
      var joinedAllData = google.visualization.data.join(joined4Data, visitorData, 'full', [[0, 0]], [1, 2, 3, 4], [1]);

      var options = {
        chart: {
          title: '<?php echo $metric1; ?> vs. <?php echo $metric2; ?>'
        },
        hAxis: {title: '<?php echo $xAxis; ?>', minValue: 0},
        vAxis: {title: '<?php echo $yAxis; ?>', minValue: 0},
        trendlines: {
          0: {},
          1: {},
          2: {},
          3: {},
          4: {}
        }
      };

      var chart = new google.visualization.ScatterChart(document.getElementById('type_regression_chart'));
      chart.draw(joinedAllData, options);
    }

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

    <?php if ($graphChoice == 1) { ?>

      <center>
        <h2>View Correlation: <?php echo $metric1; ?> vs. <?php echo $metric2; ?></h2>
        <h3>Students Only (Sample Grouped By Class Standing)</h3>
        <div id="class_standing_regression_chart" style="width: 100%; height: 60vw;"></div>
      </center>
      <?php if ($stat2 != 3) { ?>
        <div><center>
          <h4>Y-Value Interpretation</h4>
          <?php for ($i = 0; $i < count($choices); $i++) {
            echo $i+1 . ": " . $choices[$i] . "<br>";
          } ?>
        </center></div>
      <?php } ?><br>

      <div class="ui divider"></div><br>

      <div><center>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line"><h3>Class Standing</h3></th>
              <th><h3>Sample Statistics</h3></th>
              <th><h3>t-Test</h3></th>
              <th><h3>ANOVA Test</h3></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><h3>Freshmen</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($frStats['b0'], 4); ?> + <?php echo number_format($frStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($frStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($frX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($frStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($frStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($frStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($frStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($frStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($frStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($frT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($frX) - 2; ?></h4>
                <h4 style="color:red" id="fr_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($frF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($frX) - 2; ?></h4>
                <h4 style="color:red" id="fr_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Sophomores</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($soStats['b0'], 4); ?> + <?php echo number_format($soStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($soStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($soX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($soStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($soStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($soStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($soStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($soStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($soStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($soT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($soX) - 2; ?></h4>
                <h4 style="color:red" id="so_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($soF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($soX) - 2; ?></h4>
                <h4 style="color:red" id="so_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Juniors</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($jrStats['b0'], 4); ?> + <?php echo number_format($jrStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($jrStats['r'], 4); ?></h4>
                <h4>Correlation Coefficient: <?php echo number_format($jrStats['r'], 6); ?></h4>
                <h4>Group Size: <?php echo count($jrX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($jrStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($jrStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($jrStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($jrStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($jrStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($jrStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($jrT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($jrX) - 2; ?></h4>
                <h4 style="color:red" id="jr_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($jrF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($jrX) - 2; ?></h4>
                <h4 style="color:red" id="jr_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Seniors</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($srStats['b0'], 4); ?> + <?php echo number_format($srStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($srStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($srX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($srStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($srStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($srStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($srStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($srStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($srStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($srT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($srX) - 2; ?></h4>
                <h4 style="color:red" id="sr_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($srF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($srX) - 2; ?></h4>
                <h4 style="color:red" id="sr_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Graduate Students</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($grStats['b0'], 4); ?> + <?php echo number_format($grStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($grStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($grX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($grStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($grStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($grStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($grStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($grStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($grStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($grT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($grX) - 2; ?></h4>
                <h4 style="color:red" id="gr_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($grF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($grX) - 2; ?></h4>
                <h4 style="color:red" id="gr_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Doctoral Students</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($drStats['b0'], 4); ?> + <?php echo number_format($drStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($drStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($drX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($drStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($drStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($drStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($drStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($drStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($drStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($drT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($drX) - 2; ?></h4>
                <h4 style="color:red" id="dr_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($drF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($drX) - 2; ?></h4>
                <h4 style="color:red" id="dr_f-pvalue"></h4>
              </td>
            </tr>
        </table>
      </center></div><br>

    <?php } else if ($graphChoice == 2) { ?>
      <center>
        <h2>View Correlation: <?php echo $metric1; ?> vs. <?php echo $metric2; ?></h2>
        <h3>All Users (Sample Grouped By User Type)</h3>
        <div id="type_regression_chart" style="width: 100%; height: 60vw;"></div>
      </center>
      <?php if ($stat2 != 3) { ?>
        <div><center>
          <h4>Y-Value Interpretation</h4>
          <?php for ($i = 0; $i < count($choices); $i++) {
            echo $i+1 . ": " . $choices[$i] . "<br>";
          } ?>
        </center></div>
      <?php } ?>

      <div class="ui divider"></div><br>

      <div><center>
        <table class="ui celled padded table">
          <thead>
            <tr>
              <th class="single line"><h3>Class Standing</h3></th>
              <th><h3>Sample Statistics</h3></th>
              <th><h3>t-Test</h3></th>
              <th><h3>ANOVA Test</h3></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><h3>Domestic Students</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($domStats['b0'], 4); ?> + <?php echo number_format($domStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($domStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($domX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($domStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($domStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($domStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($domStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($domStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($domStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($domT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($domX) - 2; ?></h4>
                <h4 style="color:red" id="dom_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($domF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($domX) - 2; ?></h4>
                <h4 style="color:red" id="dom_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>International Students</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($intlStats['b0'], 4); ?> + <?php echo number_format($intlStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($intlStats['r'], 4); ?></h4>
                <h4>Group Size: <?php echo count($intlX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($intlStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($intlStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($intlStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($intlStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($intlStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($intlStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($intlT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($intlX) - 2; ?></h4>
                <h4 style="color:red" id="intl_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($intlF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($intlX) - 2; ?></h4>
                <h4 style="color:red" id="intl_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Faculty/Staff</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($facStats['b0'], 4); ?> + <?php echo number_format($facStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($facStats['r'], 4); ?></h4>
                <h4>Population Size: <?php echo count($facX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($facStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($facStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($facStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($facStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($facStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($facStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($facT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($facX) - 2; ?></h4>
                <h4 style="color:red" id="fac_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($facF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($facX) - 2; ?></h4>
                <h4 style="color:red" id="fac_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Residents</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($resStats['b0'], 4); ?> + <?php echo number_format($resStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($resStats['r'], 4); ?></h4>
                <h4>Population Size: <?php echo count($resX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($resStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($resStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($resStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($resStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($resStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($resStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($resT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($resX) - 2; ?></h4>
                <h4 style="color:red" id="res_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($resF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($resX) - 2; ?></h4>
                <h4 style="color:red" id="res_f-pvalue"></h4>
              </td>
            </tr>
            <tr>
              <td><h3>Visitors</td>
              <td>
                <h4 style="color:red">Least Squares Regression Line: y = <?php echo number_format($visitorStats['b0'], 4); ?> + <?php echo number_format($visitorStats['b1'], 4); ?>x</h4>
                <h4 style="color:red">Correlation Coefficient: r = <?php echo number_format($visitorStats['r'], 4); ?></h4>
                <h4>Population Size: <?php echo count($visitorX); ?></h4>
                <h4>Average X-Value: <?php echo number_format($visitorStats['avgX'], 3); ?></h4>
                <h4>Variance of X: <?php echo number_format($visitorStats['varX'], 3); ?></h4>
                <h4>Average Y-Value: <?php echo number_format($visitorStats['avgY'], 3); ?></h4>
                <h4>Variance of Y: <?php echo number_format($visitorStats['varY'], 3); ?></h4>
                <h4>Beta<sub>0</sub>: <?php echo number_format($visitorStats['b0'], 4); ?></h4>
                <h4>Beta<sub>1</sub>: <?php echo number_format($visitorStats['b1'], 4); ?></h4>
              </td>
              <td>
                <h4>t-Score (<i>T<sub>0</sub></i>): <?php echo number_format($visitorT0, 4); ?></h4>
                <h4>Degrees of Freedom: <?php echo count($visitorX) - 2; ?></h4>
                <h4 style="color:red" id="visitor_t-pvalue"></h4>
              </td>
              <td>
                <h4>F-Statistic (<i>F<sub>0</sub></i>): <?php echo number_format($visitorF0, 4); ?></h4>
                <h4>Numerator Degrees of Freedom: 1</h4>
                <h4>Denominator Degrees of Freedom: <?php echo count($visitorX) - 2; ?></h4>
                <h4 style="color:red" id="visitor_f-pvalue"></h4>
              </td>
            </tr>
        </table>
      </center></div><br>
    <?php } ?>

    <?php
  }
  ?>
