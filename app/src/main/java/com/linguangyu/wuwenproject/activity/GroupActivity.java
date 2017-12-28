package com.linguangyu.wuwenproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.adapter.GroupAdapter;
import com.linguangyu.wuwenproject.db.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 光裕 on 2017/11/4.
 */

public class GroupActivity extends Activity {

    private GroupAdapter groupAdapter;
    private List<GroupInfo> mGroup = new ArrayList<>();
    private ImageView imageAdd;
    private ImageView imageBack;
    private long i;
    private long k;
    private ListView listView;
    private EditText editGroupName;

    private final String[] itemGroup = new String[] { "删除全部"};

    //存储分组数据
    private SharedPreferences pref2;
    private SharedPreferences.Editor editor2;

    //删除组内数据
    private SharedPreferences pref1;
    private SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group);
//        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        editor2 = getSharedPreferences("wuwenproject.groupdata",MODE_PRIVATE).edit();
        //editor2 = pref2.edit();
        pref2 = getSharedPreferences("wuwenproject.groupdata",MODE_PRIVATE);

        editor1 = getSharedPreferences("wuwenproject.commondata",MODE_PRIVATE).edit();
        pref1 = getSharedPreferences("wuwenproject.commondata",MODE_PRIVATE);

        groupAdapter = new GroupAdapter(mGroup,GroupActivity.this);
        listView = (ListView) findViewById(R.id.list_group);
        listView.setAdapter(groupAdapter);

        imageBack = (ImageView) findViewById(R.id.back_3);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //将i得到存储在本地数据库的值，以便后面i的值继续递增
        i = pref2.getLong("groupId",k);

        //循环遍历存储在数据库的聊天数据
        for (long j=1; j <=i;j++) {

            String groupName = pref2.getString("group_name"+j,"");
            mGroup.add(new GroupInfo(groupName));
            groupAdapter.notifyDataSetChanged();
//            listView.smoothScrollToPosition(listView.getCount() - 1);

        }

        imageAdd = (ImageView) findViewById(R.id.image_add);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                // 获取布局
                View view2 = View.inflate(GroupActivity.this, R.layout.diolog_add, null);

                editGroupName = (EditText) view2.findViewById(R.id.edit_add_name);

                // 设置参数
                builder.setTitle("添加组名").setIcon(R.mipmap.ic_group_add)
                        .setView(view2)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String groupName1 = editGroupName.getText().toString();
                                if (groupName1+"1" == "1") {
                                    Toast.makeText(GroupActivity.this,"组名不能为空",Toast.LENGTH_SHORT).show();
                                }else{
                                    mGroup.add(new GroupInfo(groupName1));
                                    groupAdapter.notifyDataSetChanged();
                                    listView.smoothScrollToPosition(listView.getCount() - 1);
                                    i = i+1;
                                    editor2.putLong("groupId",i);
                                    editor2.putString("group_name"+i,groupName1);
                                    editor2.apply();
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

                Bundle data = new Bundle();
                Intent intent = new Intent(GroupActivity.this,CommonActivity.class);
                TextView groupText = (TextView) view.findViewById(R.id.text_item_group);
                String groupTextName = groupText.getText().toString();
                data.putString("1",groupTextName);
                intent.putExtras(data);
                startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("选择")
                        .setIcon(R.drawable.ic_action_list)
                        .setItems(itemGroup, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (itemGroup[which].equals("删除全部")) {
                                    editor2.clear();
                                    editor1.clear();
                                    editor2.putLong("groupId",0);
                                    editor2.commit();
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
