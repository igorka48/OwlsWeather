package owlsdevelopers.com.owlsweather

import android.app.Application
import com.social.com.di.components.ApplicationComponent
import com.social.com.di.components.DaggerApplicationComponent
import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.ui.di.modules.DataModule


class OwlsWeatherApplication : Application() {
    var applicationComponent: ApplicationComponent? = null


    private val _dataManager: DataManager by lazy {
        DataManager.load(this)
    }

    override fun onCreate() {
        super.onCreate()
        this.dataManager
        this.initializeInjector()
    }

    private fun initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .dataModule(DataModule(this))
                .build()
    }



    val dataManager: DataManager
        @Synchronized
        get() {
            return _dataManager
        }
}
