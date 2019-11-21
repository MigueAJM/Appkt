package com.example.eleccionkt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_candidato.view.*

class CandidatoAdapter(private var mListacandidato: List<candidato>,
                       private val mContext: Context,private val clickListener: (candidato)-> Unit): RecyclerView.Adapter<CandidatoAdapter.CandidatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return CandidatoViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_candidato,parent,false))
    }

    override fun onBindViewHolder(holder: CandidatoViewHolder, position: Int) {
        holder.bind(mListacandidato[position],mContext,clickListener)
    }

    override fun getItemCount(): Int = mListacandidato.size

    fun setTask(candidato: List<candidato>){
        mListacandidato = candidato
        notifyDataSetChanged()
    }

    fun getTask(): List<candidato> = mListacandidato

    class CandidatoViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind (candid:candidato, context: Context,clickListener: (candidato) -> Unit){
            itemView.tvdescripcion.text = candid.nombre_alumno.toString()
            itemView.tvpropuesta.text = candid.nombre_carrera.toString()
            itemView.setOnClickListener{clickListener(candid)}
        }
    }

}