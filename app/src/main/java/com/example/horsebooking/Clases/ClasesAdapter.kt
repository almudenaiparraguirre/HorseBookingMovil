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
        holder.descripcionTextView.text = clase.descripcion
        holder.precioTextView.text = clase.precio
    }

    override fun getItemCount(): Int {
        return clasesList.size
    }

    inner class ClaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloCurso)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descAcademia)
        val precioTextView: TextView = itemView.findViewById(R.id.info_precio_curso)
    }
}