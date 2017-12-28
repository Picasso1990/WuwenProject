package com.linguangyu.wuwenproject;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.ViewPager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.linguangyu.wuwenproject.activity.EmailActivity;
import com.linguangyu.wuwenproject.activity.HelpActivity;
import com.linguangyu.wuwenproject.activity.IatSettings;
import com.linguangyu.wuwenproject.activity.MainActivityText;
import com.linguangyu.wuwenproject.activity.RegardActivity;
import com.linguangyu.wuwenproject.adapter.MyFragmentPagerAdapter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    //UI Objects
    private Toolbar toolbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_voice;
    private RadioButton rb_chat;
    private RadioButton rb_note;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59ef2312");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_voice.setChecked(true);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //以下是抽屉栏所需的方法
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_good) {

            Toast.makeText(MainActivity.this,"抱歉，该功能正在努力实现，谢谢您的好评",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_feedback) {

            Intent intent_send = new Intent(MainActivity.this, EmailActivity.class);
            startActivity(intent_send);

        } else if (id == R.id.nav_help) {

            Intent intent_help = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent_help);

        } else if (id == R.id.nav_regard) {

            Intent intent_regard = new Intent(MainActivity.this, RegardActivity.class);
            startActivity(intent_regard);

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(MainActivity.this, IatSettings.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            Intent intent_share=new Intent(Intent.ACTION_SEND);
            intent_share.setType("text/plain"); //"image/*"
            intent_share.putExtra(Intent.EXTRA_SUBJECT,"共享软件");
            intent_share.putExtra(Intent.EXTRA_TEXT, "我在安卓市场发现了“无闻”这个软件，快来......下载吧！！");
            intent_share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent_share, "选择分享类型"));

        } else if (id == R.id.nav_send) {

            Intent intent_send = new Intent(MainActivity.this,EmailActivity.class);
            startActivity(intent_send);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //以上是抽屉栏所需的方法

    //以下是导航栏所需的方法
    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_voice = (RadioButton) findViewById(R.id.rb_voice);
        rb_chat = (RadioButton) findViewById(R.id.rb_chat);
        rb_note = (RadioButton) findViewById(R.id.rb_note);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_voice:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_chat:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_note:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_voice.setChecked(true);
                    toolbar.setTitle("分贝");
                    break;
                case PAGE_TWO:
                    rb_chat.setChecked(true);
                    toolbar.setTitle("交流");
                    break;
                case PAGE_THREE:
                    rb_note.setChecked(true);
                    toolbar.setTitle("短信");
                    break;
            }
        }
    }
    //以上是导航栏所需的方法
}
