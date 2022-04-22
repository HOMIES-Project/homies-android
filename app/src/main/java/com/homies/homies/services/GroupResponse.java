package com.homies.homies.services;

import java.util.Date;

public class GroupResponse {

    private int id;
    private String groupKey;
    private String groupName ;
    private String groupRelation ;
    UserData userAdmin;
    private int user;
    UserData userdata;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public UserData getUserdata() {
        return userdata;
    }

    public void setUserdata(UserData userdata) {
        this.userdata = userdata;
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
