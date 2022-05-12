package com.homies.homies.retrofit.model.tasksModels;


import com.homies.homies.retrofit.model.TaskList;
import com.homies.homies.retrofit.model.UserData;

public class getAllTaskForOneUserResponse {
    private int id;
    private String taskName;
    private String dataCreate;
    private String dataEnd;
    private String dataDescription;
    private boolean cancel;
    private String photo;
    private TaskList taskList;
    private UserData userData;
    private UserData userAssigneds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(String dataCreate) {
        this.dataCreate = dataCreate;
    }

    public String getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(String dataEnd) {
        this.dataEnd = dataEnd;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserAssigneds() {
        return userAssigneds;
    }

    public void setUserAssigneds(UserData userAssigneds) {
        this.userAssigneds = userAssigneds;
    }
}
