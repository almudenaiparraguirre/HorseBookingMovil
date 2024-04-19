package com.example.horsebooking

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
    private lateinit var inputContrasenaUsuario: EditText
    private lateinit var inputNombreUsuario: EditText
    private lateinit var mensajeError: TextView

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que se ejecuta al iniciar el MainActivity
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this@MainActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        inputEmailUsuario = findViewById(R.id.inputRegistroEmail)
        inputContrasenaUsuario = findViewById(R.id.inputRegistroContrasena)
        inputNombreUsuario = findViewById(R.id.inputRegistroNombre)
        mensajeError = findViewById(R.id.mensajeError)
    }

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que permite al usuario registrarse e implementa sus
     * datos en la base de datos de Firebase
     */
    fun registrarUsuarioEnFirebase() {
        firebaseAuth.createUserWithEmailAndPassword(inputEmailUsuario.text.toString(), inputContrasenaUsuario.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    val email = user?.email

                    val userId = email?.replace(".", ",")
                    userId?.let {
                        val userData = HashMap<String, Any>()
                        userData["email"] = email
                        userData["nombre"] = inputNombreUsuario.text.toString()
                        databaseReference.child("usuarios").child(userId).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Usuario registrado correctamente en Firebase",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirebaseDatabase", "Error al registrar el usuario en Firebase", e)
                                Toast.makeText(
                                    this,
                                    "Error al registrar el usuario en Firebase",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar usuario: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("FirebaseAuth", "Error al registrar usuario", task.exception)
                }
            }
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
        val contraseñaTextView = findViewById<TextView>(R.id.inputRegistroContrasena)
        val email = emailTextView.text.toString().lowercase()
        val contraseña = contraseñaTextView.text.toString()
        val nombreTextView = findViewById<TextView>(R.id.inputRegistroNombre)
        val nombre = nombreTextView.text.toString()

        // 2. Validar los campos
        if (email.isEmpty() || contraseña.isEmpty()) {
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
        if (!validarContraseña(contraseña)) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula y 1 número",
                Toast.LENGTH_SHORT
            ).show()
            mensajeError.text = "La contraseña debe tener al menos 8 caracteres, 1 minúscula, 1 mayúscula y 1 número"
            return
        }

        // 5. Llamar a una función para registrar al usuario en Firebase
        registrarUsuarioEnFirebase()
    }

    /**
     * @author Almudena Iparraguirre Castillo
     * Función que valida la contraseña introducida por el usuario
     * @param contraseña
     * @return Boolean */
    fun validarContraseña(contraseña: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
        return regex.matches(contraseña)
    }
}