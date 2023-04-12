package com.example.myapplication;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.connection.TCP_Init;
import com.example.myapplication.databinding.HomeFragmentBinding;
import com.example.myapplication.databinding.MainFragmentBinding;
import com.example.myapplication.databinding.NavigationLayoutBinding;
import com.example.myapplication.permission.PermissionUtil;
import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.WeatherViewModel;
import com.qweather.sdk.view.HeConfig;

import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int PERMISSION_WEATHER = 0;
    private static Socket client;
    private static TCP_Init tcp_init;
    private static PrintStream out;
    private HomeViewModel homeViewModel;
    private NavigationLayoutBinding binding;
    private WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.navigation_layout);
        binding.setLifecycleOwner(this);

        // 获取viewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        if (PermissionUtil.checkAndRequestPermission(this)) {
            beginWeatherService();
        }
    }

    private void beginWeatherService() {
        HeConfig.init("HE2303241601191653", "2333c3fd650949d3a066a988075c5613");
        HeConfig.switchToDevService(); // 切换至免费订阅
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取定位服务
        weatherViewModel.beginLocating(locationManager);
        // 请求天气
        weatherViewModel.beginObservingWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_WEATHER) {
            //判断定位权限并请求天气
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                beginWeatherService();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!weatherViewModel.isObservingWeather()) {
            weatherViewModel.beginObservingWeather();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (weatherViewModel.isObservingWeather()) {
            weatherViewModel.endObservingWeather();
        }
    }
}