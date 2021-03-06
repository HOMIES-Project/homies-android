package com.homies.homies.retrofit.model.tasks;

import com.homies.homies.retrofit.model.user.UserAdmin;
import com.homies.homies.retrofit.model.user.UserData;

import java.util.List;

public class TaskListResponse {

    private int id;
    private String taskName;
    private String dataCreate;
    private String dataEnd;
    private String description;
    private boolean cancel;
    private String photo;
    private String photoContentType;
    private int puntuacion;
    private TaskList taskList;
    private UserData userData;
    private String userCreator;
    private UserAdmin userAdmin;
    private List<UserData> userAssigneds;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
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

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public UserAdmin getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserAdmin userAdmin) {
        this.userAdmin = userAdmin;
    }

    public List<UserData> getUserAssigneds() {
        return userAssigneds;
    }

    public void setUserAssigneds(List<UserData> userAssigneds) {
        this.userAssigneds = userAssigneds;
    }
}

