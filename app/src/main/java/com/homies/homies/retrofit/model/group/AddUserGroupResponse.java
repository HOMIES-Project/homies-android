package com.homies.homies.retrofit.model.group;

import com.homies.homies.retrofit.model.TaskList;
import com.homies.homies.retrofit.model.UserAdmin;
import com.homies.homies.retrofit.model.UserData;

public class AddUserGroupResponse {

    private int id;
    private String groupKey;
    private String groupName;
    private String groupRelationName;
    private String addGroupDate;
    private UserAdmin userAdmin;
    private TaskList taskList;
    private String spendingList;
    private String shoppingList;
    private String settingsList;
    private UserData userData;


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

    public String getGroupRelationName() {
        return groupRelationName;
    }

    public void setGroupRelationName(String groupRelationName) {
        this.groupRelationName = groupRelationName;
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

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public String getSpendingList() {
        return spendingList;
    }

    public void setSpendingList(String spendingList) {
        this.spendingList = spendingList;
    }

    public String getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(String shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getSettingsList() {
        return settingsList;
    }

    public void setSettingsList(String settingsList) {
        this.settingsList = settingsList;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
