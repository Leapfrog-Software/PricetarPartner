<?php

require "./user.php";

date_default_timezone_set("Asia/Tokyo");

logSave();

$command = getPostParam("command");

if (strcmp($command, "registerUser") == 0) {
	registerUser();
} else if (strcmp($command, "login") == 0) {
	login();
} else if (strcmp($command, "getUser") == 0) {
	getUser();
} else if (strcmp($command, "updateClientProfile") == 0) {
	updateClientProfile();
} else if (strcmp($command, "updatePartnerProfile") == 0) {
	updatePartnerProfile();
}
else {
	echo("unknown");
}

function registerUser() {

	$email = getPostParam("email");
	$password = getPostParam("password");

	$userId = User::create($email, $password);
	if (!is_null($userId)) {
		echo(json_encode(Array("result" => "0", "userId" => $userId)));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function login() {

	$email = getPostParam("email");
	$password = getPostParam("password");

	$userId = User::login($email, $password);
	if (!is_null($userId)) {
		echo(json_encode(Array("result" => "0", "userId" => $userId)));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function getUser() {

	$userId = getPostParam("userId");
	User::updateLoginDatetime($userId);

	$users = [];
	foreach (User::readAll() as $user) {
		$userResponse = $user->toApiResponse();
		$users[] = $userResponse;
	}
	echo(json_encode(Array("result" => "0", "users" => $users)));
}

function updateClientProfile() {

	$userId = getPostParam("userId");
	$nickname = getPostParam("nickname");
	$area = getPostParam("area");
	$useFrequency = getPostParam("useFrequency");
	$newCondition = getPostParam("newCondition");
	$oldCondition = getPostParam("oldCondition");
	$genres = getPostParam("genres");
	$options = getPostParam("options");
	$message = getPostParam("message");
	$image = getPostParam("image");

	if (User::updateClientProfile($userId, $nickname, $area, $useFrequency, $newCondition, $oldCondition, $genres, $options, $message, $image)) {
		echo(json_encode(Array("result" => "0")));
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}

function updatePartnerProfile() {

	$userId = getPostParam("userId");
	$nickname = getPostParam("nickname");
	$area = getPostParam("area");
	$career = getPostParam("career");
	$status = getPostParam("status");
	$newPrice = getPostParam("newPrice");
	$oldPrice = getPostParam("oldPrice");
	$dangerousPrice = getPostParam("dangerousPrice");
	$dangerousMessage = getPostParam("dangerousMessage");
	$bigPrice = getPostParam("bigPrice");
	$bigMessage = getPostParam("bigMessage");
	$inspectionPrice = getPostParam("inspectionPrice");
	$inspectionMessage = getPostParam("inspectionMessage");
	$message = getPostParam("message");
	$image = getPostParam("image");

	if (User::updatePartnerProfile($userId, $nickname, $area, $career, $status, $newPrice, $oldPrice, $dangerousPrice, $dangeroudMessage, $bigPrice, $bigMessage, $inspectionPrice, $inspectionMessage, $message, $image)) {
		echo(json_encode(Array("result" => "0")));		
	} else {
		echo(json_encode(Array("result" => "1")));
	}
}




function getParam($key) {
    return str_replace(" ", "+", $_GET[$key]);
}

function getPostParam($key) {
    return str_replace(" ", "+", $_POST[$key]);
}

function debugSave($str){

	$text = date("Y-m-d H:i:s") . " " . $str . "\r\n";
	file_put_contents("debug.txt", $text, FILE_APPEND);
}

function logSave() {

	$params = "";
	foreach ($_POST as $key=>$val) {
		if (strlen($params) > 0) {
			$params .= "&";
		}
    	$params .= ($key . "=" . $val);
	}

	$filePath = "./log/" . date("Ymd") . ".txt";
	file_put_contents($filePath, date("H:i:s") . " " . $params . "\n", FILE_APPEND);
}

?>
