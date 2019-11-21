<?php 
$Cn = mysqli_connect("localhost","root","","eleccion")or die("server no encontrado");
mysqli_set_charset($Cn,"utf8");
    $method=$_SERVER['REQUEST_METHOD'];
    $response = array();
    $result = mysqli_query($Cn,"SELECT * from usuario");
    if (!empty($result)) {
        $response["success"] = "202";
        $response["message"] = "Alumnos encontrados";

        $response["usuario"] = array();
        foreach ($result as $tupla)
        {
            array_push($response["usuario"], $tupla);
        }
    }
    else{
        $response["success"] = "204";
        $response["message"] = "Alumnos no encontrados";
        header($_SERVER['SERVER_PROTOCOL'] . " 500  Error interno del servidor ");
    }
    echo json_encode($response);
?>