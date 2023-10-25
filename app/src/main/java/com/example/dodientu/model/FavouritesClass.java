package com.example.dodientu.model;

public class FavouritesClass {
    private String prouductImage,productTitle,productPrice;
    private boolean checked;

    public FavouritesClass() {
    }

    public FavouritesClass(String prouductImage, String productTitle, String productPrice, boolean checked) {
        this.prouductImage = prouductImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.checked = checked;
    }

    public String getProuductImage() {
        return prouductImage;
    }

    public void setProuductImage(String prouductImage) {
        this.prouductImage = prouductImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
