package com.homies.homies.services;

public class UserListResponse {

    private int idAdminGroup;
    private String login;
    private int idGroup;

    private int id;
    private String groupKey;
    private String groupName ;
    private String groupRelation ;
    private String addGroupDate;
    UserAdmin userAdmin;
    UserData userData;
    private String taskList;
    private String user;

    public int getIdAdminGroup() {
        return idAdminGroup;
    }

    public void setIdAdminGroup(int idAdminGroup) {
        this.idAdminGroup = idAdminGroup;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

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

    public String getAddGroupDate() {
        return addGroupDate;
    }

    public void setAddGroupDate(String addGroupDate) {
        this.addGroupDate = addGroupDate;
    }

    public UserAdmin getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserAdmin userAdmin) {
        this.userAdmin = userAdmin;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getTaskList() {
        return taskList;
    }

    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
