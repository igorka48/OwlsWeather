package owlsdevelopers.com.owlsweather.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import owlsdevelopers.com.owlsweather.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, HomeActivity::class.java), null)
        finish()
    }
}
