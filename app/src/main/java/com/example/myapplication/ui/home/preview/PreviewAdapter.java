package com.example.myapplication.ui.home.preview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class PreviewAdapter extends FragmentStateAdapter {
    private static final String TAG = "PreviewAdapter";
    private final List<String> roomNames;

    public PreviewAdapter(@NonNull Fragment fragment, List<String> roomNames) {
        super(fragment);
        this.roomNames = roomNames;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        // 设置按照房间过滤
        args.putInt(PreviewFragment.PREVIEW_FILTER_KEY, PreviewFragment.FILTER_TYPE_ROOM);
        args.putString(PreviewFragment.FILTER_ROOM_KEY, roomNames.get(position));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public int getItemCount() {
        return roomNames.size();
    }
}
