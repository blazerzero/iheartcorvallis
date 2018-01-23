<?php
/* Title : Ajax Pagination with jQuery & PHP
Example URL : http://www.sanwebe.com/2013/03/ajax-pagination-with-jquery-php */

$mysqli = new mysqli("oniddb.cws.oregonstate.edu","habibelo-db","RcAbWdWDkpj7XNTL","habibelo-db");
//Output any connection error
if ($mysqli->connect_error) {
    die('Error : ('. $mysqli->connect_errno .') '. $mysqli->connect_error);
}


?>
