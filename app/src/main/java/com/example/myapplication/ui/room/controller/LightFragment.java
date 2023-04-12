package com.example.myapplication.ui.room.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.myapplication.R;
import com.example.myapplication.databinding.LightControlBinding;

public class LightFragment extends BaseControlFragment {
    private static final String TAG = "LightFragment";
    private LightControlBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.light_control, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (uiStateID != NO_UI_STATE) {
            binding.setUiState(uiState);
            binding.setFragment(this);
            uiState.getRate().observe(requireActivity(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    binding.seekBar.setProgress(integer);
                }
            });
        }
    }

    public void processRateResult(SeekBar seekBar) {
        if (viewModel != null && uiStateID != NO_UI_STATE) {
            viewModel.processRateResult(uiState);
        }
    }
}
