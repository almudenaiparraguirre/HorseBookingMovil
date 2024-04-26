package com.example.horsebooking

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.android.material.bottomnavigation.BottomNavigationView

class PreciosActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precios)
        bottomNavigationView = findViewById(R.id.bottom_navigation_precios)
        bottomNavigationView.selectedItemId = R.id.menu_precios
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_novedades ->{
                    startActivity(Intent(this@PreciosActivity, NovedadesActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_precios -> {
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_reservas -> {
                    startActivity(Intent(this@PreciosActivity, ReservasActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_perfil -> {
                    startActivity(Intent(this@PreciosActivity, PerfilUsuarioActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}