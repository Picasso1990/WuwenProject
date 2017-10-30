package com.linguangyu.wuwenproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.linguangyu.wuwenproject.MainActivity;
import com.linguangyu.wuwenproject.view.MyFragment_1;
import com.linguangyu.wuwenproject.view.MyFragment_2;
import com.linguangyu.wuwenproject.view.MyFragment_3;

/**
 * Created by 光裕 on 2017/10/25.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private MyFragment_1 myFragment1 = null;
    private MyFragment_2 myFragment2 = null;
    private MyFragment_3 myFragment3 = null;



    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new MyFragment_1();
        myFragment2 = new MyFragment_2();
        myFragment3 = new MyFragment_3();

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case MainActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case MainActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
        }
        return fragment;
    }



    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

}
