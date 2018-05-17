<?php
	//	firstname=Waheed&lastname=Rahuman&payment_id=1001
	require_once("dbcontroller.php");
	$db_handle = new DBController();

	$payment_id = $_GET["payment_id"];
	$firstname = $_GET["firstname"];
	$lastname = $_GET["lastname"];

	$result = $db_handle->updatePayment($firstname,$lastname,$payment_id);

	echo $result;
?>


