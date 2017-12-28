package com.linguangyu.wuwenproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.linguangyu.wuwenproject.R;

/**
 * Created by 光裕 on 2017/11/6.
 */

public class RegardActivity extends Activity {
    private ImageView back_regard;
    private ImageView right_regard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.regard_we);

        back_regard = (ImageView) findViewById(R.id.back_regard);
        right_regard = (ImageView) findViewById(R.id.image_right);

        back_regard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right_regard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegardActivity.this,"抱歉，该功能还未开发",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
