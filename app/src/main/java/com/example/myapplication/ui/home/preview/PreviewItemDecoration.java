package com.example.myapplication.ui.home.preview;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class PreviewItemDecoration extends RecyclerView.ItemDecoration {
    private int margin; // 间隔
    private int verticalMargin;

    public PreviewItemDecoration(Context context) {
        super();
        margin = context.getResources().getDimensionPixelSize(R.dimen.dp_preview_item_margin);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = margin;
        outRect.bottom = margin;
        outRect.left = margin;
        outRect.right = margin;
    }
}
