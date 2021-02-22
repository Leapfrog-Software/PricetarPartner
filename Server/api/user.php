<?php

class User {

	const PROFILE_TYPE_NONE = "0";
	const PROFILE_TYPE_CLIENT = "1";
	const PROFILE_TYPE_PARTNER = "2";

	public $id;
	public $email;
	public $password;
	public $nickname;
	public $area;
	public $profileType;
	public $clientUseFrequency;
	public $clientCondition;
	public $clientGenres;
	public $clientOptions;
	public $clientMessage;
	public $partnerCareer;
	public $partnerStatus;
	public $partnerNewPrice;
	public $partnerOldPrice;
	public $partnerDangerousPrice;
	public $partnerDangerousMessage;
	public $partnerInspectionPrice;
	public $partnerInspectionMessage;
	public $partnerMessage;
	public $loginDatetime;
	public $deviceToken;

	static function initFromFilePath($filePath) {

		if (!file_exists($filePath)) {
			return null;
		}
		$fileData = file_get_contents($filePath);
		if ($fileData === false) {
			return null;
		}

		$datas = explode("\n", $fileData);
		if (count($datas) >= 22) {
			$data = new User();
			$data->id = $datas[0];
			$data->email = $datas[1];
			$data->password = $datas[2];
			$data->nickname = $datas[3];
			$data->area = $datas[4];
			$data->profileType = $datas[5];
			$data->clientUseFrequency = $datas[6];
			$data->clientCondition = $datas[7];
			$data->clientGenres = $datas[8];
			$data->clientOptions = $datas[9];
			$data->clientMessage = $datas[10];
			$data->partnerCareer = $datas[11];
			$data->partnerStatus = $datas[12];
			$data->partnerNewPrice = $datas[13];
			$data->partnerOldPrice = $datas[14];
			$data->partnerDangerousPrice = $datas[15];
			$data->partnerDangerousMessage = $datas[16];
			$data->partnerInspectionPrice = $datas[17];
			$data->partnerInspectionMessage = $datas[18];
			$data->partnerMessage = $datas[19];
			$data->loginDatetime = $datas[20];
			$data->deviceToken = $datas[21];
			return $data;
		}
		return null;
	}

	function toFileString() {

		return $this->id . "\n"
				. $this->email . "\n"
				. $this->password . "\n"
				. $this->nickname . "\n"
				. $this->area . "\n"
				. $this->profileType . "\n"
				. $this->clientUseFrequency . "\n"
				. $this->clientCondition . "\n"
				. $this->clientGenres . "\n"
				. $this->clientOptions . "\n"
				. $this->clientMessage . "\n"
				. $this->partnerCareer . "\n"
				. $this->partnerStatus . "\n"
				. $this->partnerNewPrice . "\n"
				. $this->partnerOldPrice . "\n"
				. $this->partnerDangerousPrice . "\n"
				. $this->partnerDangerousMessage . "\n"
				. $this->partnerInspectionPrice . "\n"
				. $this->partnerInspectionMessage . "\n"
				. $this->partnerMessage . "\n"
				. $this->loginDatetime . "\n"
				. $this->deviceToken;
	}

	function toApiResponse() {

		return Array("id" => $this->id,
					 "nickname" => $this->nickname,
					 "area" => $this->area,
					 "profileType" => $this->profileType,
					 "clientUseFrequency" => $this->clientUseFrequency,
					 "clientCondition" => $this->clientCondition,
					 "clientGenres" => $this->clientGenres,
					 "clientOptions" => $this->clientOptions,
					 "clientMessage" => $this->clientMessage,
					 "partnerCareer" => $this->partnerCareer,
					 "partnerStatus" => $this->partnerStatus,
					 "partnerNewPrice" => $this->partnerNewPrice,
					 "partnerOldPrice" => $this->partnerOldPrice,
					 "partnerDangerousPrice" => $this->partnerDangerousPrice,
					 "partnerDangerousMessage" => $this->partnerDangerousMessage,
					 "partnerInspectionPrice" => $this->partnerInspectionPrice,
					 "partnerInspectionMessage" => $this->partnerInspectionMessage,
					 "partnerMessage" => $this->partnerMessage,
					 "loginDatetime" => $this->loginDatetime);
	}

	function save() {
		return file_put_contents("../data/user/" . $this->id . "/info.txt", $this->toFileString()) !== false;
	}

	static function readAll() {

		$datas = [];

		foreach (glob("../data/user/*", GLOB_BRACE) as $dir) {
			$data = User::initFromFilePath($dir . "/info.txt");
			if (!is_null($data)) {
				$datas[] = $data;
			}
		}
		return $datas;
	}

	static function read($id) {

		$filePath = "../data/user/" . $id . "/info.txt";
		return User::initFromFilePath($filePath);
	}

	static function create($email, $password) {

		$current = date("YmdHis");
		$userId = $current . substr(str_shuffle("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"), 0, 8);

		$user = new User();
		$user->id = $userId;
		$user->email = $email;
		$user->password = $password;
		$user->nickname = "";
		$user->area = "";
		$user->profileType = User::PROFILE_TYPE_NONE;
		$user->clientUseFrequency = "";
		$user->clientCondition = "";
		$user->clientGenres = "";
		$user->clientOptions = "";
		$user->clientMessage = "";
		$user->partnerCareer = "";
		$user->partnerStatus = "";
		$user->partnerNewPrice = "";
		$user->partnerOldPrice = "";
		$user->partnerDangerousPrice = "";
		$user->partnerDangerousMessage = "";
		$user->partnerInspectionPrice = "";
		$user->partnerInspectionMessage = "";
		$user->partnerMessage = "";
		$user->loginDatetime = $current;
		$user->deviceToken = "";

		$mask = umask();
		umask(000);
		mkdir("../data/user/" . $userId, 0777, true);
		umask($mask);

		if ($user->save()) {
			return $userId;
		}
		return null;
	}

	static function updateClientProfile($userId, $nickname, $area, $useFrequency, $condition, $genres, $options, $message, $image) {

		$user = User::read($userId);
		if (is_null($user)) {
			return false;
		}
		$user->nickname = $nickname;
		$user->area = $area;
		$user->profileType = User::PROFILE_TYPE_CLIENT;
		$user->clientUseFrequency = $useFrequency;
		$user->clientCondition = $condition;
		$user->clientGenres = $genres;
		$user->clientOptions = $options;
		$user->clientMessage = $message;

		if (strlen($image) > 0) {
			if (file_put_contents("../data/user/" . $userId . "/image", base64_decode($image)) === false) {
				return false;
			}
		}
		return $user->save();
	}

	static function updatePartnerProfile($userId, $nickname, $area, $career, $status, $newPrice, $oldPrice, $dangerousPrice, $dangeroudMessage, $inspectionPrice, $inspectionMessage, $message, $image) {

		$user = User::read($userId);
		if (is_null($user)) {
			return false;
		}
		$user->nickname = $nickname;
		$user->area = $area;
		$user->profileType = User::PROFILE_TYPE_PARTNER;
		$user->partnerCareer = $career;
		$user->partnerStatus = $status;
		$user->partnerNewPrice = $newPrice;
		$user->partnerOldPrice = $oldPrice;
		$user->partnerDangerousPrice = $dangerousPrice;
		$user->partnerDangerousMessage = $dangeroudMessage;
		$user->partnerInspectionPrice = $inspectionPrice;
		$user->partnerInspectionMessage = $inspectionMessage;
		$user->partnerMessage = $message;

		if (strlen($image) > 0) {
			if (file_put_contents("../data/user/" . $userId . "/image", base64_decode($image)) === false) {
				return false;
			}
		}
		return $user->save();
	}

	static function login($email, $password) {

		foreach (User::readAll() as $user) {
			if ((strcmp($user->email, $email) == 0) && (strcmp($user->password, $password) == 0)) {
				return $user->id;
			}
		}
		return null;
	}

	static function updateLoginDatetime($userId) {

		$user = User::read($userId);
		if (is_null($user)) {
			return false;
		}
		$user->loginDatetime = date("YmdHis");
		return $user->save();
	}

	static function updateDeviceToken($userId, $deviceToken) {

		$user = User::read($userId);
		if (is_null($user)) {
			return false;
		}
		$user->deviceToken = $deviceToken;
		return $user->save();
	}
}

?>
