package com.example.horsebooking.Reservas

import android.annotation.SuppressLint
import com.example.horsebooking.Novedades.NovedadesActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.Clases.Clase
import com.example.horsebooking.Clases.ClasesActivity
import com.example.horsebooking.Clases.ClasesAdapter
import com.example.horsebooking.Clases.ReservasAdapter
import com.example.horsebooking.Perfil.PerfilUsuarioActivity
import com.example.horsebooking.R
import com.example.horsebooking.SinCuenta.FirebaseDB
import com.example.horsebooking.SinCuenta.IniciarSesionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReservasActivity : AppCompatActivity(), ClasesAdapter.OnItemClickListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var clasesAdapter: ReservasAdapter
    private lateinit var database: DatabaseReference
    private val bookedClassesList = mutableListOf<Clase>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)
        auth = FirebaseDB.getInstanceFirebase()
        setupRecyclerView()
        fetchBookedClasses()
        setupBottomNavigationView()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewReservas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        clasesAdapter = ReservasAdapter(bookedClassesList, this) // Cambio aquí
        recyclerView.adapter = clasesAdapter
    }

    fun desinscribirseClase(claseId: String, position: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.email
        if (userId != null) {
            val reservaRef = FirebaseDatabase.getInstance().getReference("usuarios/$userId/reservas/$claseId")
            reservaRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    bookedClassesList.removeAt(position)
                    clasesAdapter.notifyItemRemoved(position)
                    Toast.makeText(this, "Desinscripción exitosa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al desinscribirse", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBookedClasses() {
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.email
        Log.d("ReservasActivity", "User ID: $userId")  // Log the user ID to verify it's not null
        if (userId != null) {
            val email = FirebaseDB.getInstanceFirebase().currentUser?.email
            val formattedEmail = email?.replace(".", ",")
            if (email != null) {
                // Declarar reservationsRef fuera del bloque if
                val reservationsRef = FirebaseDatabase.getInstance().getReference("usuarios").child(formattedEmail.toString()).child("reservas")
                reservationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.d("ReservasActivity", "Reservations found: ${snapshot.childrenCount}")  // Log how many reservations are found
                            snapshot.children.forEach { child ->
                                val reservation = child.getValue(Reservation::class.java)
                                Log.d("ReservasActivity", "Reservation data for ${child.key}: $reservation")  // Log the details of each reservation
                                if (reservation?.booked == true) {
                                    fetchClassDetails(child.key!!)
                                } else {
                                    Log.d("ReservasActivity", "Class ${child.key} is not booked")
                                }
                            }
                        } else {
                            Log.d("ReservasActivity", "No reservations found for user: $userId")
                            Toast.makeText(this@ReservasActivity, "No bookings found.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ReservasActivity", "Failed to fetch reservations: ${error.message}")
                        Toast.makeText(this@ReservasActivity, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Maneja el caso en que el correo electrónico sea nulo
                Log.e("ReservasActivity", "Correo electrónico del usuario es nulo.")
            }
        } else {
            Log.d("ReservasActivity", "No user ID found.")
        }
    }

    private fun fetchClassDetails(classId: String) {
        val classReference = FirebaseDatabase.getInstance().getReference("clases").child(classId)
        classReference.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val clase = snapshot.getValue(Clase::class.java)
                if (clase != null) {
                    bookedClassesList.add(clase)
                    runOnUiThread {
                        clasesAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    private fun setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_reservas)
        bottomNavigationView.selectedItemId = R.id.menu_reservas
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_novedades -> {
                startActivity(Intent(this@ReservasActivity, NovedadesActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_reservas -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_precios -> {
                startActivity(Intent(this@ReservasActivity, ClasesActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_perfil -> {
                startActivity(Intent(this@ReservasActivity, PerfilUsuarioActivity::class.java))
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
        val intent = Intent(this@ReservasActivity, IniciarSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onInscribirseClicked(position: Int) {
        TODO("Not yet implemented")
    }
}
