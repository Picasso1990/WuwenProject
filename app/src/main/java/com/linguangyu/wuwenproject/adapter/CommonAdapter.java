package com.linguangyu.wuwenproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.db.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 光裕 on 2017/11/4.
 */

public class CommonAdapter extends BaseAdapter {

    private Context context;
    private List<Common> mListCommon = new ArrayList<>();
    private ViewHolder viewHolder;

    public CommonAdapter(List<Common> mListCommon,Context context){
        this.context = context;
        this.mListCommon = mListCommon;
    }

    @Override
    public int getCount() {
        return mListCommon.size();
    }

    @Override
    public Object getItem(int position) {
        return mListCommon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_common,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String groupName = mListCommon.get(position).getCommonPhrase();

        if (groupName != null) {
            viewHolder.textView_common.setText(groupName);
            viewHolder.item_common.setVisibility(View.VISIBLE);
        }else {
            return null;
        }

        return convertView;
    }

    private static class ViewHolder {

        public View mView;
        public RelativeLayout item_common;
        public TextView textView_common;

        public ViewHolder(View mView) {
            this.mView = mView;
            this.item_common = (RelativeLayout) mView.findViewById(R.id.rl_item_common);
            this.textView_common = (TextView) mView.findViewById(R.id.text_item_common);
        }


    }

}
