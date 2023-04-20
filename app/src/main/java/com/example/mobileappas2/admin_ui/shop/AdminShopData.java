package com.example.mobileappas2.admin_ui.shop;

public class AdminShopData {
    private String title, description;
    private Float price;
    private Integer ID;

    public AdminShopData(String val1, String val2, Float val3, Integer val4)
    {
        title = val1;
        description = val2;
        price = val3;
        ID = val4;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
