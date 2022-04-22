package com.homies.homies.services;

import java.util.Date;
import java.util.List;

public class GroupResponse {

    private int id;
    private String groupKey;
    private String groupName ;
    private String groupRelation ;
    UserData userAdmin;
    List<UserData> userData;
    private Date addGroupDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupRelation() {
        return groupRelation;
    }

    public void setGroupRelation(String groupRelation) {
        this.groupRelation = groupRelation;
    }

    public List<UserData> getUserdata() {
        return userData;
    }

    public void setUserdata(List<UserData> userdata) {
        this.userData = userdata;
    }

    public UserData getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserData userAdmin) {
        this.userAdmin = userAdmin;
    }

    public Date getAddGroupDate() {
        return addGroupDate;
    }

    public void setAddGroupDate(Date addGroupDate) {
        this.addGroupDate = addGroupDate;
    }
}
