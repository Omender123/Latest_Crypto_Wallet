package com.crypto.croytowallet.Model;

public class SwapModel {
    String sendData, receivedData, coinPrice, currencyType,currencySymbol,coinAmount,enterAmount,userBalance,TotalAmount,type,CoinType;
    int value;



    public SwapModel(String sendData, String receivedData, String coinPrice, String currencyType, String currencySymbol, String coinAmount, String enterAmount, String userBalance, String totalAmount, int value, String type,String CoinType) {
        this.sendData = sendData;
        this.receivedData = receivedData;
        this.coinPrice = coinPrice;
        this.currencyType = currencyType;
        this.currencySymbol = currencySymbol;
        this.coinAmount = coinAmount;
        this.enterAmount = enterAmount;
        this.userBalance = userBalance;
        this.TotalAmount = totalAmount;
        this.value = value;
        this.type = type;
        this.CoinType = CoinType;
    }

    public SwapModel() {
    }

    public String getSendData() {
        return sendData;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(String coinAmount) {
        this.coinAmount = coinAmount;
    }

    public String getEnterAmount() {
        return enterAmount;
    }

    public void setEnterAmount(String enterAmount) {
        this.enterAmount = enterAmount;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoinType() {
        return CoinType;
    }

    public void setCoinType(String coinType) {
        CoinType = coinType;
    }
}
