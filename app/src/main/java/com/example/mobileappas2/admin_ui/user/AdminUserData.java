package com.example.mobileappas2.admin_ui.user;

public class AdminUserData {
	// private variables
    private String username, email;
    private Integer ID;

	// construction for the user data	
    public AdminUserData(String val1, String val2, Integer val3)
    {
        username = val1;
        email = val2;
        ID = val3;
    }
	
	// GETTERS & SETTERS
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
