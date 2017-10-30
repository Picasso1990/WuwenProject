package com.linguangyu.wuwenproject.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linguangyu.wuwenproject.R;

/**
 * Created by 光裕 on 2017/10/25.
 */

public class MyFragment_3 extends Fragment {
    public MyFragment_3(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.voice_fragment,container,false);
//        TextView textView = (TextView) view.findViewById(R.id.txt_content);
//        textView.setText("分贝");
        return view;
    }
}
