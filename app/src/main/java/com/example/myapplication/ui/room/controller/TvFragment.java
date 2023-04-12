package com.example.myapplication.ui.room.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.dlong.rep.dlroundmenuview.DLRoundMenuView;
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener;
import com.example.myapplication.R;
import com.example.myapplication.databinding.TvControlBinding;

public class TvFragment extends BaseControlFragment {
    private TvControlBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tv_control, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (uiStateID != NO_UI_STATE) {
            binding.powerButton.setOnClickListener(v -> viewModel.switchActivated(uiState));
            binding.roundMenuView.setOnMenuClickListener(new OnMenuClickListener() {
                @Override
                public void OnMenuClick(int i) {
                    viewModel.processTVCommand(uiState, i);
                }
            });
        }
    }

}
