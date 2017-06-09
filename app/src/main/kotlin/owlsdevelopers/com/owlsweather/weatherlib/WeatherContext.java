/**
 * ${copyright}.
 */
package owlsdevelopers.com.owlsweather.weatherlib;

import android.content.Context;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;

import owlsdevelopers.com.owlsweather.R;
import owlsdevelopers.com.owlsweather.data.DataManager;
import owlsdevelopers.com.owlsweather.ui.repository.SettingsRepository;
import owlsdevelopers.com.owlsweather.data.repository.SettingsRepositoryImp;
import owlsdevelopers.com.owlsweather.ui.repository.TownsRepository;


public class WeatherContext {
    private static WeatherContext me;
    private WeatherClient client;
    private SettingsRepository settings;

    private WeatherContext() {}

    public static WeatherContext getInstance() {
        if (me == null)
            me = new WeatherContext();

        return me;
    }

    public WeatherClient getClient(Context ctx, TownsRepository repository) {
        if (client != null)
            return client;

        try {
            settings = new SettingsRepositoryImp(ctx, repository);
            WeatherConfig config = new WeatherConfig();
            config.numDays = 10;
            config.unitSystem = ((WeatherLibUnitSystem)settings.getCurrentUnitSystem()).unitSystemValue();
            config.lang = settings.getCurrentLanguage();
            config.ApiKey = ctx.getString(R.string.open_weather_map_api_key);
            client = new WeatherClient.ClientBuilder()
                    .attach(ctx)
                    .config(config)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(WeatherDefaultClient.class)
                    .build();
        }
        catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        return client;
    }

    public void invalidate() {
        client = null;
    }
}
