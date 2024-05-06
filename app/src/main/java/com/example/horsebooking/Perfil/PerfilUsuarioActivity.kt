package com.example.horsebooking.Perfil

import android.Manifest
import com.example.horsebooking.Novedades.NovedadesActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.horsebooking.Clases.ClasesActivity
import com.example.horsebooking.R
import com.example.horsebooking.Reservas.ReservasActivity
import com.example.horsebooking.SinCuenta.FirebaseDB
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class PerfilUsuarioActivity : AppCompatActivity() {

    private val REQUEST_CAMERA = 123
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imagen: ImageView
    private lateinit var lapiz: ImageView
    private lateinit var miStorage: StorageReference
    private lateinit var cardViewPerfil: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)
        bottomNavigationView = findViewById(R.id.bottom_navigation_perfil)
        bottomNavigationView.selectedItemId = R.id.menu_perfil
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        lapiz = findViewById(R.id.lapiz_editar)
        imagen = findViewById(R.id.roundedImageView)
        cardViewPerfil = findViewById(R.id.cardview_perfil)

        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val storageRef: StorageReference = FirebaseDB.getInstanceStorage().reference.child("imagenesPerfilGente").child("$userId.jpg")
        val imagesRef = storageRef.child("imagenesPerfilGente/$userId.jpg")
        println("Image URL: $imagesRef")
        Glide.with(this)
            .load(imagesRef)
            .into(imagen)

        lifecycleScope.launch {
            downloadImage2()
        }

        lapiz.setOnClickListener {
            mostrarDialogoElegirOrigen()
        }
        mostrarImagenGrande()
    }

    private fun mostrarImagenSeleccionada(uri: Uri?) {
        uri?.let {
            Glide.with(this)
                .load(it)
                .into(imagen)
        }
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades -> {
                    startActivity(Intent(this@PerfilUsuarioActivity, NovedadesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_reservas -> {
                    startActivity(Intent(this@PerfilUsuarioActivity, ReservasActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_precios -> {
                    startActivity(Intent(this@PerfilUsuarioActivity, ClasesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_perfil -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    /**
     * Cambia la imagen de perfil del usuario y la carga en Firebase Storage.
     *
     * @param oldImageUrl URI de la imagen anterior.
     */
    private suspend fun changeAndUploadImage(oldImageUrl: Uri) {
        withContext(Dispatchers.IO) {
            val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid

            // Descarga la imagen en un Bitmap
            val bitmap = Glide.with(this@PerfilUsuarioActivity)
                .asBitmap()
                .load(oldImageUrl)
                .submit()
                .get()

            // Comprimir el bitmap
            val compressedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            val baos = ByteArrayOutputStream()
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val data = baos.toByteArray()

            // Subir la nueva imagen comprimida a Firebase Storage con el nombre del usuario
            val newImageRef = FirebaseDB.getInstanceStorage().reference.child("imagenesPerfilGente").child("$userId.jpg")
            try {
                Tasks.await(newImageRef.putBytes(data))

                // Ahora puedes usar newImageRef para mostrar la imagen en tu aplicación si es necesario
            } catch (exception: Exception) {
                println("Error al cargar la nueva imagen: ${exception.message}")
            }
        }
    }


    /**
     * Descarga la imagen de perfil del usuario desde Firebase Storage y la muestra en la interfaz
     * de usuario. Luego, llama a la función para cambiar el nombre y cargar la imagen.
     */
    private fun downloadImage2() {
        val storageRef = FirebaseDB.getInstanceStorage().reference
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

        imagesRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(this)
                .load(url)
                .into(imagen)

            // Call the function to change the name and upload the image
            runBlocking {
                changeAndUploadImage(url)
            }
        }.addOnFailureListener { exception ->
            println("Error al cargar la imagen: ${exception.message}")

            imagen.setImageResource(R.mipmap.img_logo)
        }
    }

    private fun mostrarImagenGrande() {
        cardViewPerfil.setOnClickListener {
            mostrarDialogImagen(imagen)
        }

        lapiz.setOnClickListener {
            mostrarDialogoElegirOrigen()
        }
    }

    /*/**
     * Inicializa los datos del usuario en la interfaz, mostrando información como la fecha de
     * registro, nombre, correo, experiencia, nivel y precisión.
     */
    @SuppressLint("SetTextI18n")
    private fun inicializarConBase() = runBlocking{
        fechaRegistro.text = "Se unió en " + UtilsDB.obtenerFechaActualEnTexto()
        nombreUsuarioTextView.text = UtilsDB.getNombre()
        gmailUsuarioTextView.text = UtilsDB.getCorreo()
        experienciaTextView.text = UtilsDB.getExperiencia().toString()
        nivelTextView.text= UtilsDB.getNivelMaximo()?.minus(1).toString()
        precisionTextView.text =UtilsDB.getMediaPrecisiones().toString()+"%"
    }
*/

    /**
     * Muestra un cuadro de diálogo que permite al usuario elegir entre tomar una foto con la cámara
     * o seleccionar una imagen de la galería.
     */
    private fun mostrarDialogoElegirOrigen() {
        val opciones = arrayOf("Tomar foto", "Elegir de la galería")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar origen de la imagen")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        // Opción "Tomar foto" seleccionada
                        requestCameraPermission()
                    }

                    1 -> {
                        // Opción "Elegir de la galería" seleccionada
                        abrirGaleria()
                    }
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Muestra la imagen de perfil en grande en un cuadro de diálogo al hacer clic en la imagen.
     *
     * @param imageView ImageView que se mostrará en el cuadro de diálogo.
     */
    private fun mostrarDialogImagen(imageView: ImageView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_imagen)

        val imagenAmpliada = dialog.findViewById<ImageView>(R.id.imagenAmpliada)
        imagenAmpliada.setImageDrawable(imageView.drawable)

        // Animación de escala
        val escala = ScaleAnimation(
            0.2f,  // Escala de inicio
            1.0f,  // Escala de fin
            0.2f,  // Punto focal de inicio (X)
            0.2f,  // Punto focal  de inicio (Y)
            Animation.RELATIVE_TO_SELF, 0.5f,  // Punto focal de fin (X)
            Animation.RELATIVE_TO_SELF, 0.5f   // Punto focal de fin (Y)
        )

        escala.duration = 200  // Duración de la animación en milisegundos
        imagenAmpliada.startAnimation(escala)

        // Cierra el dialog al hacer clic en la imagen
        imagenAmpliada.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Abre la galería de imágenes para seleccionar una.
     */
    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    /**
     * Callback que se ejecuta después de seleccionar una imagen desde la galería.
     * Actualiza la URI de la imagen seleccionada, muestra la imagen en un ImageView,
     * y realiza acciones como descargar la imagen y guardarla en Firebase.
     *
     * @see [startForActivityGallery]
     */
    private var selectedImageUri: Uri? = null
    val startForActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                selectedImageUri = data
                imagen.setImageURI(data)

                // Descargar la imagen desde la URI y guardarla en un archivo
                //descargarYGuardarImagen(selectedImageUri)
                guardarImagenEnFirebase(selectedImageUri)
            }

            val preferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("profileImageUri", selectedImageUri.toString())
            editor.apply()
        }

    /**
     * Guarda la imagen seleccionada de la galería en Firebase Storage.
     *
     * @param imageUri URI de la imagen seleccionada.
     */
    private fun guardarImagenEnFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
            if (userId != null) {
                val storageRef = FirebaseDB.getInstanceStorage().reference
                val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

                // Subir la imagen a Firebase Storage con el nuevo nombre
                imagesRef.putFile(imageUri)
                    .addOnSuccessListener {
                        println("Éxito al subir la imagen con el nombre $userId")
                    }
                    .addOnFailureListener { exception ->
                        println("Error al subir la imagen: ${exception.message}")
                    }
            } else {
                println("El ID del usuario es nulo.")
            }
        }
    }

    /**
     * Función que maneja el resultado de la solicitud de permisos para la cámara.
     *
     * @param isGranted Indica si el permiso fue concedido o no.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, abrir la cámara
                abrirCamara()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Solicita permiso para acceder a la cámara y la abre si el permiso ya está concedido.
     */
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, abrir la cámara
                abrirCamara()
            }
            else -> {
                // Solicitar permiso para la cámara
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /**
     * Sube la imagen a Firebase Storage.
     *
     * @param bitmap Imagen en formato Bitmap que se va a subir.
     */
    private fun guardarImagen(bitmap: Bitmap?) {
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid

        if (userId != null) {
            val storageRef = FirebaseDB.getInstanceStorage().reference
            val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

            // Guardar la imagen en Firebase Storage
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            imagesRef.putBytes(data)
                .addOnSuccessListener {
                    println("Éxito al subir la imagen con el nombre $userId.jpg")
                }
                .addOnFailureListener { exception ->
                    println("Error al subir la imagen: ${exception.message}")
                }
        } else {
            println("El ID del usuario es nulo.")
        }
    }

    /**
     * Maneja el resultado de la actividad de la cámara para obtener una foto.
     *
     * @param requestCode Código de solicitud.
     * @param resultCode Código de resultado.
     * @param data Datos de la actividad.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imagen.setImageBitmap(imageBitmap)
            guardarImagen(imageBitmap)
            val uri = data?.data

            val filePath = uri?.lastPathSegment?.let { miStorage.child("hit").child(it) }
            if (uri != null) {
                filePath?.putFile(uri)?.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this, "Éxito al subir el archivo", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al subir el archivo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Abre la cámara para capturar una foto utilizando la intención [MediaStore.ACTION_IMAGE_CAPTURE].
     */
    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }
}