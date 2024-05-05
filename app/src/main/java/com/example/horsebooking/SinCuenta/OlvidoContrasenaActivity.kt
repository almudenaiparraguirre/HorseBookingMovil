package com.example.horsebooking.SinCuenta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.horsebooking.R
import com.google.firebase.auth.FirebaseAuth

class OlvidoContrasenaActivity : AppCompatActivity() {

    private lateinit var textoAdvertencia: TextView
    private lateinit var boton: Button
    private lateinit var email: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvido_contrasena)
        textoAdvertencia = findViewById(R.id.textoAdvertencia)
        boton = findViewById(R.id.botonIniciarSesion)
        email = findViewById(R.id.editTextEmail)
        auth = FirebaseDB.getInstanceFirebase()

        boton.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (emailText.isEmpty()) {
                textoAdvertencia.visibility = View.VISIBLE
                Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
            } else if (!isValidEmail(emailText)) {
                textoAdvertencia.visibility = View.VISIBLE
                Log.d("RestableceContrasena", "Formato de correo electrónico no válido")
            } else {
                textoAdvertencia.visibility = View.GONE

                // Asegúrate de haber inicializado FirebaseAuth antes de usarlo
                auth.sendPasswordResetEmail(emailText).addOnSuccessListener {
                    Toast.makeText(this, "Por favor revisar el email", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    /** @author Almudena Iparraguirre Castillo
     * Función que regresa a la pantalla de inicio de sesión
     * @param view */
    fun irIniciarSesion(view: View){
        val intent = Intent(this@OlvidoContrasenaActivity, IniciarSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    /** @author Almudena Iparraguirre Castillo
     * Función que implementa el cambio de contraseña en
     * una cuenta
     * @param view */
    fun confirmarCambioContrasena(view: View){
        val intent = Intent(this@OlvidoContrasenaActivity, IniciarSesionActivity::class.java)
        Toast.makeText(this, "Contraseña restablecida con éxito", Toast.LENGTH_SHORT)
        startActivity(intent)
        finish()
    }

    /**
     * Método llamado al hacer clic en el botón para enviar el correo electrónico.
     */
    fun enviarEmail(view: View) {
        val emailText = email.text.toString().trim()

        if (emailText.isEmpty()) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
        } else if (!isValidEmail(emailText)) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "Formato de correo electrónico no válido")
        } else {
            textoAdvertencia.visibility = View.GONE
            val intent = Intent(this, EnvioCodigoActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Método para comprobar que el email introducido es válido.
     * @param email Valida el email
     * @return true si el email es válido, false de lo contrario.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}