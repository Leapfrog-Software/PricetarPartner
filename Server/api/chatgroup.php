<?php

class ChatGroup {

	public $id;
	public $userId1;
	public $userId2;
	public $unreadCount1;
	public $unreadCount2;

	static function initFromFilePath($filePath) {

		if (!file_exists($filePath)) {
			return null;
		}
		$fileData = file_get_contents($filePath);
		if ($fileData === false) {
			return null;
		}
		$datas = explode("\n", $fileData);
		if (count($datas) >= 5) {
			$data = new ChatGroup();
			$data->id = $datas[0];
			$data->userId1 = $datas[1];
			$data->userId2 = $datas[2];
			$data->unreadCount1 = $datas[3];
			$data->unreadCount2 = $datas[4];
			return $data;
		}
		return null;
	}

	function toApiResponse() {

		return Array("id" => $this->id,
					 "userId1" => $this->userId1,
					 "userId2" => $this->userId2,
					 "unreadCount1" => $this->unreadCount1,
					 "unreadCount2" => $this->unreadCount2);
	}

	function toFileString() {

		return $this->id . "\n"
				. $this->userId1 . "\n"
				. $this->userId2 . "\n"
				. $this->unreadCount1 . "\n"
				. $this->unreadCount2;
	}

	static function readAll() {

		$datas = [];

		foreach (glob("../data/chat/*", GLOB_BRACE) as $dir) {
			$chatGroupData = ChatGroup::initFromFilePath($dir . "/info.txt");
			if (!is_null($chatGroupData)) {
				$datas[] = $chatGroupData;
			}
		}
		return $datas;
	}

	static function read($userId1, $userId2) {

		foreach (ChatGroup::readAll() as $chatGroup) {
			if ((strcmp($chatGroup->userId1, $userId1) == 0) && (strcmp($chatGroup->userId2, $userId2) == 0)) {
				return $chatGroup;
			}
			if ((strcmp($chatGroup->userId1, $userId2) == 0) && (strcmp($chatGroup->userId2, $userId1) == 0)) {
				return $chatGroup;
			}
		}
		return null;
	}

	function save() {
		return file_put_contents("../data/chat/" . $this->id . "/info.txt", $this->toFileString()) !== false;
	}

	static function createIfNotExist($sender, $target) {

		$existChatGroup = ChatGroup::read($sender, $target);
		if (!is_null($existChatGroup)) {
			return $existChatGroup->id;
		}

		$chatGroupId = date("YmdHis") . substr(str_shuffle("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"), 0, 8);

		$mask = umask();
		umask(000);
		mkdir("../data/chat/" . $chatGroupId, 0777, true);
		umask($mask);

		$chatGroup = new ChatGroup();
		$chatGroup->id = $chatGroupId;
		$chatGroup->userId1 = $sender;
		$chatGroup->userId2 = $target;
		$chatGroup->unreadCount1 = "0";
		$chatGroup->unreadCount2 = "0";

		if ($chatGroup->save()) {
			return $chatGroup->id;
		} else {
			return null;
		}
	}

	static function incrementUnreadCount($sender, $target) {

		$chatGroup = ChatGroup::read($sender, $target);
		if (is_null($chatGroup)) {
			return false;
		}

		if (strcmp($chatGroup->userId1, $sender) == 0) {
			$chatGroup->unreadCount2 = strval(intval($chatGroup->unreadCount2) + 1);
		} else {
			$chatGroup->unreadCount1 = strval(intval($chatGroup->unreadCount1) + 1);
		}
		return $chatGroup->save();
	}
}

?>
