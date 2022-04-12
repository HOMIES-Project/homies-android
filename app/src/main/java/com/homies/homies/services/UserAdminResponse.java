package com.homies.homies.services;

public class UserAdminResponse {
    private Integer idAdminGroup;
    private String login;
    private Integer idGroup;

    public Integer getIdAdminGroup() {
        return idAdminGroup;
    }

    public void setIdAdminGroup(Integer idAdminGroup) {
        this.idAdminGroup = idAdminGroup;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }
}
