package com.example.timecapsule.utils;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class WrapContentLinearLayoutManager extends GridLayoutManager {
    public WrapContentLinearLayoutManager(Context context, int spanCount) {
        super(context,spanCount);
    }



    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}

