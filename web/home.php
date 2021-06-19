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
        //
        $str = "select * from ".dataTableName." where `device_id` = $value";
        return $this->sql->query($str)->num_rows == 1 ? true: false;
    }

    /*
     * ACCOUNT doesn't exist, DEVICE doesn't exist: generate new token & insert to both tables
     * ACCOUNT doesn't exist, DEVICE exists: reject
     * ACCOUNT exists, DEVICE doesn't exist: use old token & insert into device_data, update users table
     * ACCOUNT exists, DEVICE exists: reject & redirect to login
     * todo : problem here
     */
    private function insertData()
    {
        $str = "";
        if (!$this->checkIfDataExists(dataTableName, "deviceID", $this->deviceID)) {
            if (!$this->checkIfDataExists(tableName, "email", $this->email)) {
                // get new token
                $this->token = $this->generateNewToken();
                // insert into users table
                $str .= "insert into " . tableName . "(`id`, `name`, `email`, `password`, `uid`, `device_count`, `timestamp`) 
                VALUES (NULL, '$this->name', '$this->email', '$this->password', '$this->token', '1', NULL);";
            } else {
                $obj = $this->sql->query("select `uid`, `device_count` from " . tableName . " where `email` = '$this->email'")->fetch_object();
                // get old token
                $this->token = $obj->uid;
                $x = $obj->device_count + 1;
                $str .= "update " . tableName . " set `device_count` = '$x' where `email` = '$this->email';";
            }

            $str .= "insert into " . dataTableName . " (`token`, `device_id`, `device_info`) VALUES ('$this->token', '$this->deviceID', '$this->deviceInfo');";
            return $this->sql->multi_query($str);
        }

        return false;
    }
}

if (isset($_SESSION["loggedIn"]) and $_SESSION['loggedIn'])
    echo "SUCCESS";
else
    die("ACCESS DENIED")
?>