package com.example.dodientu.model;

public class AdminOffers {
    private String offerName,offerDescription,offerImg;

    public AdminOffers() {
    }

    public AdminOffers(String offerName, String offerDescription, String offerImg) {
        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.offerImg = offerImg;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }
}
