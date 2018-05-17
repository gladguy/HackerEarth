<?php
class DBController {
	private $host = "localhost";
	private $user = "waheed_water";
	private $password = "rehan123";
	private $database = "waheed_puliyal";

	//private $user = "root";
	//private $database = "test";

	
	function __construct() {
		$conn = $this->connectDB();
		if(!empty($conn)) {
			$this->selectDB($conn);
		}
	}
	
	function connectDB() {
		$conn = mysql_connect($this->host,$this->user,$this->password);
		return $conn;
	}
	
	function selectDB($conn) {
		mysql_select_db($this->database,$conn);
	}

	function createPayment($amount)
	{
			$sql = "INSERT INTO payment_status (FirstName, LastName, Amount, Paid_Date, Status ) VALUES('NOT PAID','NOT PAID', ".$amount.", now(), FALSE )";

			if (mysql_query($sql) === TRUE)
			{
				$result = mysql_query("SELECT MAX(PaymentID) FROM payment_status");
				$row = mysql_fetch_row($result);
				$highest_id = $row[0];
				return $highest_id;
			} 
			else
			{
				return 0;
			}
	}
	function checkPayment($payment_id)
	{
			$cursor = mysql_query("SELECT Status,LastName FROM payment_status where PaymentID=".$payment_id);
			$row = mysql_fetch_row($cursor);
			$highest_id = $row[0];
			return $highest_id;
	}
	function updatePayment($firstname,$lastname,$payment_id)
	{
			$sql = "update payment_status set Status = TRUE,FirstName='".$firstname."',LastName='".$lastname."' where PaymentID=".$payment_id;

			if (mysql_query($sql) === TRUE)
			{
				return 1; //Success
			} 
			else
			{
				return 0; //Failed
			}
	}



	function runQuery($query) {
		$result = mysql_query($query);
		while($row=mysql_fetch_assoc($result)) {
			$resultset[] = $row;
		}		
		if(!empty($resultset))
			return $resultset;
	}
	
	function numRows($query) {
		$result  = mysql_query($query);
		$rowcount = mysql_num_rows($result);
		return $rowcount;	
	}
}
?>