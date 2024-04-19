package com.example.horsebooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

class IniciarSesionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
    }

    fun iniciarSesion(view: View){

    }

    /*fun signIn(email: String, contrasena: String) {
        firebaseAuth.signInWithEmailAndPassword(email.lowercase(), contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Crear un Intent para ir a EligeModoJuegoActivity
                    val intent = Intent(this, MainActivity::class.java)
                    // Pasar el correo electrónico como extra al Intent
                    intent.putExtra("email", email)
                    // Iniciar la actividad
                    startActivity(intent)
                    // Aquí irías a la segunda actividad
                    Log.d("InicioSesion", "Inicio de sesión exitoso")
                } else {
                    Toast.makeText(baseContext, "Error de email o contraseña", Toast.LENGTH_SHORT).show()
                    Log.e("InicioSesion", "Error de autenticación: ${task.exception?.message}")
                }
            }
    }*/
}