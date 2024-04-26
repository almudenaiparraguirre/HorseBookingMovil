package com.example.horsebooking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class NovedadesActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novedades)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades ->
                    return@OnNavigationItemSelectedListener true

                R.id.menu_reservas -> {
                    startActivity(Intent(this@NovedadesActivity, ReservasActivity::class.java))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_precios -> {
                    startActivity(Intent(this@NovedadesActivity, PreciosActivity::class.java))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_perfil -> {
                    startActivity(Intent(this@NovedadesActivity, PerfilUsuarioActivity::class.java))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}
