package com.homies.homies.retrofit.model.tasksModels;

public class TaskEntity {

    private int id;
    private String task_name;
    private String data_create;
    private String data_end;
    private String description;
    private boolean cancel;
    private String photo;
    private int puntuacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getData_create() {
        return data_create;
    }

    public void setData_create(String data_create) {
        this.data_create = data_create;
    }

    public String getData_end() {
        return data_end;
    }

    public void setData_end(String data_end) {
        this.data_end = data_end;
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

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
