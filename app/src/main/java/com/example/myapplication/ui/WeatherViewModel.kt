package com.example.myapplication.ui

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.SmartHomeApp
import com.example.myapplication.permission.PermissionUtil
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import com.qweather.sdk.view.QWeather.OnResultWeatherNowListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

class WeatherViewModel : ViewModel() {
    companion object {
        private const val TAG = "WebViewModel"
        const val NO_WEATHER_INFO = -1;
    }
    var isObservingWeather = true
    private var haveWeatherInfo = false
    val weatherBean = MutableLiveData<WeatherNowBean.NowBaseBean>(null)
    var currentLocation: Location? = null

    fun requestWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation?.let { location ->
                QWeather.getWeatherNow(
                    SmartHomeApp.context,
                    String.format("%.2f,%.2f", location.longitude, location.latitude),
                    Lang.ZH_HANS,
                    Unit.METRIC,
                    object : OnResultWeatherNowListener {
                        override fun onError(p0: Throwable?) {
                            Log.d(TAG, "onError: $p0")
                        }

                        override fun onSuccess(bean: WeatherNowBean?) {
                            val nowBean = bean?.now
                            viewModelScope.launch(
                                Dispatchers.Main
                            ) {
                                nowBean?.run {
                                    weatherBean.value = this
                                    haveWeatherInfo = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    fun beginObservingWeather() {
        isObservingWeather = true
        viewModelScope.launch(Dispatchers.IO) {
            while (isObservingWeather) {
                requestWeather()
                if (!haveWeatherInfo) {
                    delay(Duration.parse("2s"))
                } else {
                    delay(Duration.parse("5m"))
                }
            }
        }
    }

    fun endObservingWeather() {
        isObservingWeather = false
    }

    fun beginLocating(locationManager: LocationManager) {
        if (PermissionUtil.checkPermission(SmartHomeApp.context)) {
            var provider = locationManager.getBestProvider(
                Criteria(),
                true
            )
            if (provider == null) {
                provider = LocationManager.GPS_PROVIDER
            }
            currentLocation = locationManager.getLastKnownLocation(provider)
            locationManager.requestLocationUpdates(
                provider,
                1000L, 10000f
            ) { currentLocation = it }
        }
    }

    fun getDrawableIdByCode(context: Context, code: Int) =
        getDrawableIdByName(context, "qweather_${code}_fill")

    fun getDrawableIdByName(context: Context, name: String) =
        context.resources.getIdentifier(name, "drawable", context.packageName)
}