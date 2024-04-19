package com.example.horsebooking

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import com.example.horsebooking.SinCuenta.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var videoViewSplash: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        videoViewSplash = findViewById(R.id.videoSplashScreen)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash_screen)
        videoViewSplash.setVideoURI(videoUri)
        videoViewSplash.setOnCompletionListener { mp: MediaPlayer? ->
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad del splash screen
        }

        videoViewSplash.start()
    }
}