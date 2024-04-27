package com.example.horsebooking.Perfil

import com.example.horsebooking.Novedades.NovedadesActivity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.horsebooking.Clases.ClasesActivity
import com.example.horsebooking.R
import com.example.horsebooking.Reservas.ReservasActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilUsuarioActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)
        bottomNavigationView = findViewById(R.id.bottom_navigation_perfil)
        bottomNavigationView.selectedItemId = R.id.menu_perfil
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
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
}