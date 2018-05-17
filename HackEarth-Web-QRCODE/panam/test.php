<?php    
include "qrlib.php";    

QRcode::png('code data text', 'filename.png'); // creates file 
QRcode::png('some othertext 1234'); // creates code image and outputs it directly into browser

?>