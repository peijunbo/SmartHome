package com.example.myapplication.ui.home.preview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.HomePreviewBinding;
import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.data.ControllerType;
import com.example.myapplication.ui.data.UiState;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.room.RoomFragment;

import java.util.List;

public class PreviewFragment extends Fragment {
    private static final String TAG = "PreviewFragment";
    public static final String PREVIEW_FILTER_KEY = "PREVIEW_FILTER_KEY";
    /**
     * 无过滤
     */
    public static final int FILTER_TYPE_NONE = 0;
    /**
     * 依照房间过滤
     */
    public static final int FILTER_TYPE_ROOM = 1;
    /**
     * 依照控制器类型过滤
     */
    public static final int FILTER_TYPE_CONTROLLER = 2;
    /**
     * 以这个key传入作为过滤标准的房间名，只有当FILTER_TYPE为ROOM时有效
     */
    public static final String FILTER_ROOM_KEY = "FILTER_ROOM_KEY";
    /**
     * 以这个key传入作为过滤标准的ControllerType，只有当FILTER_TYPE为CONTROLLER时有效
     */
    public static final String FILTER_CONTROLLER_KEY = "FILTER_CONTROLLER_KEY";

    public HomePreviewBinding binding;
    private HomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomePreviewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int filterType = FILTER_TYPE_NONE;
        String filterName = "";
        if (arguments != null) {
            filterType = arguments.getInt(PREVIEW_FILTER_KEY, FILTER_TYPE_NONE);
            switch (filterType) {
                case FILTER_TYPE_ROOM:
                case FILTER_TYPE_CONTROLLER:
                    filterName = arguments.getString(FILTER_ROOM_KEY);
                    if (filterName == null) {
                        // 缺少过滤名称则不过滤
                        filterType = FILTER_TYPE_NONE;
                    }
                    break;
                default:
                    filterType = FILTER_TYPE_NONE;
                    break;
            }
        }
        List<UiState> uiStates;
        switch (filterType) {
            case FILTER_TYPE_ROOM:
                uiStates = viewModel.getFilteredUiStates(filterName);
                break;
            case FILTER_TYPE_CONTROLLER:
                uiStates = viewModel.getFilteredUiStates(ControllerType.valueOf(filterName));
                break;
            default:
                uiStates = viewModel.getUiStates();
                break;
        }
        PreviewListAdapter adapter = new PreviewListAdapter(uiStates, requireActivity()); // 将LifecycleOwner传递给所有子binding
        // 设置子项开关点击时间，实现命令发送
        adapter.setOnSwitcherClickListener((v, uiState) -> {
            viewModel.processSwitch(uiState);
        });
        // 设置子项点击事件，实现跳转具体页面
        adapter.setOnItemClickListener((v, uiState) -> {
            // 找到的父碎片应当为HomeFragment
            Fragment parentFragment = getParentFragment();
            if (parentFragment == null) return;
            FragmentManager fragmentManager = parentFragment.getParentFragmentManager();

            HomeFragment homeFragment = (HomeFragment) fragmentManager
                    .findFragmentById(R.id.fragment_container); // 找到当前的HomeFragment实例
            Log.d(TAG, "onViewCreated: " + homeFragment);
            if (homeFragment == null) return;
            // 传入ControllerType参数，
            Bundle args = new Bundle();
            args.putInt(RoomFragment.UI_STATE_ID, uiState.getId());

            // 创建RoomFragment并替换页面
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(homeFragment.binding.appbarLayout, getString(R.string.shared_toolbar))
                    .replace(R.id.fragment_container, RoomFragment.class, args)
                    .addToBackStack(null)
                    .commit();
        });
        binding.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: list");
            }
        });
        binding.recyclerView.setAdapter(adapter); // 设置适配器
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2);
        binding.recyclerView.setLayoutManager(layoutManager); // 设置为表格布局
        binding.recyclerView.addItemDecoration(new PreviewItemDecoration(requireContext())); // 设置子项修饰，提供间隔
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
