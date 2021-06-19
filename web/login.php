<?php
require "definitions.php";

// starting a session to hold login state
session_start();

class Login
{
    private $email, $password;
    private mysqli $sql;

    public function __construct($email, $password)
    {
        $this->email = $email;
        $this->password = hash('sha256', $password);
    }

    private function isConnected()
    {
        $this->sql = new mysqli(serverName, userName, userPassword, databaseName);
        return $this->sql->connect_error ? false : true;
    }

    private function checkIfDataExists()
    {
        $str = "select * from " . tableName . " where `email` = '$this->email' and `password` = '$this->password'";
        return $this->sql->query($str)->num_rows == 1 ? true : false;
    }

    private function fetchData()
    {
        $obj = $this->sql->query("select `name`, `uid`, `device_count` from " . tableName . " where `email`= '$this->email'")->fetch_object();
        $arr = $this->sql->query("select `device_info` from " . dataTableName . " where `token`= '$obj->uid'")->fetch_all();
        $x = array("user_info" => $obj, "device_info" => $arr);
        echo json_encode($x);
    }

    public function begin()
    {
        if ($this->isConnected())
            if ($this->checkIfDataExists()) {
                $_SESSION['loggedIn'] = true;
                echo "SUCCESS";
                $this->fetchData();
            } else
                echo "Account doesn't exist";
        else
            echo "Couldn't connect to database";
    }

}

if (isset($_POST["email"]) and isset($_POST["password"])) {
    $log = new Login($_POST["email"], $_POST["password"]);
    $log->begin();
} else
    die("Invalid URL");

?>