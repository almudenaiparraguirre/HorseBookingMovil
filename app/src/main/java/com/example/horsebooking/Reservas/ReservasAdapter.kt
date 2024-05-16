package com.example.horsebooking.Clases

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.horsebooking.R
import com.example.horsebooking.Reservas.ReservasActivity
import com.google.firebase.storage.FirebaseStorage

class ReservasAdapter(private val clasesList: List<Clase>, private val context: Context, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder>() {
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reserva_item, parent, false)
        return ReservaViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val clase = clasesList[position]
        Log.d("onBindViewHolder clase", clase.id)
        holder.bindRow(clase)
        val imageRef = storageReference.child("imagenesClases/${clase.id}.png")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .placeholder(R.mipmap.img_logo)
                .into(holder.imagenClase)
        }
    }

    interface OnItemClickListener {
        fun onDesinscribirseClicked(clase: Clase)
    }

    override fun getItemCount(): Int {
        return clasesList.size
    }

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagenClase: ImageView = itemView.findViewById(R.id.imagenClase)
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloCurso)
        val tipoClaseTextView: TextView = itemView.findViewById(R.id.tipoClase)
        val fechaInicioTextView: TextView = itemView.findViewById(R.id.fechaInicio)
        val fechaFinTextView: TextView = itemView.findViewById(R.id.fechaFin)
        val horaClase: TextView = itemView.findViewById(R.id.horaClase)
        val precioTextView: TextView = itemView.findViewById(R.id.info_precio_curso)
        val btnDesinscribirse: TextView = itemView.findViewById(R.id.btnDesinscribirse)

        fun bindRow(clase: Clase) {
            tituloTextView.text = clase.titulo
            tipoClaseTextView.text = "Disciplina: " + clase.tipo
            fechaInicioTextView.text = "Fecha de inicio: " + clase.fecha_inicio
            fechaFinTextView.text = "Fecha de fin: " + clase.fecha_fin
            horaClase.text = "Hora: " + clase.hora + ":" + clase.minuto
            precioTextView.text = clase.precio.toString() + "€"

            btnDesinscribirse.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDesinscribirseClicked(clase)
                    Log.d("Clase", clase.id)
                }
            }
        }
    }
}

