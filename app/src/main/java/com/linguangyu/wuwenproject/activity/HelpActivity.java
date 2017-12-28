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

public class HelpActivity extends Activity {

    private ImageView back_help;
    private ImageView help1;
    private ImageView help2;
    private ImageView help3;
    private ImageView help4;
    private ImageView help5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_center);

        back_help = (ImageView) findViewById(R.id.back_help);
        help1 = (ImageView) findViewById(R.id.image_help_1);
        help2 = (ImageView) findViewById(R.id.image_help_2);
        help3 = (ImageView) findViewById(R.id.image_help_3);
        help4 = (ImageView) findViewById(R.id.image_help_4);
        help5 = (ImageView) findViewById(R.id.image_help_5);

        back_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"在界面的下方的四个按钮为播放语音交互按钮",Toast.LENGTH_LONG).show();
            }
        });

        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"在说话时说话的间隔短可以一直翻译，时间长停止翻译",Toast.LENGTH_LONG).show();
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"分贝区间参考首页分贝内容",Toast.LENGTH_LONG).show();
            }
        });

        help4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"检查手机的权限是否开启",Toast.LENGTH_LONG).show();
            }
        });

        help5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this,"请到反馈里联系我们",Toast.LENGTH_LONG).show();
            }
        });

    }
}
