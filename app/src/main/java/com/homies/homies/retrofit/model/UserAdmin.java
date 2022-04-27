package com.homies.homies.retrofit.model;

public class UserAdmin {
    private int id;
    private byte photo;
    private String photoContentType;
    private String phone;
    private boolean premium;
    private String birthDate;
    private String addDate;
    UserRequest userAdmin;
    GroupRequest group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getPhoto() {
        return photo;
    }

    public void setPhoto(byte photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public UserRequest getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserRequest userAdmin) {
        this.userAdmin = userAdmin;
    }

    public GroupRequest getGroup() {
        return group;
    }

    public void setGroup(GroupRequest group) {
        this.group = group;
    }
}
