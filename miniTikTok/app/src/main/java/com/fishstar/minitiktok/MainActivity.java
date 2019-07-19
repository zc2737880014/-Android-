package com.fishstar.minitiktok;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fishstar.minitiktok.DataType.Video;

import com.fishstar.minitiktok.Glide.GlideCircleBorderTransform;
import com.fishstar.minitiktok.RV.MyAdatper;
import com.fishstar.minitiktok.api.IMiniDouyinService;
import com.fishstar.minitiktok.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fishstar.minitiktok.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.fishstar.minitiktok.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.fishstar.minitiktok.utils.Utils.getOutputMediaFile;

public class MainActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView mListView;
    //List
    List<Video> mList = new ArrayList<>();
    //buttons
    TextView tv_shouye;
    TextView tv_paishe;
    TextView tv_shangchuan;
    ImageView refresh;
    //popupwindow
    private PopupWindow mPopWindow;
    //camera
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;
    private Button buttonRecord;
    private Button buttonLight;
    private Button buttonDelay;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean isRecording = false;
    private int rotationDegree = 0;
    private static boolean isStop = false;
    private static boolean isLightOn = false;
    //permissions
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    //api tools
    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService;



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView preview;
        ImageView like;
        TextView likeCount, CommentCount, ShareCount;
        TextView authorName;
        TextView intro;

        RelativeLayout layout;
        ImageView stopButton;
        boolean likes=false;

        public MyViewHolder(@NonNull View itemView) {
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
            like= itemView.findViewById(R.id.like);
        }

        public void bind(final Activity activity, final Video data) {
            load(avatar, data.getImageUrl());
            load(preview, data.getImageUrl());
            likeCount.setText(Math.round(((Math.random() * 10000) + 100000) / 10000f * 100) / 100 + "w");
            CommentCount.setText(Math.round(((Math.random() * 10000) + 100000) / 10000f * 100) / 100 + "w");
            ShareCount.setText(Math.round(((Math.random() * 10000) + 100000) / 10000f * 100) / 100 + "w");
            authorName.setText("@"+data.getUserName());
            intro.setText("作者id" + data.getStudentId());
            avatar.setOnClickListener(v -> {
                SelfActivity.launch(activity, data.getUserName(), data.getStudentId(),data.getImageUrl());
            });
            preview.setOnClickListener(v -> {
                VideoActivity.launch(activity, data.getVideoUrl());
            });
            like.setOnClickListener(v->{
                likes=!likes;
                if(likes){
                    like.setColorFilter(Color.RED);
                }
                else{
                    like.setColorFilter(Color.WHITE);
                }
            });

        }

        private void load(ImageView view, String path) {
            Glide.with(this.itemView.getContext())
                    .load(path)
                    .apply(new RequestOptions().centerCrop().bitmapTransform(new GlideCircleBorderTransform(20, 0xff16181A)).diskCacheStrategy(DiskCacheStrategy.NONE))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(view);
        }
    }

    private void initRecyclerView() {
        mListView = findViewById(R.id.rv);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
                return new MyViewHolder(
                        LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.activity_play, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mList.get(i);
                viewHolder.bind(MainActivity.this, video);
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        });
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(mListView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fetchFeed(mListView);
        initRecyclerView();
        refresh = findViewById(R.id.refresh);
        tv_shouye = findViewById(R.id.tv_shouye);
        tv_paishe = findViewById(R.id.tv_paishe);
        tv_shangchuan = findViewById(R.id.tv_shangchuan);
        refresh.bringToFront();
        refresh.setOnClickListener(v->{
            fetchFeed(mListView);
        });
        tv_paishe.setOnClickListener(v -> {

            Log.d("shouye", "pressed");
            showPopupWindow();
        });
        tv_shangchuan.setOnClickListener(v -> {
            Log.d("shangchuan", "pressed");
            startActivity(new Intent(MainActivity.this, SentActivity.class));
        });
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_camera, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //设置各个控件的点击响应
        //显示PopupWindow
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

        if (Utils.isPermissionsReady(MainActivity.this, permissions)) {

        } else {

            Utils.reuqestPermissions(MainActivity.this, permissions, REQUEST_EXTERNAL_STORAGE);
        }
        mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        mSurfaceView = contentView.findViewById(R.id.img);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    Log.d("Camera", "ErrorIO");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });


        contentView.findViewById(R.id.btn_picture).setOnClickListener(v -> {

            mCamera.takePicture(null, null, mPicture);
        });
        buttonRecord = contentView.findViewById(R.id.btn_record);
        buttonRecord.setOnClickListener(v -> {

            RecordVideo();
            buttonRecord.setText(isRecording ? "RecOn" : "RecOff");
        });

        contentView.findViewById(R.id.btn_facing).setOnClickListener(v -> {

            mCamera.stopPreview();
            Log.d("Camera", "Mark_4");

            if (CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK) {

                Log.d("Camera", "Mark_5");
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                startPreview(mSurfaceHolder);
                //mCamera.startPreview();
            } else {

                Log.d("Camera", "Mark_6");
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                startPreview(mSurfaceHolder);
                // mCamera.startPreview();
            }


        });

        contentView.findViewById(R.id.btn_zoom).setOnClickListener(v -> {

            mCamera.autoFocus(null);

        });

        //课后探索
        buttonLight = contentView.findViewById(R.id.btn_Light);
        buttonLight.setOnClickListener(v -> {
            if (CAMERA_TYPE != Camera.CameraInfo.CAMERA_FACING_BACK) {
                Toast.makeText(this, "前置镜头无闪光灯", Toast.LENGTH_SHORT).show();
                return;
            }
            isLightOn = !isLightOn;
            if (isLightOn) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
            buttonLight.setText(isLightOn ? "关闭闪光" : "开启闪光");
        });
        buttonDelay = contentView.findViewById(R.id.btn_delay);
        buttonDelay.setOnClickListener(v -> {
            new CountDownTimer(5 * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    buttonDelay.setText("" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {

                    buttonDelay.setText("delay");
                    buttonRecord.setText(isRecording ? "结束" : "开始");
                    RecordVideo();
                    buttonRecord.setText(isRecording ? "开始" : "结束");

                }
            }.start();
        });
    }


    public void RecordVideo() {
        if (isRecording) {

            isRecording = false;
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_VIDEO))));
            Toast.makeText(this, "拍摄结束", Toast.LENGTH_SHORT).show();

        } else {

            isRecording = true;
            mMediaRecorder = new MediaRecorder();

            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            Log.d("Camera", "Mark_1");
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

            Log.d("Camera", "Mark_2");
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            Log.d("Camera", "Mark_3");
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

            mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

            mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
            mMediaRecorder.setOrientationHint(rotationDegree);

            try {
                mMediaRecorder.prepare();
                Toast.makeText(this, "开始摄影", Toast.LENGTH_SHORT).show();
                mMediaRecorder.start();
            } catch (Exception e) {
                releaseMediaRecorder();
                return;
            }
        }
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            Log.d("Camera", "Mark_8_1");
            releaseCameraAndPreview();
        }
        Log.d("Camera", "Mark_8_2");
        Camera cam = Camera.open(position);



        //cam.cancelAutoFocus();
        Log.d("Camera", "Mark_8_3");
        cam.cancelAutoFocus();
        cam.setDisplayOrientation(getCameraDisplayOrientation(CAMERA_TYPE));
        return cam;
    }


    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {

        mCamera.release();
        mCamera = null;
    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {

        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            return;
        }
        mCamera.startPreview();
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {


        return true;
    }


    private void releaseMediaRecorder() {


    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
            Toast.makeText(this, "图片存在： \"" + pictureFile.getAbsolutePath() + "\"", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }

        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
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

    public void fetchFeed(View view) {

        Call<SentActivity.Feeds> call = getDouyinService().getVideo();
        call.enqueue(new Callback<SentActivity.Feeds>() {
            @Override
            public void onResponse(Call<SentActivity.Feeds> call, Response<SentActivity.Feeds> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mList = response.body().feeds;
                    mListView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SentActivity.Feeds> call, Throwable t) {
                Toast.makeText(MainActivity.this, "retrofit: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
