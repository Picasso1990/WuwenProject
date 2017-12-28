package com.linguangyu.wuwenproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.activity.GroupActivity;
import com.linguangyu.wuwenproject.activity.IatDemo;

/**
 * Created by 光裕 on 2017/10/25.
 */

public class MyFragment_2 extends Fragment {
    public MyFragment_2(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_frament,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button2 = (Button) getActivity().findViewById(R.id.button2);
        Button button_group = (Button) getActivity().findViewById(R.id.button_group);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),IatDemo.class);
                startActivity(intent);
            }
        });

        button_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                startActivity(intent);
            }
        });
    }
}
