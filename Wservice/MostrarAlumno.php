<?php 
    $Cn = mysqli_connect("localhost","root","","eleccion")or die("server no encontrado");
    mysqli_set_charset($Cn,"utf8");
        $method=$_SERVER['REQUEST_METHOD'];
    $obj = json_decode( file_get_contents('php://input') );   
    $objArr = (array)$obj;
	if (empty($objArr))
    {
		$response["success"] = 422;  //No encontro información 
        $response["message"] = "Error: checar json entrada";
        header($_SERVER['SERVER_PROTOCOL']." 422  Error: faltan parametros de entrada json ");		
    }
    else
    {
	    $ncontrol =  $objArr['ncontrol'];
	    $response = array();
        $result = mysqli_query($Cn,"SELECT * from usuario where ncontrol = '$ncontrol'");
        if (!empty($result)) {
            $response["success"] = "200";
            $response["message"] = "Usuario encontrado";

            $response["usuario"] = array();
            foreach ($result as $tupla)
            {
                array_push($response["usuario"], $tupla);
            }
        }
        else{
            $response["success"] = "204";
            $response["message"] = "No exite el alumno";
            header($_SERVER['SERVER_PROTOCOL'] . " 500  Error interno del servidor ");
        }
    }
    echo json_encode($response);
?>