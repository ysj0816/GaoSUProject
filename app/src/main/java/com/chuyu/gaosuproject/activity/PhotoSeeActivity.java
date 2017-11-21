package com.chuyu.gaosuproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chuyu.gaosuproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片查看(检查表填报传过来的路径)
 */
public class PhotoSeeActivity extends AppCompatActivity {

    @BindView(R.id.img_photo)
    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_see);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(path)){
            File file = new File(path);
            Picasso.with(getApplicationContext()).load(file).into(imgPhoto);
        }
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
