package com.fishstar.minitiktok.RV;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishstar.minitiktok.DataType.VideoInfo;
import com.fishstar.minitiktok.R;

import java.util.List;

public class MyAdatper extends RecyclerView.Adapter {
    List<VideoInfo> mList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.activity_play, viewGroup, false);
        return new MyHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((MyHolder) viewHolder).bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(List<VideoInfo> list) {
        mList = list;
    }
}
