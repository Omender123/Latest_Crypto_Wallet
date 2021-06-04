package com.crypto.croytowallet.Model;

public class CountryModel {
    String image,countryName,countryCode;

    public CountryModel(String image, String countryName, String countryCode) {
        this.image = image;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public CountryModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
