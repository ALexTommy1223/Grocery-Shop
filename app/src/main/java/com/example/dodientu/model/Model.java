package com.example.dodientu.model;

public class Model {
    private String image;
    private String title;
    private String decs;

    public Model() {
    }

    public Model(String image, String title, String decs) {
        this.image = image;
        this.title = title;
        this.decs = decs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }
}
