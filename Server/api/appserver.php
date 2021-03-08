<?php

require "./user.php";
require "./chatgroup.php";
require "./chat.php";

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
} else if (strcmp($command, "getChatGroup") == 0) {
	getChatGroup();
} else if (strcmp($command, "getChat") == 0) {
	getChat();
} else if (strcmp($command, "postChatMessage") == 0) {
	postChatMessage();
} else if (strcmp($command, "postChatImage") == 0) {
	postChatImage();
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

function getChatGroup() {

	$userId = getPostParam("userId");

	$chatGroups = [];
	foreach (ChatGroup::readAll() as $chatGroup) {
		if ((strcmp($chatGroup->userId1, $userId) == 0) || (strcmp($chatGroup->userId2, $userId2) == 0)) {
			$chatGroups[] = $chatGroup->toApiResponse();
		}
	}
	echo(json_encode(Array("result" => "0", "chatGroups" => $chatGroups)));
}

function getChat() {

	$userId1 = getPostParam("userId1");
	$userId2 = getPostParam("userId2");

	$chatGroup = ChatGroup::read($userId1, $userId2);

	$chats = [];
	if (!is_null($chatGroup)) {
		foreach (Chat::readAll($chatGroup->id) as $chat) {
			$chats[] = $chat->toApiResponse();
		}
	}
	echo(json_encode(Array("result" => "0", "chats" => $chats)));
}

function postChatMessage() {

	$senderId = getPostParam("senderId");
	$targetId = getPostParam("targetId");
	$message = getPostParam("message");

//	debugSave("sender: " . $senderId . ", target: " . $targetId);

	$chatGroupId = ChatGroup::createIfNotExist($senderId, $targetId);
	if (is_null($chatGroupId)) {
		echo(json_encode(Array("result" => "2")));
		return;
	}

	if (ChatGroup::incrementUnreadCount($senderId, $targetId)) {
		if (Chat::postMessage($chatGroupId, $senderId, $targetId, $message)) {
			echo(json_encode(Array("result" => "0")));
			return;
		}
	}
	echo(json_encode(Array("result" => "1")));
}

function postChatImage() {

	$senderId = getPostParam("senderId");
	$targetId = getPostParam("targetId");
	$image = getPostParam("image");

	$chatGroupId = ChatGroup::createIfNotExist($senderId, $targetId);
	if (is_null($chatGroupId)) {
		echo(json_encode(Array("result" => "2")));
		return;
	}

	if (ChatGroup::incrementUnreadCount($senderId, $targetId)) {
		if (Chat::postImage($chatGroupId, $senderId, $targetId, $image)) {
			echo(json_encode(Array("result" => "0")));
			return;
		}
	}	
	echo(json_encode(Array("result" => "1")));
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
