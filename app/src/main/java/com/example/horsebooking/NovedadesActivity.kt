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
        bottomNavigationView = findViewById(R.id.bottom_navigation_novedades)
        bottomNavigationView.selectedItemId = R.id.menu_novedades
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades -> return@OnNavigationItemSelectedListener true

                R.id.menu_reservas -> {
                    if (javaClass != ReservasActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, ReservasActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }

                R.id.menu_precios -> {
                    if (javaClass != PreciosActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, PreciosActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }

                R.id.menu_perfil -> {
                    if (javaClass != PerfilUsuarioActivity::class.java) {
                        startActivity(Intent(this@NovedadesActivity, PerfilUsuarioActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }
}