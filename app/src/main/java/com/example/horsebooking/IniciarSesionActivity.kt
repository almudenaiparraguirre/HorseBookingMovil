import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.horsebooking.MainActivity
import com.example.horsebooking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener

class IniciarSesionActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
    }

    fun iniciarSesion(view: View) {
        val email = "almudenaiparraguirre@gmail.com" // Obtener el correo electrónico del EditText
        val contrasena = "Abcde123" // Obtener la contraseña del EditText

        // Verificar si el usuario existe
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result.signInMethods!!.size > 0) {
                        // El usuario existe, intenta iniciar sesión
                        signIn(email, contrasena)
                    } else {
                        // El usuario no existe, mostrar un mensaje de error
                        Toast.makeText(baseContext, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Error al verificar si el usuario existe
                    Log.e("IniciarSesion", "Error al verificar usuario: ${task.exception?.message}")
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
                    val intent = Intent(this, MainActivity::class.java)
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
