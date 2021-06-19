<?php
require "definitions.php";

class Registration
{
    // variable declarations
    private $name, $email, $password, $token, $deviceID, $deviceInfo;
    private mysqli $sql;

    public function __construct($name, $email, $password, $deviceID, $deviceInfo)
    {
        $this->name = $name;
        $this->email = $email;
        $this->password = hash('sha256', $password);
        $this->deviceID = $deviceID;
        $this->deviceInfo = $deviceInfo;
    }

    private function isConnected()
    {
        $this->sql = new mysqli(serverName, userName, userPassword, databaseName);
        return $this->sql->connect_error ? false : true;
    }

    private function checkIfDataExists($table, $col, $value)
    {
        $str = "select * from " . $table . " where `$col` = '$value'";
        return $this->sql->query($str)->num_rows == 0 ? false : true;
    }

    private function generateNewToken()
    {
        // generate a 16 character random token
        $characters = "9876543210abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()<>[]{}";
        $charactersLength = strlen($characters);
        $randString = "";
        for ($i = 0; $i < 16; $i++)
            $randString .= $characters[rand(0, $charactersLength - 1)];

        while ($this->checkIfDataExists(tableName, "uid", $randString))
            $randString = $this->generateNewToken();

        return $randString;
    }

    private function insertData() {
        $this->token = $this->generateNewToken();
        // add to users table
        $str = "insert into " . tableName . "(`id`, `name`, `email`, `password`, `uid`, `device_count`, `timestamp`) 
                VALUES (NULL, '$this->name', '$this->email', '$this->password', '$this->token', '1', NULL);";
        // add to device_data table
        $str .= "insert into " . dataTableName . " (`token`, `device_id`, `device_info`) VALUES ('$this->token', '$this->deviceID', '$this->deviceInfo');";

        return $this->sql->multi_query($str);
    }

    public function begin()
    {
        if ($this->isConnected()) {
            if(!$this->checkIfDataExists(tableName,'email', $this->email))
                if(!$this->checkIfDataExists(dataTableName, 'device_id', $this->deviceID))
                    if ($this->insertData())
                        echo "SUCCESS";
                    else
                        echo "Error";
                else
                    die("Device already registered");
            else
                die("Account already exists, please login.");
        } else
            die("Couldn't connect to database");
    }
}

if (isset($_POST["name"]) and isset($_POST["email"]) and isset($_POST["password"]) and isset($_POST["deviceID"]) and isset($_POST["deviceInfo"])) {
    $reg = new Registration($_POST["name"], $_POST["email"], $_POST["password"], $_POST["deviceID"], $_POST["deviceInfo"]);
    $reg->begin();
} else
    die("Invalid URL");

?>
