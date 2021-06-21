<?php
require "definitions.php";

// starting a session to hold login state
session_start();

class Home
{
    public static $ADD_TO_TRACKING = 1;
    public static $GET_LOCATION = 2;

    private $token, $deviceID;
    private mysqli $sql;

    public function __construct($token, $deviceID)
    {
        $this->token = $token;
        $this->deviceID = $deviceID;
    }

    private function isConnected()
    {
        $this->sql = new mysqli(serverName, userName, userPassword, databaseName);
        return $this->sql->connect_error ? false : true;
    }

    private function checkIfDataExists($table, $param, $val)
    {
        $str = "select * from " . $table . " where `$param` = '$val'";
        return $this->sql->query($str)->num_rows > 0 ? true : false;
    }

    private function getOwnerToken()
    {
        // non - owners will have a null device_info & location column
        $str = "select distinct `token` from " . dataTableName . " where `device_id` = '$this->deviceID' and `device_info` is not null";
        $res = $this->sql->query($str);
        return $res->num_rows > 0 ? $res->fetch_object()->token : null;
    }

    /*
     * ACCOUNT exists, DEVICE doesn't exist: use old token & insert into device_data, update users table
     * ACCOUNT exists, DEVICE exists: reject & redirect to login
     *
     * This will only work if a device already exists in the database (either registered by you or someone else).
     * Device's info & location would be stored in their respective owner's databases & others will just hold a
     * reference to it.
     *
     * params:
     *      token - your 16 character uid
     *      deviceID - add the id of the device to track (available only if it is registered)
     *
     */

    private function addDeviceToTracking()
    {
        // add device if not owned by you (owners will be able to track their devices by default)
        $owner = $this->getOwnerToken();
        if ($owner != null)
            if (strcmp($this->token, $owner) != 0) {
                $obj = $this->sql->query("select `device_count` from " . tableName . " where `uid` = '$this->token'")->fetch_object();
                $x = $obj->device_count + 1;

                $str = "update " . tableName . " set `device_count` = '$x' where `uid` = '$this->token';";
                $str .= "insert into " . dataTableName . " (`token`, `device_id`) VALUES ('$this->token', '$this->deviceID');";

                if ($this->sql->multi_query($str))
                    echo "SUCCESS";

                else die("FAILED");
            } else
                die("Already added");
        else
            die("No owners. Device may not have been registered.");
    }

    private function getLocation() {
        // to be implemented yet
    }

    public function begin($task)
    {
        if($this->isConnected()) {
            $task == Home::$ADD_TO_TRACKING ? $this->addDeviceToTracking(): $this->getLocation();
        } else
            die("Couldn't connect to database");
    }
}

if (isset($_SESSION["loggedIn"]) and $_SESSION['loggedIn']) {
    if (isset($_POST["task"]) and isset($_POST["token"]) and isset($_POST["deviceID"])) {
        $x = new Home($_POST["token"], $_POST["deviceID"]);
        if (strcmp($_POST["task"], "add") == 0)
            $x->begin(Home::$ADD_TO_TRACKING);
        else if (strcmp($_POST["task"], "getLoc") == 0)
            $x->begin(Home::$GET_LOCATION);
        else
            die("Unsupported task");
    } else
        die("Invalid URL");
} else
    die("ACCESS DENIED")
?>