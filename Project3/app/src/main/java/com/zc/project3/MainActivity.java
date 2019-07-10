package com.zc.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button btn1,btn2,btn3,btn4;
    private ImageView mImageView;
    private int flag1,flag2,flag3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn_Scale);
        btn2 = findViewById(R.id.btn_Alpha);
        btn3 = findViewById(R.id.btn_Combine);
        btn4 = findViewById(R.id.btn_Lottie);

        mImageView = findViewById(R.id.iv_animator);

        flag1 = flag2 = flag3 = 0;

        //对缩放动画进行设置
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mImageView,
                "scaleX", 1, 2);
        objectAnimatorX.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorX.setInterpolator(new LinearInterpolator());
        objectAnimatorX.setDuration(1000);
        objectAnimatorX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mImageView,
                "scaleY", 1, 2);
        objectAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorY.setInterpolator(new LinearInterpolator());
        objectAnimatorY.setDuration(1000);
        objectAnimatorY.setRepeatMode(ValueAnimator.REVERSE);
        final AnimatorSet animatorSet_Scale = new AnimatorSet();
        animatorSet_Scale.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet_Scale.setDuration(1000);

        //对透明度动画进行设置
        final ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(mImageView,
                "Alpha",1f,0.5f);
        objectAnimatorAlpha.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorAlpha.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimatorAlpha.setDuration(1000);

        //对组合动画进行设置
        final AnimatorSet animatorSet_Combine = new AnimatorSet();
        animatorSet_Combine.playTogether(objectAnimatorX,objectAnimatorY,objectAnimatorAlpha);
        animatorSet_Combine.setDuration(1000);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(flag1 == 0){
                        animatorSet_Scale.start();
                        flag1 = 1;
                    }
                    else{
                        animatorSet_Scale.cancel();
                        flag1 = 0;
                    }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag2 == 0){
                    objectAnimatorAlpha.start();
                    flag2 = 1;
                }
                else{
                    objectAnimatorAlpha.cancel();
                    flag2 = 0;
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag3 == 0){
                    animatorSet_Combine.start();
                    flag3 = 1;
                }
                else{
                    animatorSet_Combine.cancel();
                    flag3 = 0;
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LottieActivity.class));
            }
        });
    }
}
