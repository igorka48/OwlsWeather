package owlsdevelopers.com.owlsweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.ArrayAdapter



class SettingsActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initSpinners()
    }

    fun initSpinners(){
        val data = arrayOf("one", "two", "three", "four", "five")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data)
        spinnerUnits.adapter = adapter

    }

}
