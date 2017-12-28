package com.linguangyu.wuwenproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.adapter.CommonAdapter;
import com.linguangyu.wuwenproject.adapter.GroupAdapter;
import com.linguangyu.wuwenproject.db.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 光裕 on 2017/11/4.
 */

public class CommonActivity extends Activity {

    private CommonAdapter commonAdapter;
    private List<Common> mCommon = new ArrayList<>();
    private ImageView imageAddCommon;
    private ImageView imageBack4;
    private long i;
    private long k;
    private String commonData;
    private ListView listView;
    private TextView titleText;

    private final String[] itemCommon = new String[] { "删除全部"};

    //存储分组数据
    private SharedPreferences pref1;
    private SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_common);
        //pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        //editor1 = pref1.edit();
        editor1 = getSharedPreferences("wuwenproject.commondata",MODE_PRIVATE).edit();
        pref1 = getSharedPreferences("wuwenproject.commondata",MODE_PRIVATE);
        //取出GroupActivity传过来的数据
        Intent intent = getIntent();
        commonData = intent.getStringExtra("1");
        //显示标题名字
        titleText = (TextView) findViewById(R.id.text_common);
        titleText.setText(commonData);

        commonAdapter = new CommonAdapter(mCommon,CommonActivity.this);
        listView = (ListView) findViewById(R.id.list_common);
        listView.setAdapter(commonAdapter);

        imageBack4 = (ImageView) findViewById(R.id.back_4);
        imageBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将i得到存储在本地数据库的值，以便后面i的值继续递增
        i = pref1.getLong("commonId",k);

        //循环遍历存储在数据库的聊天数据
        for (long j=1; j <=i;j++) {

            String commonName = pref1.getString(commonData+j,"");
            if (commonName != ""){
                mCommon.add(new Common(commonName));
                commonAdapter.notifyDataSetChanged();
            }


        }

        imageAddCommon = (ImageView) findViewById(R.id.image_add_common);
        imageAddCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(CommonActivity.this);
                // 获取布局
                View view3 = View.inflate(CommonActivity.this, R.layout.diolog_add, null);

                final EditText editCommonName = (EditText) view3.findViewById(R.id.edit_add_name);

                // 设置参数
                builder.setTitle("添加常用语").setIcon(R.mipmap.ic_group_add)
                        .setView(view3)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String commonName1 = editCommonName.getText().toString();
                                if (commonName1+"1" == "1") {
                                    Toast.makeText(CommonActivity.this,"组名不能为空",Toast.LENGTH_SHORT).show();
                                }else{
                                    mCommon.add(new Common(commonName1));
                                    commonAdapter.notifyDataSetChanged();
                                    listView.smoothScrollToPosition(listView.getCount() - 1);
                                    i = i+1;
                                    editor1.putLong("commonId",i);
                                    editor1.putString(commonData+i,commonName1);
                                    editor1.apply();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 创建对话框
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle dataCommon = new Bundle();
                Intent intent = new Intent(CommonActivity.this,IatDemo.class);
                TextView commonText = (TextView) view.findViewById(R.id.text_item_common);
                String commonTextName = commonText.getText().toString();
                dataCommon.putString("2",commonTextName);
                intent.putExtras(dataCommon);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(CommonActivity.this)
                        .setTitle("选择")
                        .setIcon(R.drawable.ic_action_list)
                        .setItems(itemCommon, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (itemCommon[which].equals("删除全部")) {
                                    editor1.clear();
                                    editor1.putLong("commonId",0);
                                    editor1.commit();
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
    }
}
