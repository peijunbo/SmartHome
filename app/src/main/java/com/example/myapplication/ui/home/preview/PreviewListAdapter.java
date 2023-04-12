package com.example.myapplication.ui.home.preview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.dlong.rep.dlroundmenuview.DLRoundMenuView;
import com.example.myapplication.R;
import com.example.myapplication.connection.CommandUtil;
import com.example.myapplication.databinding.SwitchCardBinding;
import com.example.myapplication.ui.HomeViewModel;
import com.example.myapplication.ui.data.ControllerType;
import com.example.myapplication.ui.data.UiState;

import java.util.List;

public class PreviewListAdapter extends RecyclerView.Adapter<PreviewListAdapter.PreviewItemHolder> {
    private static final String TAG = "PreviewListAdapter";
    /**
     * 无过滤规则
     */
    private static final int FILTER_NONE = 0;
    /**
     * 按照房间过滤
     */
    private static final int FILTER_ROOM = 1;
    /**
     * 按照开关类型过滤
     */
    private static final int FILTER_CONTROLLER_TYPE = 2;
    private List<UiState> uiStates;
    private ControllerType contentType;
    private String contentRoom;
    private int filterType;

    public interface OnItemClickListener {
        void onClick(View v, UiState uiState);
    }
    public interface OnSwitcherClickListener {
        void onSwitch(View v, UiState uiState);
    }
    private OnItemClickListener onItemClickListener;
    private OnSwitcherClickListener onSwitcherClickListener;
    private final LifecycleOwner lifecycleOwner;


    public PreviewListAdapter(List<UiState> uiStates, LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        this.uiStates = uiStates;
    }


    @Override
    public int getItemViewType(int position) {
        return uiStates.get(position).getControllerType().ordinal();
    }

    @NonNull
    @Override
    public PreviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SwitchCardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.switch_card, parent, false);
        if (lifecycleOwner != null) {
            binding.setLifecycleOwner(lifecycleOwner);
        }
        return new PreviewItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewItemHolder holder, int position) {
        holder.binding.setUiState(uiStates.get(position));
        switch (uiStates.get(position).getMachineName()) {
            case CommandUtil.MACHINE_AIR_CONDITIONER:
                holder.binding.switchIcon.setImageResource(R.drawable.round_air_24);
                break;
            case CommandUtil.MACHINE_CURTAIN:
                holder.binding.switchIcon.setImageResource(R.drawable.round_curtains_24);
                break;
            case CommandUtil.MACHINE_DOOR:
                holder.binding.switchIcon.setImageResource(R.drawable.round_door_front_24);
                break;
            case CommandUtil.MACHINE_LIGHT:
                holder.binding.switchIcon.setImageResource(R.drawable.round_lightbulb_24);
                break;
            case CommandUtil.MACHINE_TV:
                holder.binding.switchIcon.setImageResource(R.drawable.round_tv_24);
                break;
            case CommandUtil.MACHINE_FAN:
                holder.binding.switchIcon.setImageResource(R.drawable.mode_fan);
                break;
        }
        holder.binding.switcher.setOnClickListener(v -> {
            if (onSwitcherClickListener != null) {
                onSwitcherClickListener.onSwitch(v, uiStates.get(position));
            }
        });
        if (uiStates.get(position).getControllerType() != ControllerType.SWITCH) {
            holder.binding.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v, uiStates.get(position)); // 点击事件处理
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return uiStates.size();
    }

    public static class PreviewItemHolder extends RecyclerView.ViewHolder {
        private final SwitchCardBinding binding;

        public PreviewItemHolder(@NonNull SwitchCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnSwitcherClickListener(OnSwitcherClickListener onSwitcherClickListener) {
        this.onSwitcherClickListener = onSwitcherClickListener;
    }
}
