package com.example.mobileappas2.ui.shop;

public class ShopDataHolder {
    private String title, description;
    private Float price;

    public ShopDataHolder(String val1, String val2, Float val3)
    {
        title = val1;
        description = val2;
        price = val3;
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
}
