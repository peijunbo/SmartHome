package com.example.myapplication.ui.room.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.databinding.AirConditionerControlBinding;

public class AirConditionerFragment extends BaseControlFragment {
    private static final String TAG = "AirConditionerFragment";
    private AirConditionerControlBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.air_conditioner_control, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (uiStateID != NO_UI_STATE) {
            binding.setUiState(uiState);
            binding.turnUpButton.setOnClickListener(v -> viewModel.turnUpTemperature(uiStateID));
            binding.turnDownButton.setOnClickListener(v -> viewModel.turnDownTemperature(uiStateID));
            binding.power.setOnClickListener(v -> viewModel.switchActivated(uiState));
            binding.modeButton.setOnClickListener(v -> viewModel.changeFAAMode(uiState));
            binding.windSpeedButton.setOnClickListener(v -> viewModel.changeWindSpeed(uiState));
        }
    }


}
