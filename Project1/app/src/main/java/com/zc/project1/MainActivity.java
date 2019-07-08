package com.zc.project1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    public int flag1=0;
    public int flag2=0;
    public Switch s;
    public TextView text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn1 = findViewById(R.id.btn1);
        ImageButton btn2 = findViewById(R.id.btn2);
        final ImageView photo1 = findViewById(R.id.photo1);
        final TextView text1 = findViewById(R.id.text1);
        s = findViewById(R.id.s1);
        text2 = findViewById(R.id.text2);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    text2.setText("Hello!");
                }
                else{
                    text2.setText("Bye!");
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag1){
                    case 0:
                        flag1=1;
                        text1.setText("你好！");
                    break;
                    case 1:
                        flag1=0;
                        text1.setText("Hello World!");
                        break;
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(flag2){
                    case 0:
                        flag2=1;
                        photo1.setImageResource(R.drawable.hello2);
                        break;
                    case 1:
                        flag2=0;
                        photo1.setImageResource(R.drawable.hello);
                        break;
                }
            }
        });



    }

}
