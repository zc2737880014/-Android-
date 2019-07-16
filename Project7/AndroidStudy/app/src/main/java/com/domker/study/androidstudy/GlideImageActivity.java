package com.domker.study.androidstudy;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class GlideImageActivity extends AppCompatActivity {
    ViewPager pager = null;
    LayoutInflater layoutInflater = null;
    List<View> pages = new ArrayList<View>();

    //权限申请（读SD卡）
    private String[] mPermissionsArrays = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
};
    private static final int REQUEST_PERMISSION_CODE = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        layoutInflater = getLayoutInflater();
        pager = (ViewPager) findViewById(R.id.view_pager);
        //申请权限（先做检查）
        if(!checkPermissionAllGranted(mPermissionsArrays)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(mPermissionsArrays,REQUEST_PERMISSION_CODE);
            }
            else{
                Toast.makeText(this,"已取得SD卡读取权限",Toast.LENGTH_SHORT).show();
                addImage(R.drawable.drawableimage);
                addImage(R.drawable.ic_markunread);
                addImage("/sdcard/test.jpg");
                addImage("file:///android_asset/assetsimage.jpg");
                addImage(R.raw.rawimage);
                addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
                ViewAdapter adapter = new ViewAdapter();
                adapter.setDatas(pages);
                pager.setAdapter(adapter);
            }
        }
        else{
            addImage(R.drawable.drawableimage);
            addImage(R.drawable.ic_markunread);
            addImage("/sdcard/test.jpg");
            addImage("file:///android_asset/assetsimage.jpg");
            addImage(R.raw.rawimage);
            addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
            ViewAdapter adapter = new ViewAdapter();
            adapter.setDatas(pages);
            pager.setAdapter(adapter);
        }

//        layoutInflater = getLayoutInflater();
//        pager = (ViewPager) findViewById(R.id.view_pager);
//        addImage(R.drawable.drawableimage);
//        addImage(R.drawable.ic_markunread);
//        addImage("/sdcard/test.jpg");
//        addImage("file:///android_asset/assetsimage.jpg");
//        addImage(R.raw.rawimage);
//        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
//        ViewAdapter adapter = new ViewAdapter();
//        adapter.setDatas(pages);
//        pager.setAdapter(adapter);
    }

    private void addImage(int resId) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.activity_image_item, null);
        Glide.with(this)
                .load(resId)
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }

    private void addImage(String path) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.activity_image_item, null);
        Glide.with(this)
                .load(path)
                .apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }
    //复用PermissionActivity里的测试函数
    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            addImage(R.drawable.drawableimage);
            addImage(R.drawable.ic_markunread);
            addImage("/sdcard/test.jpg");
            addImage("file:///android_asset/assetsimage.jpg");
            addImage(R.raw.rawimage);
            addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
            ViewAdapter adapter = new ViewAdapter();
            adapter.setDatas(pages);
            pager.setAdapter(adapter);
        }else {
            addImage(R.drawable.drawableimage);
            addImage(R.drawable.ic_markunread);
//            addImage("/sdcard/test.jpg");
            addImage("file:///android_asset/assetsimage.jpg");
            addImage(R.raw.rawimage);
            addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
            ViewAdapter adapter = new ViewAdapter();
            adapter.setDatas(pages);
            pager.setAdapter(adapter);
        }
    }
}
