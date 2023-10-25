package com.example.dodientu.model;

public class Offers {
    private String description,img;
    private String title;

    public Offers() {
    }

    public Offers(String description, String img) {
        this.description = description;
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
