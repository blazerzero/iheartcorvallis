<?php

$mysqli = new mysqli("oniddb.cws.oregonstate.edu","habibelo-db","RcAbWdWDkpj7XNTL","habibelo-db");

if(!$mysqli || $msqli->connect_errno){
    echo "Connection error " . $mysqli->connect_errno . " " . $mysqli->connect_error;
} else {
    echo "Connected!!!";
}

$mysqli->close();
?>
