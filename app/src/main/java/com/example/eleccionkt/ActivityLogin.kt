package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.eleccionkt.BaseDatos.adminDB
import com.example.eleccionkt.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getUsuarios()
        etncontrol.requestFocus()
    }
    fun login(v:View){
        if(etncontrol.text!!.isEmpty() || etnip.text!!.isEmpty())
            etncontrol.requestFocus()
        else{
            val control = etncontrol.text.toString(); val nip = etnip.text.toString()
            val admin = adminDB(this)
            val result =  admin.Consulta("Select ncontrol,nip From usuario Where ncontrol = '$control'")
            if(result!!.moveToFirst()){
                var scontrol = result.getString(0)
                var snip = result.getString(1)
                if (control == scontrol && nip == snip){
                    val actividad = Intent(this,MainActivity::class.java)
                    actividad.putExtra("ncontrol",control)
                    startActivity(actividad)
                }else{
                    Toast.makeText(this,"Su numero de control o nip son incoreectos",Toast.LENGTH_SHORT).show()
                    etncontrol.requestFocus()
                }
           }else Toast.makeText(this,"" +error("capa8"),Toast.LENGTH_SHORT).show()
        }
    }
    fun getUsuarios() { //funcion que carga la informacion de MySQL a SQLite
        val wsURL = address.IP + "WService/MostrarAlumnos.php"
        val admin = adminDB(this)
        admin.Ejecuta("DELETE FROM usuario")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null, Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val usuariosJson = response.getJSONArray("usuario")//name usuario (webservice)
                for (i in 0 until usuariosJson.length()) {
                    val ncontrol = usuariosJson.getJSONObject(i).getString("ncontrol")
                    val nombre = usuariosJson.getJSONObject(i).getString("nombre_alumno")
                    val sexo = usuariosJson.getJSONObject(i).getString("sexo")
                    val id_carrera = usuariosJson.getJSONObject(i).getString("id_carrera")
                    val nip = usuariosJson.getJSONObject(i).getString("nip")
                    val sentencia = "INSERT INTO usuario(ncontrol,nombre_alumno,sexo,id_carrera,nip) Values('$ncontrol','$nombre','$sexo',$id_carrera,'$nip')"
                    var result = admin.Ejecuta(sentencia)
                    Toast.makeText(this, "Información cargada: " + result, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error capa8: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
