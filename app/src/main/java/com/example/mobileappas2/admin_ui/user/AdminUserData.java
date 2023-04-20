package com.example.mobileappas2.admin_ui.user;

public class AdminUserData {
    private String username, email;
    private Integer ID;

    public AdminUserData(String val1, String val2, Integer val3)
    {
        username = val1;
        email = val2;
        ID = val3;
    }
    public AdminUserData() {}

    public String getTitle() {
        return username;
    }

    public void setTitle(String title) {
        this.username = title;
    }

    public String getDescription() {
        return email;
    }

    public void setDescription(String description) {
        this.email = description;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
