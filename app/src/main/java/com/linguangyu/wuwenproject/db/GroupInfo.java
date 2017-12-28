package com.linguangyu.wuwenproject.db;

/**
 * Created by 光裕 on 2017/11/4.
 */

public class GroupInfo {

    private String GroupName;

    public GroupInfo (String GroupName) {
        this.GroupName = GroupName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

}
