<?php
require "definitions.php";

class Registration
{
    // variable declarations
    private $name, $email, $password, $token;
    private mysqli $sql;

    public function __construct($name, $email, $password)
    {
        $this->name = $name;
        $this->email = $email;
        $this->password = hash('sha256', $password);
    }

    private function isConnected()
    {
        $this->sql = new mysqli(serverName, userName, userPassword, databaseName);
        return $this->sql->connect_error ? false : true;
    }

    private function checkIfDataExists($col, $value)
    {
        $str = "select * from " . tableName . " where `$col` = '$value'";
        return $this->sql->query($str)->num_rows == 0 ? false : true;
    }

    private function generateNewToken()
    {
        // generate a 16 character random token
        $characters = "9876543210abcdefghijklmnopqrstuvwxyzABCDEFGHJIJKLMNOPQRSTUVWXYZ!@#$%^&*()<>[]{}";
        $charactersLength = strlen($characters);
        $randString = "";
        for ($i = 0; $i < 16; $i++)
            $randString .= $characters[rand(0, $charactersLength - 1)];
        return $randString;
    }

    private function insertData()
    {
        $this->token = $this->generateNewToken();
        // keep looping until a brand new token is obtained
        while ($this->checkIfDataExists("uid", $this->token))
            $this->token = $this->generateNewToken();
        // insert into database
        $str = "insert into " . tableName . "(`id`, `name`, `email`, `password`, `uid`, `device_count`, `timestamp`) 
                VALUES (NULL, '$this->name', '$this->email', '$this->password', '$this->token', '0', NULL);";

        $res = $this->sql->query($str);
        return $res;
    }

    public function begin()
    {
        if ($this->isConnected()) {
            if (!$this->checkIfDataExists("email", $this->email)) {
                if ($this->insertData())
                    echo "SUCCESS";
                else
                    echo "Sorry, please try again later";
            } else
                echo "Account already exists";
        } else
            echo "Couldn't connect to database";
    }
}

$reg = new Registration($_GET["name"], $_GET["email"], $_GET["password"]);
$reg->begin();
?>
