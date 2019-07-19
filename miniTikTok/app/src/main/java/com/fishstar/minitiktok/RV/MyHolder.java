package com.fishstar.minitiktok.RV;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fishstar.minitiktok.DataType.VideoInfo;
import com.fishstar.minitiktok.Glide.GlideCircleBorderTransform;
import com.fishstar.minitiktok.MainActivity;
import com.fishstar.minitiktok.Player.VideoPlayerIJK;
import com.fishstar.minitiktok.Player.VideoPlayerListener;
import com.fishstar.minitiktok.R;
import com.fishstar.minitiktok.VideoActivity;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyHolder extends RecyclerView.ViewHolder {
    VideoPlayerIJK ijkPlayerView;
    ImageView avatar;
    ImageView preview;
    TextView likeCount,CommentCount,ShareCount;
    TextView authorName;
    TextView intro;

    int mVideoWidth = 0;
    int mVideoHeight = 0;
    RelativeLayout layout;
    ImageView stopButton;
    boolean isPlaying = true;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.homePageAvatar);
        preview = itemView.findViewById(R.id.Preview);
        likeCount = itemView.findViewById(R.id.likeCount);
        CommentCount = itemView.findViewById(R.id.CommentCount);
        ShareCount = itemView.findViewById(R.id.ShareCount);
        authorName = itemView.findViewById(R.id.author_name);
        intro = itemView.findViewById(R.id.intro);


        layout = itemView.findViewById(R.id.play_layout);
        stopButton = itemView.findViewById(R.id.buttion_stop);
        stopButton.setVisibility(View.INVISIBLE);

    }
    public void bind(VideoInfo data) {
        load(avatar,"https://q1.qlogo.cn/g?b=qq&nk=1647075274&s=100");
        load(preview,data.getImageUrl());
        likeCount.setText(Math.round(((Math.random()*10000)+100000)/10000f*100)/100+"w");
        CommentCount.setText(Math.round(((Math.random()*10000)+100000)/10000f*100)/100+"w");
        ShareCount.setText(Math.round(((Math.random()*10000)+100000)/10000f*100)/100+"w");
        authorName.setText(data.getUserName());
        intro.setText("一段介绍");
        preview.setOnClickListener(v->{

        });



    }
    private void load(ImageView view ,String path){
        Glide.with(this.itemView.getContext())
                .load(path)
                .apply(new RequestOptions().centerCrop().bitmapTransform(new GlideCircleBorderTransform(20,0xff16181A)).diskCacheStrategy(DiskCacheStrategy.NONE))
                .error(R.drawable.ic_launcher_foreground)
                .into(view);
    }

}
