package com.crypto.croytowallet.Model;

public class OfferModel {
    String offer_name, shortDescription,longDescription,date,imageUrl,offer_tittle;

    public OfferModel(String offer_name, String shortDescription, String longDescription, String date, String imageUrl, String offer_tittle) {
        this.offer_name = offer_name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.date = date;
        this.imageUrl = imageUrl;
        this.offer_tittle = offer_tittle;
    }

    public OfferModel() {
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOffer_tittle() {
        return offer_tittle;
    }

    public void setOffer_tittle(String offer_tittle) {
        this.offer_tittle = offer_tittle;
    }
}
