package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Fade;

import com.example.myapplication.MainActivity;
import com.example.myapplication.databinding.HomeFragmentBinding;
import com.example.myapplication.permission.PermissionUtil;
import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.WeatherViewModel;
import com.example.myapplication.ui.home.preview.PreviewAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    public HomeFragmentBinding binding;
    private HomeViewModel homeViewModel;
    private WeatherViewModel weatherViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        setReenterTransition(new Fade());
        setEnterTransition(new Fade());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<String> roomNames = homeViewModel.getRoomNames();
        // 天气图标
        weatherViewModel.getWeatherBean().observe(requireActivity(),
                weatherBean -> {
                    if (weatherBean != null) {
                        try {
                            binding.weatherIcon.setImageResource(
                                    weatherViewModel.getDrawableIdByCode(
                                            requireContext(),
                                            Integer.parseInt(weatherBean.getIcon()))
                            );
                            binding.weatherText.setText(weatherBean.getText() + " " + weatherBean.getTemp() + "\u2103");
                            binding.weatherWind.setText(weatherBean.getWindDir() + " 风力" + weatherBean.getWindScale() + "级");
                        } catch (NullPointerException e) {
                            Log.d(TAG, "nullptr: " + weatherBean);
                        }
                    }
                }
        );
        binding.weatherCard.setOnClickListener(v -> PermissionUtil.checkAndRequestPermission(requireActivity()));

        PreviewAdapter previewAdapter = new PreviewAdapter(this, roomNames);
        binding.controllerPager.setAdapter(previewAdapter); // 设置viewpager adapter

        new TabLayoutMediator(binding.pagerTab, binding.controllerPager,
                (tab, position) -> {
                    tab.setText(roomNames.get(position)); // 对标签设置属性
                }).attach(); // tab与pager进行绑定
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
