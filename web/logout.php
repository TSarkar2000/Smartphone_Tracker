<?php
session_start();
if (isset($_SESSION["loggedIn"]) and $_SESSION["loggedIn"]) {
    $_SESSION["loggedIn"] = false;
    session_destroy();
}
echo "LOGGED OUT";
?>