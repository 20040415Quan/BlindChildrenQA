package com.example.androidmvvmtest.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.bean.VideoListBean;
import com.example.androidmvvmtest.ui.activity.MusicActivity;
import com.example.androidmvvmtest.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MusicAllListAdapter extends BaseQuickAdapter<VideoListBean.Data,MusicAllListAdapter.ViewHolder> {

    private List<VideoListBean.Data> data;
    private boolean[] isPlaying;
    private Context context;

    public MusicAllListAdapter(int layoutResId, List<VideoListBean.Data> data) {
        super(layoutResId,data);
        this.data = data;
        isPlaying = new boolean[data.size()];
    }

    public void notifyPlaying(int id,int lastId){
        if (data==null){
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).aid==id+1){
                isPlaying[i] = true;
                notifyItemChanged(i);
            }else if (data.get(i).aid==lastId+1){
                isPlaying[i] = false;
                notifyItemChanged(i);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull MusicAllListAdapter.ViewHolder viewHolder, VideoListBean.Data data) {

        if(data==null){return;}
        //图片
        Glide.with(viewHolder.imgIcon).load(data.url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imgIcon);
        // 标题
        if (data.title != null) {
            viewHolder.tvTitle.setText(data.title);
        } else {
            viewHolder.tvTitle.setText("");
        }

        // 内容
        if (data.overview != null) {
            viewHolder.tvContent.setText(data.overview);
        } else {
            viewHolder.tvContent.setText("");
        }

        // 制作
        if (data.author != null) {
            viewHolder.tvPublisherName.setText(data.author);
        } else {
            viewHolder.tvPublisherName.setText("");
        }

        // 播放量
        if (data.playNums != null) {
            viewHolder.tvPlayNum.setText(data.playNums + " views");
        } else {
            viewHolder.tvPlayNum.setText("0 views");
        }

        // 背景
        if (isPlaying[viewHolder.getAbsoluteAdapterPosition()]) {
            viewHolder.viewContainer.setBackgroundResource(R.drawable.bg_music_playing);
            viewHolder.rightIcon.setImageResource(R.drawable.bt_icon);
        } else {
            viewHolder.viewContainer.setBackgroundResource(R.drawable.button_answer);
            viewHolder.rightIcon.setImageResource(R.drawable.icon_player);
        }


    }

    public class ViewHolder extends BaseViewHolder {

        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvPublisherName;
        private final TextView tvPlayNum;
        private final ImageView imgIcon;
        private final ImageView rightIcon;
        private final ConstraintLayout viewContainer;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.video_title);
             tvContent= view.findViewById(R.id.video_content);
             tvPublisherName = view.findViewById(R.id.publisher_name);
            tvPlayNum = view.findViewById(R.id.play_count);
            imgIcon = view.findViewById(R.id.video_icon);
            viewContainer = view.findViewById(R.id.view_container);
            rightIcon = view.findViewById(R.id.right_icon);

        }
    }
}
