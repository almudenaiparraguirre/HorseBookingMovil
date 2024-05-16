package com.example.horsebooking.Clases

import com.example.horsebooking.Novedades.NovedadesActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.Novedades.Novedad
import com.example.horsebooking.Novedades.NovedadesAdapter
import com.example.horsebooking.Perfil.PerfilUsuarioActivity
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
import java.util.Date
import java.util.Locale

class ClasesActivity : AppCompatActivity(), ClasesAdapter.OnItemClickListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var clasesAdapter: ClasesAdapter
    private val clasesList = mutableListOf<Clase>()
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clases)
        database = FirebaseDatabase.getInstance().reference.child("clases")
        recyclerView = findViewById(R.id.recyclerViewClases)
        recyclerView.layoutManager = LinearLayoutManager(this)
        auth = FirebaseDB.getInstanceFirebase()

        clasesAdapter =
            ClasesAdapter(clasesList, this, this) // Pasar el contexto como segundo parámetro
        recyclerView.adapter = clasesAdapter

        // Leer datos de Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clasesList.clear()
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                for (claseSnapshot in snapshot.children) {
                    val clase = claseSnapshot.getValue(Clase::class.java)?.apply {
                        id = claseSnapshot.key ?: ""
                        // Asignar el estado de reserva basado en otra consulta a las reservas del usuario
                        booked = snapshot.child("usuarios/$userId/reservas/$id/booked").getValue(Boolean::class.java) ?: false
                    }
                    if (clase != null) {
                        clasesList.add(clase)
                    }
                }
                clasesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ClasesActivity, "Error al cargar las clases: ${error.message}", Toast.LENGTH_LONG).show()
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

    /** @author Almudena Iparraguirre Castillo
     * Función que cierra sesión en la cuenta actual
     * @param view */
    fun cerrarSesion(view: View){
        auth.signOut()
        val intent = Intent(this@ClasesActivity, IniciarSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onInscribirseClicked(position: Int) {
        val clase = clasesList[position]
        if (!clase.booked) {
            val userId = FirebaseAuth.getInstance().currentUser?.email ?: return
            val claseId = clase.id  // Asegúrate de que cada clase tiene un identificador único
            val reservaPath = "usuarios/${userId.replace(".", ",")}/reservas/$claseId"
            val reservaInfo = mapOf("booked" to true, "dateBooked" to getCurrentDate())

            FirebaseDatabase.getInstance().reference.child(reservaPath).setValue(reservaInfo)
                .addOnSuccessListener {
                    Toast.makeText(this, "Inscrito en: ${clase.titulo}", Toast.LENGTH_SHORT).show()
                    clase.booked = true
                    clasesAdapter.notifyItemChanged(position)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al inscribir en la clase", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Ya estás inscrito en esta clase", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}