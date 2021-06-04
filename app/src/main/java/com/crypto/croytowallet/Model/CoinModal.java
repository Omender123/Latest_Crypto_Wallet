package com.crypto.croytowallet.Model;

public class CoinModal {

    String id,username,amount,time,type;

    public CoinModal(String id, String username, String amount, String time, String type) {
        this.id = id;
        this.username = username;
        this.amount = amount;
        this.time = time;
        this.type = type;
    }

    public CoinModal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
