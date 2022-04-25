package com.homies.homies.services;

public class DeleteUserGroupRequest {

    private int idAdminGroup;
    private String login;
    private int idGroup;

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
}
