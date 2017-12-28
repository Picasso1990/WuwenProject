package com.linguangyu.wuwenproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.linguangyu.wuwenproject.R;

/**
 * Created by 光裕 on 2017/11/6.
 */

public class EmailActivity extends Activity {
    private EditText send_title;
    private EditText send_nr;
    private Button send_email;
    private Button send_clean;
    private ImageView send_back;
    private String titleString;
    private String nrString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_email);

        send_back = (ImageView) findViewById(R.id.back_send);
        send_clean = (Button) findViewById(R.id.clean_button);
        send_email = (Button) findViewById(R.id.send_button);
        send_nr = (EditText) findViewById(R.id.edit_nr_send);
        send_title = (EditText) findViewById(R.id.edit_title_send);



        send_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_title.setText("");
                send_nr.setText("");
            }
        });

        send_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleString = send_title.getText().toString();
                nrString = send_nr.getText().toString();
                Intent data_email =new Intent(Intent.ACTION_SENDTO);
                data_email.setData(Uri.parse("mailto:13430348380@163.com"));
                data_email.putExtra(Intent.EXTRA_SUBJECT, titleString);
                data_email.putExtra(Intent.EXTRA_TEXT, nrString);
                startActivity(data_email);
            }
        });

    }
}
