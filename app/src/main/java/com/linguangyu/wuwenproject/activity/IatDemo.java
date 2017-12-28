package com.linguangyu.wuwenproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.linguangyu.wuwenproject.MainActivity;
import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.adapter.ListviewAdapter;
import com.linguangyu.wuwenproject.db.MsgInfo;
import com.linguangyu.wuwenproject.util.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 光裕 on 2017/10/31.
 */

public class IatDemo extends Activity implements View.OnClickListener {
    private static String TAG = IatDemo.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private final String[] items = new String[] { "重读", "删除全部", "复制"};


    private long i = 0;
    private long k = 0;

    //剪切板管理工具类
    private ClipboardManager mClipboardManager;
    //剪切板Data对象
    private ClipData mClipData;


    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue ;

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    //接收CommonActivity传来的数据
    private String commonEdit;
    private EditText editText_write;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private ListviewAdapter adapter;
    private List<MsgInfo> msgInfoList = new ArrayList<>();

    //存储交流数据
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private TextView mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private ListView listView;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private boolean mTranslateEnable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yuyin);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        mResultText = ((TextView) findViewById(R.id.text_left));

        adapter = new ListviewAdapter(msgInfoList,IatDemo.this);
        listView = (ListView) findViewById(R.id.list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                new AlertDialog.Builder(IatDemo.this)
                        .setTitle("选择")
                        .setIcon(R.drawable.ic_action_list)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (items[which].equals("重读")) {
                                    // 移动数据分析，收集开始合成事件
                                    FlowerCollector.onEvent(IatDemo.this, "tts_play");
                                    TextView right = (TextView) view.findViewById(R.id.text_right);
                                    TextView left = (TextView) view.findViewById(R.id.text_left);
                                    String data_right = right.getText().toString();
                                    String data_left =  left.getText().toString();
                                    // 设置参数
                                    setTtsParam();

                                    if (data_right == ""){
                                        int code_left = mTts.startSpeaking(data_left, mTtsListener);
                                        if (code_left != ErrorCode.SUCCESS) {
                                            showTip("语音合成失败,错误码: " + code_left);
                                        }
                                    }else {
                                       int code_right = mTts.startSpeaking(data_right, mTtsListener);
                                        if (code_right != ErrorCode.SUCCESS) {
                                            showTip("语音合成失败,错误码: " + code_right);
                                        }
                                    }

                                }else if (items[which].equals("复制")) {
                                    TextView right_copy = (TextView) view.findViewById(R.id.text_right);
                                    TextView left_copy = (TextView) view.findViewById(R.id.text_left);
                                    String copy_right = right_copy.getText().toString();
                                    String copy_left = left_copy.getText().toString();
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    if (copy_right == "") {
                                        //创建一个新的文本clip对象
                                        cm.setText(copy_left);
                                    }else {
                                        cm.setText(copy_right);
                                    }
                                }else if (items[which].equals("删除全部")){

                                    editor.clear();
                                    editor.putLong("Id",0);
                                    editor.commit();
                                    listView.removeAllViewsInLayout();

                                }

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            }
        });

        //将i得到存储在本地数据库的值，以便后面i的值继续递增
        i = pref.getLong("Id",k);

        //循环遍历存储在数据库的聊天数据
        for (long j=1; j <=i;j++) {

            String iat_data = pref.getString("Iat_data_"+j,"");
            String tts_data = pref.getString("Tts_data_"+j,"");
            //判断循环左边还是右边
            if (iat_data != "") {
                msgInfoList.add(new MsgInfo(iat_data,null));
            }else msgInfoList.add(new MsgInfo(null,tts_data));
        }
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(listView.getCount() - 1);

        initLayout();



        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(IatDemo.this, mInitListener);

        // 云端发音人名称列表
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(IatDemo.this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);



    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.button_yuyin).setOnClickListener(IatDemo.this);
        findViewById(R.id.edit_write).setOnClickListener(IatDemo.this);
        findViewById(R.id.setting).setOnClickListener(IatDemo.this);
        findViewById(R.id.button_send).setOnClickListener(IatDemo.this);
        findViewById(R.id.tts_cancel).setOnClickListener(IatDemo.this);
        findViewById(R.id.tts_pause).setOnClickListener(IatDemo.this);
        findViewById(R.id.tts_resume).setOnClickListener(IatDemo.this);
        findViewById(R.id.speeder).setOnClickListener(IatDemo.this);
        findViewById(R.id.back_2).setOnClickListener(IatDemo.this);
        findViewById(R.id.iatCommonAdd).setOnClickListener(IatDemo.this);
        editText_write = (EditText) findViewById(R.id.edit_write);
        Intent intent1 = getIntent();
        commonEdit = intent1.getStringExtra("2");
        editText_write.setText(commonEdit);

    }

    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View v) {

        if( null == mIat ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return;
        }

        switch (v.getId()) {
            // 进入参数设置页面
            case R.id.setting:
                Intent intents = new Intent(IatDemo.this, IatSettings.class);
                startActivity(intents);
                break;
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.button_yuyin:

                //设置i，保存i的数据，以便下次循环
                i = i+1;
                editor.putLong("Id",i);
                editor.apply();

                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(IatDemo.this, "iat_recognize");
//                mResultText.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setIatParam();

                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_title_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));

                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败,错误码：" + ret);
                    } else {
                        showTip(getString(R.string.text_begin));
                    }
                }
                break;
            //开始发送
            case R.id.button_send:

                //设置i，保存i的数据，以便下次循环
                i = i+1;
                editor.putLong("Id",i);
                editor.apply();

                // 移动数据分析，收集开始合成事件
                FlowerCollector.onEvent(IatDemo.this, "tts_play");
                String text = editText_write.getText().toString();
                msgInfoList.add(new MsgInfo(null,text));
                editor.putString("Tts_data_"+i,text);
                editor.apply();

                adapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(listView.getCount() - 1);
                // 设置参数
                setTtsParam();
                int code = mTts.startSpeaking(text, mTtsListener);
                ((EditText) findViewById(R.id.edit_write)).setText("");
                //			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code);
                }
                break;
            // 取消合成
            case R.id.tts_cancel:
                mTts.stopSpeaking();
                break;
            // 暂停播放
            case R.id.tts_pause:
                mTts.pauseSpeaking();
                break;
            // 继续播放
            case R.id.tts_resume:
                mTts.resumeSpeaking();
                break;
            // 选择发音人
            case R.id.speeder:
                showPresonSelectDialog();
                break;
            case R.id.back_2:
                finish();
                break;
            case R.id.edit_write:
                listView.smoothScrollToPosition(listView.getCount() - 1);
                break;
            case R.id.iatCommonAdd:
                Intent intent3 = new Intent(IatDemo.this,GroupActivity.class);
                startActivity(intent3);

        }

    }



    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
            Log.d(TAG, "InitListener init() code = " + code);

        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if( mTranslateEnable ){
                printTransResult( results );
            }else{
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private int selectedNum = 0;
    /**
     * 发音人选择。
     */
    private void showPresonSelectDialog() {
        new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
                .setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项,各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                voicer = mCloudVoicersValue[which];
                                selectedNum = which;
                                dialog.dismiss();
                            }
                        }).show();

    }



    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };



    /**
     * 参数设置
     *
     * @return
     */
    public void setIatParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
        if( mTranslateEnable ){
            Log.i( TAG, "translate enable" );
            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }


    /**
     * 参数设置
     * @return
     */
    private void setTtsParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

        }
        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

    };

        private void printTransResult(RecognizerResult results) {
            String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
            String oris = JsonParser.parseTransResult(results.getResultString(), "src");

            if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
                showTip("解析结果失败，请确认是否已开通翻译功能。");
            } else {
                mResultText.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
            }

        }

        private void printResult(RecognizerResult results) {
            String text = JsonParser.parseIatResult(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            msgInfoList.add(new MsgInfo(resultBuffer.toString(),null));

            editor.putString("Iat_data_"+i,resultBuffer.toString());
            editor.apply();

            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(listView.getCount() - 1);

        }

        private void showTip(final String str) {
            mToast.setText(str);
            mToast.show();
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(IatDemo.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }
    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(IatDemo.this);
        super.onPause();
    }

}