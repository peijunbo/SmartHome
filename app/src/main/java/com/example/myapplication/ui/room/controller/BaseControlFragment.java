package com.example.myapplication.ui.room.controller;

import static com.example.myapplication.ui.room.RoomFragment.NO_UI_STATE;
import static com.example.myapplication.ui.room.RoomFragment.UI_STATE_ID;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.data.UiState;

/**
 * 控制界面的基类，提供了HomeViewModel和UiState的获取
 */
public class BaseControlFragment extends Fragment {
    public static final int NO_UI_STATE = -1;
    protected HomeViewModel viewModel;
    protected int uiStateID;
    protected UiState uiState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取viewModel
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取UiState
        Bundle arguments = getArguments();
        if (arguments != null) {
            uiStateID = arguments.getInt(UI_STATE_ID, NO_UI_STATE);
            uiState = viewModel.findUiState(uiStateID);
            if (uiState == null) { // 没有找到uiState同样视为没有UiState
                uiStateID = NO_UI_STATE;
            }
        }
    }
}
