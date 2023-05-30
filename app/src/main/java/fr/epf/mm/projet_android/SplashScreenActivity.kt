package fr.epf.mm.projet_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    private var SPLASH_TIME : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.setTitle("")

        Handler().postDelayed({
            startActivity(Intent (this, MenuAccueilActivity::class.java))
            finish()
        }, SPLASH_TIME)


    }
}