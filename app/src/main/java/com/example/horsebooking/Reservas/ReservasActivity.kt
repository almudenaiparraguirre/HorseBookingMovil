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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReservasActivity : AppCompatActivity(), ClasesAdapter.OnItemClickListener,
    ReservasAdapter.OnItemClickListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var clasesAdapter: ReservasAdapter
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
        clasesAdapter = ReservasAdapter(bookedClassesList, this, this)
        recyclerView.adapter = clasesAdapter
    }

    fun desinscribirseClase(clase: Clase) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email?.replace(".", ",") ?: return
        val claseId = clase.codigo
        val reservaPath = "usuarios/$userEmail/reservas/$claseId"
        Log.d("Reserva path", reservaPath)

        FirebaseDatabase.getInstance().reference.child(reservaPath).removeValue()
            .addOnSuccessListener {
                bookedClassesList.remove(clase)
                //clasesAdapter.notifyItemRemoved(position)
            }
            .addOnFailureListener { exception ->
                Log.e("ReservasActivity", "Error al desinscribir de la clase: ${exception.message}")
                Toast.makeText(this, "Error al desinscribir de la clase", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchBookedClasses() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        Log.d("ReservasActivity", "User ID: $userEmail")
        if (userEmail != null) {
            val formattedEmail = userEmail.replace(".", ",")
            val reservationsRef = FirebaseDatabase.getInstance().getReference("usuarios/$formattedEmail/reservas")
            reservationsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookedClassesList.clear()  // Clear the list to avoid duplicating entries
                    if (snapshot.exists()) {
                        Log.d("ReservasActivity", "Reservations found: ${snapshot.childrenCount}")
                        snapshot.children.forEach { child ->
                            val reservation = child.getValue(Reservation::class.java)
                            if (reservation?.booked == true) {
                                fetchClassDetails(child.key!!)
                            } else {
                                Log.d("ReservasActivity", "Class ${child.key} is not booked")
                            }
                        }
                    } else {
                        Toast.makeText(this@ReservasActivity, "No bookings found.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ReservasActivity, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
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
                    clase.booked = true
                    clase.codigo = snapshot.key ?: ""
                    bookedClassesList.add(clase)
                    runOnUiThread {
                        clasesAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReservasActivity", "Failed to fetch class details: ${error.message}")
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
    override fun onDesinscribirseClicked(clase: Clase) {
        desinscribirseClase(clase)
    }
}