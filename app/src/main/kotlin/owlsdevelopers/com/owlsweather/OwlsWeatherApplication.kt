package owlsdevelopers.com.owlsweather

import android.app.Application
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

import owlsdevelopers.com.owlsweather.data.DataManager


class OwlsWeatherApplication : Application() {
    private val _dataManager: DataManager by lazy {
        DataManager.load(this)
    }

    override fun onCreate() {
        super.onCreate()
        // configure Flurry
        //FlurryAgent.setLogEnabled(false);
        // init Flurry
        //FlurryAgent.init(this, "8ZJMCNS5VSJWX5WQ83M5");
    }

    val dataManager: DataManager
        @Synchronized
        get() {
            return _dataManager
        }
}
