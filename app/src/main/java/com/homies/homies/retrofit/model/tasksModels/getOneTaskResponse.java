package com.homies.homies.retrofit.model.tasksModels;

import com.homies.homies.retrofit.model.GroupResponse;

public class getOneTaskResponse {

    private int id;
    private String nameList;
    private GroupResponse group;
    private TaskEntity task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameList() {
        return nameList;
    }

    public void setNameList(String nameList) {
        this.nameList = nameList;
    }

    public GroupResponse getGroup() {
        return group;
    }

    public void setGroup(GroupResponse group) {
        this.group = group;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }
}
