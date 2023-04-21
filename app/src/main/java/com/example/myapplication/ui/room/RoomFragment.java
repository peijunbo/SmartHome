package com.example.myapplication.ui.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RoomFragmentBinding;
import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.data.UiState;
import com.example.myapplication.ui.room.controller.AirConditionerFragment;
import com.example.myapplication.ui.room.controller.LightFragment;
import com.example.myapplication.ui.room.controller.TvFragment;

public class RoomFragment extends Fragment {
    private static final String TAG = "RoomFragment";
    /**
     * 用于Bundle传参的key, 传的参数为界面对应UiState实例的id
     */
    public static final String UI_STATE_ID = "UI_STATE_ID";
    public static final int NO_UI_STATE = -1;
    private int uiStateID = NO_UI_STATE;
    private UiState uiState;
    private HomeViewModel viewModel;
    public RoomFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取viewModel
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        // 设置过渡动画
        setEnterTransition(new Slide());
        Transition transition = new Fade();
        transition.setDuration(2000);
        setSharedElementEnterTransition(transition);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.room_fragment, container, false);
        return binding.getRoot();
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

        if (uiStateID != NO_UI_STATE) {
            // 设置工具栏
            binding.roomToolbar.setTitle(uiState.getRoomName()
                    + " " + uiState.getControllerName().getValue());
            binding.roomToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentFragmentManager().popBackStack();
                }
            });
            // 添加子Fragment，显示具体控制页面
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            Bundle args = new Bundle();
            args.putInt(UI_STATE_ID, uiStateID);
            switch (uiState.getControllerType()) {
                case TV:
                    transaction.add(R.id.controller_container, TvFragment.class, args);
                    break;
                case SWITCH:
                    break;
                case AIR_CONDITIONER:
                    transaction.add(R.id.controller_container, AirConditionerFragment.class, args);
                    break;
                case LIGHT:
                    transaction.add(R.id.controller_container, LightFragment.class, args);
                    break;
            }
            transaction.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
