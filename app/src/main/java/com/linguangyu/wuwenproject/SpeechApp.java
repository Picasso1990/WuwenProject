package com.linguangyu.wuwenproject;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by 光裕 on 2017/10/31.
 */

public class SpeechApp extends Application {
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59faf9cb");
        super.onCreate();
    }
}
