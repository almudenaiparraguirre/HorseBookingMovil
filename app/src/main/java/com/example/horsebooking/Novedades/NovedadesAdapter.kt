package com.example.horsebooking.Novedades

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
import com.example.horsebooking.SinCuenta.FirebaseDB
import com.google.firebase.storage.FirebaseStorage

class NovedadesAdapter(private val novedadesList: List<Novedad>, private val context: Context) :
    RecyclerView.Adapter<NovedadesAdapter.NovedadViewHolder>(){
    var items: List<Novedad> = listOf()
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovedadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.novedad_item, parent, false)
        return NovedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovedadViewHolder, position: Int) {
        val novedad = novedadesList[position]
        holder.tituloTextView.text = novedad.titulo
        holder.descripcionTextView.text = novedad.descripcion
        val imageRef = storageReference.child("imagenesNovedades/${novedad.id}.png")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .placeholder(R.mipmap.img_logo)
                .into(holder.imagenNovedad)
        }
    }


    override fun getItemCount(): Int {
        return novedadesList.size
    }

    inner class NovedadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloTextView)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
        var imagenNovedad: ImageView = itemView.findViewById(R.id.imagenNovedad)
    }
}
