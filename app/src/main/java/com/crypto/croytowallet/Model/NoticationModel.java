package com.crypto.croytowallet.Model;

public class NoticationModel {
    String landingImages,Images,details;

    public NoticationModel(String landingImages, String images, String details) {
        this.landingImages = landingImages;
        Images = images;
        this.details = details;
    }

    public NoticationModel() {
    }

    public String getLandingImages() {
        return landingImages;
    }

    public void setLandingImages(String landingImages) {
        this.landingImages = landingImages;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
