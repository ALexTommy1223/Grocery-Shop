package com.example.dodientu.model;

public class HorizontalProductModel {
    private String productImage;
    private String productTitle;
    private String productPrice;
    private boolean checked;
    private String expiredDate;

    public HorizontalProductModel() {
    }

    public HorizontalProductModel(String productImage, String productTitle, String productPrice, boolean checked, String expiredDate) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.checked = checked;
        this.expiredDate = expiredDate;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
