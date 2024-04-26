package com.example.horsebooking

import com.example.horsebooking.SinCuenta.NovedadesActivity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReservasActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)
        bottomNavigationView = findViewById(R.id.bottom_navigation_reservas)
        bottomNavigationView.selectedItemId = R.id.menu_reservas
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades -> {
                    startActivity(Intent(this@ReservasActivity, NovedadesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_reservas -> {
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_precios -> {
                    startActivity(Intent(this@ReservasActivity, PreciosActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_perfil -> {
                    startActivity(Intent(this@ReservasActivity, PerfilUsuarioActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}