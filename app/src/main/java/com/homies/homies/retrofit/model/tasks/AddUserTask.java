package com.homies.homies.retrofit.model.tasks;

import com.homies.homies.retrofit.model.UserData;

import java.util.List;

public class AddUserTask {

    private int idTask;
    private String login;
    private int idList;

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }
}
