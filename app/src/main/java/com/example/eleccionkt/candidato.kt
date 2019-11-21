package com.example.eleccionkt

class candidato(id_candidato:Int,descripcion: String, propuesta: String){
    var id_candidato :Int = 0
    var nombre_alumno:String = ""
    var nombre_carrera:String = ""

    init {
        this.id_candidato = id_candidato
        this.nombre_alumno = descripcion
        this.nombre_carrera = propuesta
    }
}