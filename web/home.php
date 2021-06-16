<?php
require "definitions.php";

// starting a session to hold login state
session_start();

class Home
{

    private $token, $device_id, $location;
    private mysqli $sql;

    private function isConnected()
    {
        $this->sql = new mysqli(serverName, userName, userPassword, databaseName);
        return $this->sql->connect_error ? false : true;
    }

    private function deviceExists($value) {
        // select device_data.device_id from users inner join device_data on users.uid = device_data.token where device_data.device_id = 'vivo_1904-123456789'
        $str = "select * from ".dataTableName." where `device_id` = $value";
        return $this->sql->query($str)->num_rows == 1 ? true: false;
    }

    /**
    @parameters:
       token - unique 16 character token assigned to each user on registration
       device_id - unique device identifier obtained from app

    @return:
        boolean value representing whether addition was successful
   */
    public function addDevice($token, $device_id)
    {

    }
}

if (isset($_SESSION["loggedIn"]) and $_SESSION['loggedIn'])
    echo "SUCCESS";
else
    die("ACCESS DENIED")
?>