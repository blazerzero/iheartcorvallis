<?php

    $con = mysqli_connect("oniddb.cws.oregonstate.edu", "habibelo-db", "RcAbWdWDkpj7XNTL", "habibelo-db");

    if (mysqli_connect_errno($con)) {
        echo "Failed to connected to MySQL: " . mysqli_connect_error();
    }

    $username = $_POST['username'];

?>