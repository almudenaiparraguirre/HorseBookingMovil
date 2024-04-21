package com.example.horsebooking.SinCuenta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.horsebooking.PerfilUsuarioActivity
import com.example.horsebooking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IniciarSesionActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
    }

    fun iniciarSesion(view: View) {
        val email = "a@gmail.com"
        val contrasena = "Abcde123"

        val databaseReference = FirebaseDatabase.getInstance().reference.child("usuarios")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(email.replace(".", ","))) {
                    // El usuario existe en la base de datos
                    signIn(email, contrasena)
                } else {
                    // El usuario no existe en la base de datos
                    Toast.makeText(baseContext, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores de base de datos
                Log.e("IniciarSesion", "Error al verificar usuario: ${error.message}")
                Toast.makeText(baseContext, "Error al verificar usuario", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun signIn(email: String, contrasena: String) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Crear un Intent para ir a MainActivity
                    val intent = Intent(this, PerfilUsuarioActivity::class.java)
                    // Pasar el correo electrónico como extra al Intent
                    intent.putExtra("email", email)
                    // Iniciar la actividad
                    startActivity(intent)
                    // Aquí irías a la segunda actividad
                    Log.d("IniciarSesion", "Inicio de sesión exitoso")
                } else {
                    // Error al iniciar sesión
                    Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                    Log.e("IniciarSesion", "Error de autenticación: ${task.exception?.message}")
                }
            }
    }
}
