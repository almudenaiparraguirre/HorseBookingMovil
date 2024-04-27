package com.example.horsebooking.Clases

import com.example.horsebooking.Novedades.NovedadesActivity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.Novedades.Novedad
import com.example.horsebooking.Novedades.NovedadesAdapter
import com.example.horsebooking.Perfil.PerfilUsuarioActivity
import com.example.horsebooking.R
import com.example.horsebooking.Reservas.ReservasActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ClasesActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var clasesAdapter: ClasesAdapter
    private val novedadesList = mutableListOf<Clase>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clases)
        database = FirebaseDatabase.getInstance().reference.child("clases")
        recyclerView = findViewById(R.id.recyclerViewClases)
        recyclerView.layoutManager = LinearLayoutManager(this)

        clasesAdapter =
            ClasesAdapter(novedadesList, this) // Pasar el contexto como segundo parámetro
        recyclerView.adapter = clasesAdapter

        // Leer datos de Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                novedadesList.clear()
                for (novedadSnapshot in snapshot.children) {
                    val novedad = novedadSnapshot.getValue(Clase::class.java)
                    if (novedad != null) {
                        novedadesList.add(novedad)
                    }
                }
                clasesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
        bottomNavigationView = findViewById(R.id.bottom_navigation_precios)
        bottomNavigationView.selectedItemId = R.id.menu_precios
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades ->{
                    startActivity(Intent(this@ClasesActivity, NovedadesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_precios -> {
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_reservas -> {
                    startActivity(Intent(this@ClasesActivity, ReservasActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_perfil -> {
                    startActivity(Intent(this@ClasesActivity, PerfilUsuarioActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}