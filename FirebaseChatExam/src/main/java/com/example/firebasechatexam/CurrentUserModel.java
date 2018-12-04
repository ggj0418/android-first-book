package com.example.ggj04.sejongtalk.model;

public class CurrentUserModel {
    public String userName;
    public String profileImageUrl;

    public CurrentUserModel () {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}