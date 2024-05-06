package com.example.horsebooking.Clases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.R

class ClasesAdapter(private val clasesList: List<Clase>, private val context: Context) :
    RecyclerView.Adapter<ClasesAdapter.ClaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.clase_item, parent, false)
        return ClaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaseViewHolder, position: Int) {
        val clase = clasesList[position]
        holder.tituloTextView.text = clase.titulo
        holder.tipoClaseTextView.text = "Disciplina: " + clase.tipo
        holder.fechaInicioTextView.text = "Fecha de inicio: " + clase.fecha_inicio
        holder.fechaFinTextView.text = "Fecha de fin: " + clase.fecha_fin
        holder.precioTextView.text = clase.precio + "€"
    }

    override fun getItemCount(): Int {
        return clasesList.size
    }

    inner class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloCurso)
        val tipoClaseTextView: TextView = itemView.findViewById(R.id.tipoClase)
        val fechaInicioTextView: TextView = itemView.findViewById(R.id.fechaInicio)
        val fechaFinTextView: TextView = itemView.findViewById(R.id.fechaFin)
        val precioTextView: TextView = itemView.findViewById(R.id.info_precio_curso)
    }
}