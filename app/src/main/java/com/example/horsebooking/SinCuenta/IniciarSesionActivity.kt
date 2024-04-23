package com.example.horsebooking.SinCuenta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var textViewNoTienesCuenta: TextView
    private lateinit var editTextEmailUsuario: EditText
    private lateinit var editTextContrasenaUsuario: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        textViewNoTienesCuenta = findViewById(R.id.textViewNoTienesCuenta)
        editTextEmailUsuario = findViewById(R.id.inputRegistroEmail)
        editTextContrasenaUsuario = findViewById(R.id.inputRegistroContrasena)

        val texto = textViewNoTienesCuenta.text.toString()
        val spannableString = SpannableString(texto)
        val indiceInicio = texto.indexOf("¡Registrate")
        val indiceFin = texto.indexOf("servicios!") + "servicios!".length

        val spanClicable = object: ClickableSpan(){
            override fun onClick(widget: View) {
                val intent = Intent(this@IniciarSesionActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(spanClicable, indiceInicio, indiceFin, 0)
        textViewNoTienesCuenta.setTextColor(resources.getColor(R.color.white))
        textViewNoTienesCuenta.text = spannableString
        textViewNoTienesCuenta.setOnClickListener{
            spanClicable.onClick(it)
        }
        textViewNoTienesCuenta.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }

    /** @author Almudena Iparraguirre Castillo
     * Función que verifica las credenciales introducidas por el usuario
     * y si son correctas inicia sesión, en caso de estar incorrectas lanza un
     * mensaje de aviso
     * @param view */
    fun iniciarSesion(view: View) {
        val email = editTextEmailUsuario.text.toString()
        val contrasena = editTextContrasenaUsuario.text.toString()

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

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que inicia sesión del usuario en Firebase
     * @param email
     * @param contrasena */
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
                    finish()
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
