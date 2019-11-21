package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.eleccionkt.BaseDatos.adminDB
import com.example.eleccionkt.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sControl : String
    private lateinit var  viewAdapter: CandidatoAdapter
    private lateinit var viewManager:RecyclerView.LayoutManager
    val candidatoList: List<candidato> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAgregarMa.setOnClickListener{
            startActivity(Intent(this,ActivityRegistro::class.java))
            getCandidatosWs()
        }
        //bloque de codigo que me verifica si ya se han logueado en la app
        val actividad = intent
        if(actividad != null && actividad.hasExtra("ncontrol")){
            sControl = actividad.getStringExtra("ncontrol")
        }else {
            val admin = adminDB(this)
           // val sentencia = "Delete from usuario"
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
        //login

        //Recycler View start
        viewManager = LinearLayoutManager(this)
        viewAdapter = CandidatoAdapter(candidatoList,this,{
                candid:candidato -> onItemClickListener(candid)})
        rv_candidatolist.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL))
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        {override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,swipeDir: Int){
                val position = viewHolder.adapterPosition
                val candid = viewAdapter.getTask()
                val  admin = adminDB(baseContext)
                if(admin.Ejecuta("Delete From candidato Where id_candidato ="
                            +candid[position].id_candidato) == true){
                    retrieveCandidato()
                }
            }
        }).attachToRecyclerView(rv_candidatolist)
    }
    private fun onItemClickListener(candid:candidato){
        Toast.makeText(this,"Clicked item",Toast.LENGTH_SHORT).show()
    }

    override fun onResume(){
        super.onResume()
        retrieveCandidato()
    }

    private fun retrieveCandidato(){
        val  candidatoX = getCandidatos()
        viewAdapter.setTask(candidatoX!!)

    }

    fun  getCandidatos():MutableList<candidato>{
        var candidato:MutableList<candidato> = ArrayList()
        val admin = adminDB(this)
        val tupla = admin.Consulta("Select id_candidato, descripcion, propuesta From candidato Order By id_candidato")
        while (tupla!!.moveToNext()){
            val no = tupla.getInt(0)
            val descrip = tupla.getString(1)
            val propues = tupla.getString(2)
            candidato.add(candidato(no, descrip, propues))
        }
        tupla.close()
        admin.close()
        return candidato
    }
    fun getCandidatosWs() { //funcion que carga la informacion de MySQL a SQLite
        val wsURL = address.IP + "Wservice/getcandidatos.php"
        val admin = adminDB(this)
        admin.Ejecuta("DELETE FROM candidato")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val candidatoJson = response.getJSONArray("candidato")//name usuario (webservice)
                for (i in 0 until candidatoJson.length()) {
                    val idc = candidatoJson.getJSONObject(i).getString("id_candidato")
                    val ncontrol = candidatoJson.getJSONObject(i).getString("ncontrol")
                    val descripcion = candidatoJson.getJSONObject(i).getString("descripcion")
                    val propuesta = candidatoJson.getJSONObject(i).getString("propuesta")
                    val sentencia = "INSERT INTO candidato(id_candidato,ncontrol,descripcion,propuesta) Values('$idc','$ncontrol','$descripcion','$propuesta')"
                    var result = admin.Ejecuta(sentencia)
                  //  Toast.makeText(this, "Información cargada: " + result, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error capa8: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
