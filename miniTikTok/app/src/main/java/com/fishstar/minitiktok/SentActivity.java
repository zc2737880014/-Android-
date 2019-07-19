package com.fishstar.minitiktok;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fishstar.minitiktok.DataType.Video;
import com.fishstar.minitiktok.api.IMiniDouyinService;
import com.fishstar.minitiktok.utils.ImageHelper;
import com.fishstar.minitiktok.utils.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private List<Video> mVideos = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    public Button mBtn;
    private TextView mLog;

    public class Feeds {
        List<Video> feeds;
        boolean success;
    }

    public class nothing {

    }

    public class results {
        nothing result;
        String url;
        boolean success;

    }

    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);
        mLog = findViewById(R.id.Log);
        initBtns();
    }

    private void initBtns() {
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mBtn.getText().toString();
                if (getString(R.string.select_an_image).equals(s)) {
                    mLog.setText(mLog.getText() + "\n选择封面...");
                    chooseImage();
                } else if (getString(R.string.select_a_video).equals(s)) {
                    mLog.setText(mLog.getText() + "\n选择视频...");
                    chooseVideo();
                } else if (getString(R.string.post_it).equals(s)) {
                    if (mSelectedVideo != null && mSelectedImage != null) {
                        mLog.setText(mLog.getText() + "\n传送数据...");
                        postVideo();
                    } else {
                        throw new IllegalArgumentException("error data uri, mSelectedVideo = "
                                + mSelectedVideo
                                + ", mSelectedImage = "
                                + mSelectedImage);
                    }
                } else if ((getString(R.string.success_try_refresh).equals(s))) {
                    mLog.setText(mLog.getText() + "\n按钮刷新...");
                    mBtn.setText(R.string.select_an_image);
                }
            }
        });

    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "选择文件 = " + mSelectedImage.getEncodedPath());
                mLog.setText(mLog.getText() + "\n文件目录 =" + mSelectedImage.getEncodedPath());
                mBtn.setText(R.string.select_a_video);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "选择视频= " + mSelectedVideo.getEncodedPath());
                mLog.setText(mLog.getText() + "\n视频目录 =" + mSelectedImage.getEncodedPath());
                mBtn.setText(R.string.post_it);
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(SentActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        mBtn.setText("POSTING...");
        mBtn.setEnabled(false);
        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);

        Call<results> call = getDouyinService().PostVideo("317010XXXX", "fishXXXX", coverImagePart, videoPart);
        call.enqueue(new Callback<results>() {
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mLog.setText(mLog.getText() + "\n上传成功！");
                    mBtn.setText(R.string.success_try_refresh);
                    mBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<results> call, Throwable t) {
                Toast.makeText(SentActivity.this, "retrofit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mLog.setText(mLog.getText() + "\n上传失败！");
            }
        });

        Toast.makeText(this, "正在上传", Toast.LENGTH_SHORT).show();
    }


    private IMiniDouyinService getDouyinService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMiniDouyinService.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (miniDouyinService == null) {
            miniDouyinService = retrofit.create(IMiniDouyinService.class);
        }
        return miniDouyinService;
    }

}
