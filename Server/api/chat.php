<?php

class Chat {

	const CHAT_TYPE_MESSAGE = "0";
	const CHAT_TYPE_IMAGE = "1";

	public $id;
	public $groupId;
	public $senderId;
	public $targetId;
	public $datetime;
	public $type;
	public $message;

	static function initFromString($string) {

		$datas = explode(",", $string);
		if (count($datas) >= 7) {
			$data = new Chat();
			$data->id = $datas[0];
			$data->groupId = $datas[1];
			$data->senderId = $datas[2];
			$data->targetId = $datas[3];
			$data->datetime = $datas[4];
			$data->type = $datas[5];
			$data->message = $datas[6];
			return $data;
		}
		return null;
	}

	function toFileString() {

		return $this->id . ","
				. $this->groupId . ","
				. $this->senderId . ","
				. $this->targetId . ","
				. $this->datetime . ","
				. $this->type . ","
				. $this->message . "\n";
	}

	function toApiResponse() {

		return Array("id" => $this->id,
					 "groupId" => $this->groupId, 
					 "senderId" => $this->senderId,
					 "targetId" => $this->targetId,
					 "datetime" => $this->datetime,
					 "type" => $this->type,
					 "message" => $this->message);
	}

	static function readAll($groupId) {

		$datas = [];

		$filePath = "../data/chat/" . $groupId . "/chat.txt";
		if (file_exists($filePath)) {
			$fileData = file_get_contents($filePath);
			if ($fileData !== false) {
				foreach (explode("\n", $fileData) as $line) {
					$chat = Chat::initFromString($line);
					if (!is_null($chat)) {
						$datas[] = $chat;
					}
				}
			}
		}
		return $datas;
	}

	static function postMessage($groupId, $senderId, $targetId, $message) {

		$current = date("YmdHis");
		$chatId = $current . substr(str_shuffle("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"), 0, 8);

		$chat = new Chat();
		$chat->id = $chatId;
		$chat->groupId = $groupId;
		$chat->senderId = $senderId;
		$chat->targetId = $targetId;
		$chat->datetime = $current;
		$chat->type = Chat::CHAT_TYPE_MESSAGE;
		$chat->message = $message;

		$filePath = "../data/chat/" . $groupId . "/chat.txt";
		return file_put_contents($filePath, $chat->toFileString(), FILE_APPEND) !== false;
	}

	static function postImage($groupId, $senderId, $targetId, $image) {

		$current = date("YmdHis");
		$chatId = $current . substr(str_shuffle("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"), 0, 8);

		$chat = new Chat();
		$chat->id = $chatId;
		$chat->groupId = $groupId;
		$chat->senderId = $senderId;
		$chat->targetId = $targetId;
		$chat->datetime = $current;
		$chat->type = Chat::CHAT_TYPE_IMAGE;
		$chat->message = "";

		if (file_put_contents("../data/chat/" . $groupId . "/" . $chatId, base64_decode($image)) === false) {
			return false;
		}

		$filePath = "../data/chat/" . $groupId . "/chat.txt";
		return file_put_contents($filePath, $chat->toFileString(), FILE_APPEND) !== false;
	}
}

?>
