package com.example.horsebooking

import IniciarSesionActivity
import android.annotation.SuppressLint
import android.content.Intent
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var inputEmailUsuario: EditText
    private lateinit var inputRegistroApellidos: EditText
    private lateinit var inputNombreUsuario: EditText
    private lateinit var inputRegistroNumeroTelefono: EditText
    private lateinit var mensajeError: TextView

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que se ejecuta al iniciar el MainActivity
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this@MainActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        inputEmailUsuario = findViewById(R.id.inputRegistroEmail)
        inputNombreUsuario = findViewById(R.id.inputRegistroNombre)
        inputRegistroApellidos = findViewById(R.id.inputRegistroApellidos)
        inputRegistroNumeroTelefono = findViewById(R.id.inputRegistroNumeroTelefono)
        mensajeError = findViewById(R.id.mensajeError)
    }

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que valida los campos del registro introducidos por el usuario
     * @param view */
    fun comprobarCampos(view: View){
        //val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        //mediaPlayer.start()
        // 1. Obtener los valores ingresados en los campos de correo y contraseña
        val emailTextView = findViewById<TextView>(R.id.inputRegistroEmail)
        val email = emailTextView.text.toString().lowercase()
        val nombreTextView = findViewById<TextView>(R.id.inputRegistroNombre)
        val nombre = nombreTextView.text.toString()

        // 2. Validar los campos
        if (email.isEmpty() || inputRegistroApellidos.text.isEmpty() || inputRegistroNumeroTelefono.text.isEmpty() || inputNombreUsuario.text.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            mensajeError.text = "Por favor, completa todos los campos"
            return
        }

        // 3. Validar el formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Formato de correo electrónico incorrecto", Toast.LENGTH_SHORT)
                .show()
            mensajeError.text = "Formato de correo electrónico incorrecto"
            return
        }

        // 4. Validar la longitud y composición de la contraseña
       /* if (!validarContraseña(contraseña)) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula y 1 número",
                Toast.LENGTH_SHORT
            ).show()
            mensajeError.text = "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula y 1 número"
            return
        }
*/
        // 5. Llamar a una función para registrar al usuario en Firebase
        val intent = Intent(this@MainActivity, Registrate2Activity::class.java)
        startActivity(intent)
        finish()
    }

    fun volverInicioSesion(view: View){
        val intent = Intent(this@MainActivity, IniciarSesionActivity::class.java)
        startActivity(intent)
        finish()
    }
}