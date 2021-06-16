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
        $str = "select `name`, `uid`, `device_count` from " . tableName . " where `email`= '$this->email'";
        $obj = $this->sql->query($str)->fetch_object();
        echo json_encode($obj);
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

if (isset($_GET["email"]) and isset($_GET["password"])) {
    $log = new Login($_GET["email"], $_GET["password"]);
    $log->begin();
} else
    die("Invalid URL");

?>