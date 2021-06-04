package com.crypto.croytowallet.Model;

public class OverViewModel {
    String id,Name,Image,currencyRate,high_price,low_price,symbol;

    String CurrentPrice;

    public OverViewModel(String id, String name, String image, String currencyRate, String high_price, String low_price, String symbol, String currentPrice) {
        this.id = id;
        Name = name;
        Image = image;
        this.currencyRate = currencyRate;
        this.high_price = high_price;
        this.low_price = low_price;
        this.symbol = symbol;
        CurrentPrice = currentPrice;
    }

    public OverViewModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getHigh_price() {
        return high_price;
    }

    public void setHigh_price(String high_price) {
        this.high_price = high_price;
    }

    public String getLow_price() {
        return low_price;
    }

    public void setLow_price(String low_price) {
        this.low_price = low_price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String  getCurrentPrice() {
        return CurrentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        CurrentPrice = currentPrice;
    }
}


