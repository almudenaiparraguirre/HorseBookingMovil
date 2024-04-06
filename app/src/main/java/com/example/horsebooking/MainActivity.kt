package com.example.horsebooking

import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    /**
     * @author Almudena Iparraguirre Castillo
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this@MainActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    /**
     * @author Almudena Iparraguirre Castillo
     * @param view
     */
    fun registrarse(view: View) {
        val email = "almudena@gmail.com"
        val contrasena = "Abcde123"

        firebaseAuth.createUserWithEmailAndPassword(email, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    val email = user?.email

                    val userId = email?.replace(".", ",")
                    userId?.let {
                        val userData = HashMap<String, Any>()
                        userData["email"] = email
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
}