package owlsdevelopers.com.owlsweather.ui.di.modules

import android.content.Context
import com.survivingwithandroid.weather.lib.WeatherClient
import dagger.Module
import dagger.Provides
import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.data.DataManager
import owlsdevelopers.com.owlsweather.data.repository.SettingsRepositoryImp
import owlsdevelopers.com.owlsweather.ui.repository.SettingsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepository
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepositoryImp
import owlsdevelopers.com.owlsweather.weatherlib.WeatherContext


@Module
class DataModule(val application: OwlsWeatherApplication) {

    @Provides
    internal fun provideSettingsRepository(repository: SettingsRepositoryImp): SettingsRepository {
        return repository
    }


    @Provides
    internal fun provideTownsRepository(repository: TownsRepositoryImp): TownsRepository {
        return TownsRepositoryImp(application.dataManager)
    }

    @Provides
    internal fun provideDataManager():DataManager{
        return application.dataManager
    }

    @Provides
    internal fun provideWeatherClient(repository: TownsRepositoryImp):WeatherClient{
        return WeatherContext.getInstance().getClient(application, repository)
    }

    @Provides
    internal  fun context(): Context {
        return application
    }



}
