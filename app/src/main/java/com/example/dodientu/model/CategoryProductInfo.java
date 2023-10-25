package com.example.dodientu.model;

public class CategoryProductInfo {
    private String productImage,productName,productPrice,productExpiryDate;
    private boolean isFavorite;

    public CategoryProductInfo() {
    }

    public CategoryProductInfo(String productImage, String productName, String productPrice, String productExpiryDate, boolean isFavorite) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productExpiryDate = productExpiryDate;
        this.isFavorite = isFavorite;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductExpiryDate() {
        return productExpiryDate;
    }

    public void setProductExpiryDate(String productExpiryDate) {
        this.productExpiryDate = productExpiryDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
