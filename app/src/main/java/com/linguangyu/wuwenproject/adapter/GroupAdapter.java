package com.linguangyu.wuwenproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linguangyu.wuwenproject.R;
import com.linguangyu.wuwenproject.db.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 光裕 on 2017/11/4.
 */

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private List<GroupInfo> mListGroup = new ArrayList<>();
    private ViewHolder viewHolder;

    public GroupAdapter(List<GroupInfo> mListGroup , Context context) {
        this.context = context;
        this.mListGroup = mListGroup;
    }

    @Override
    public int getCount() {
        return mListGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return mListGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_group,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String groupName = mListGroup.get(position).getGroupName();

        if (groupName != null) {
            viewHolder.textView_group.setText(groupName);
            viewHolder.item_group.setVisibility(View.VISIBLE);
        }else {
            return null;
        }

        return convertView;
    }

    private static class ViewHolder {

        public View mView;
        public RelativeLayout item_group;
        public TextView textView_group;

        public ViewHolder(View mView) {
            this.mView = mView;
            this.item_group = (RelativeLayout) mView.findViewById(R.id.rl_item_group);
            this.textView_group = (TextView) mView.findViewById(R.id.text_item_group);
        }


    }

}
