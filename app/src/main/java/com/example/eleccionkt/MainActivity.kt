package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eleccionkt.BaseDatos.adminDB

class MainActivity : AppCompatActivity() {

    private lateinit var sControl : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actividad = intent
        if(actividad != null && actividad.hasExtra("ncontrol")){
            sControl = actividad.getStringExtra("ncontrol")
        }else {
            val admin = adminDB(this)
            //val sentencia = "Delete from usuario"
            //admin.Ejecuta(sentencia)
            val result = admin.Consulta("Select ncontrol from usuario")
            if(result!!.moveToFirst()){
                sControl = result.getString(0)
                result.close()
                admin.close()
            }else{
                val actividadLog = Intent(this, ActivityLogin::class.java)
                startActivity(actividadLog)
            }
        }
    }
}
