package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.eleccionkt.BaseDatos.adminDB
import kotlinx.android.synthetic.main.activity_registro.*

class ActivityRegistro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }
    fun insertar(v:View){
        if(etnombre_usuario.text!!.isEmpty() || txtpropuesta.text!!.isEmpty()){
            Toast.makeText(this,"Complete los campos", Toast.LENGTH_SHORT).show()
            txtdescripcion.requestFocus()
        }else{
            val descripcion = txtdescripcion.text.toString()
            val propuesta = txtpropuesta.text.toString()
            val admin = adminDB(this)
            val sentencia = "Insert into candidato(descripcion,propuesta,ncontrol) " +
                    "Values('$descripcion','$propuesta','16980322')"
            if (admin.Ejecuta(sentencia)) {
                Toast.makeText(this,"Acción exitosa", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent) }
            else{
                Toast.makeText(this,"Complete los campos", Toast.LENGTH_SHORT).show()
                txtdescripcion.requestFocus()
            }
        }
    }
}
