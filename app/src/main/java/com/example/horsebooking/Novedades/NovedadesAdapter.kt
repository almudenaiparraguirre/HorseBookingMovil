package com.example.horsebooking.Novedades

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.R

class NovedadesAdapter(private val novedadesList: List<Novedad>, private val context: Context) :
    RecyclerView.Adapter<NovedadesAdapter.NovedadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovedadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.novedad_item, parent, false)
        return NovedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovedadViewHolder, position: Int) {
        val novedad = novedadesList[position]
        holder.tituloTextView.text = novedad.titulo
        holder.descripcionTextView.text = novedad.descripcion
        //holder.fechaTextView.text = novedad.fecha
    }

    override fun getItemCount(): Int {
        return novedadesList.size
    }

    inner class NovedadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloTextView)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
    }
}
