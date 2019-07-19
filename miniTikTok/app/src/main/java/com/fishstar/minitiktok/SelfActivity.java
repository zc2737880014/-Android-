package com.fishstar.minitiktok;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fishstar.minitiktok.Glide.GlideCircleBorderTransform;

public class SelfActivity extends AppCompatActivity {
    private ImageView image;
    private TextView name;
    private TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        name = findViewById(R.id.SelfName);
        image= findViewById(R.id.avatar);
        id = findViewById(R.id.UserIds);
        name.setText(getIntent().getStringExtra("name"));
        id.setText(getIntent().getStringExtra("id"));

        load(getIntent().getStringExtra("url"));

    }
    public static void launch(Activity activity, String name,String id,String url) {
        Intent intent = new Intent(activity, SelfActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }
    private void load(int resId){
        Glide.with(this)
                .load(resId)
                .error(R.drawable.ic_launcher_background)
                .into(image);
    }
    private void load(String path){
        Glide.with(this)
                .load(path)
                .apply(new RequestOptions().centerCrop().bitmapTransform(new GlideCircleBorderTransform(20,0xff16181A)).diskCacheStrategy(DiskCacheStrategy.NONE))
                .error(R.drawable.ic_launcher_foreground)
                .into(image);
    }

}
