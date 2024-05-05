package com.example.horsebooking.Novedades

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.Perfil.PerfilUsuarioActivity
import com.example.horsebooking.Clases.ClasesActivity
import com.example.horsebooking.R
import com.example.horsebooking.Reservas.ReservasActivity
import com.example.horsebooking.SinCuenta.FirebaseDB
import com.example.horsebooking.SinCuenta.IniciarSesionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NovedadesActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var novedadesAdapter: NovedadesAdapter
    private val novedadesList = mutableListOf<Novedad>()
    val auth: FirebaseAuth = FirebaseDB.getInstanceFirebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novedades)
        bottomNavigationView = findViewById(R.id.bottom_navigation_novedades)
        bottomNavigationView.selectedItemId = R.id.menu_novedades
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        database = FirebaseDatabase.getInstance().reference.child("novedades")

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNovedades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        novedadesAdapter =
            NovedadesAdapter(novedadesList, this) // Pasar el contexto como segundo parámetro
        recyclerView.adapter = novedadesAdapter

        // Leer datos de Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                novedadesList.clear()
                for (novedadSnapshot in snapshot.children) {
                    val novedad = novedadSnapshot.getValue(Novedad::class.java)
                    if (novedad != null) {
                        novedadesList.add(novedad)
                    }
                }
                novedadesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades -> return@OnNavigationItemSelectedListener true
                R.id.menu_reservas -> {
                    if (javaClass != ReservasActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, ReservasActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                R.id.menu_precios -> {
                    if (javaClass != ClasesActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, ClasesActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                R.id.menu_perfil -> {
                    if (javaClass != PerfilUsuarioActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, PerfilUsuarioActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }

    /** @author Almudena Iparraguirre Castillo
     * Función que cierra sesión en la cuenta actual
     * @param view */
    fun cerrarSesion(view: View){
        auth.signOut()
        val intent = Intent(this@NovedadesActivity, IniciarSesionActivity::class.java)
        startActivity(intent)
        finish()
    }
}