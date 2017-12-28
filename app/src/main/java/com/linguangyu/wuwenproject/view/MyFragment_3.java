package com.linguangyu.wuwenproject.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linguangyu.wuwenproject.R;

import java.util.ArrayList;

/**
 * Created by 光裕 on 2017/10/25.
 */

public class MyFragment_3 extends Fragment {

    private EditText editText1;
    private EditText editText2;
    public MyFragment_3(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText1 = (EditText) getActivity().findViewById(R.id.edit_note_name);
        editText2 = (EditText) getActivity().findViewById(R.id.editText);
        Button button3 = (Button) getActivity().findViewById(R.id.button3);
        Button button_clean = (Button) getActivity().findViewById(R.id.button_clean);


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteName = editText1.getText().toString();
                String note = editText2.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();//得到短信管理器
                   //由于短信可能较长，故将短信拆分
                ArrayList<String> texts = smsManager.divideMessage(note);
                for(String text : texts){
                smsManager.sendTextMessage(noteName, null, text, null, null);//分别发送每一条短信
                }
                  Toast.makeText(getActivity(), "发送成功!", Toast.LENGTH_LONG).show();//提示成功
            }
        });
        button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                editText2.setText("");
            }
        });
    }
}
