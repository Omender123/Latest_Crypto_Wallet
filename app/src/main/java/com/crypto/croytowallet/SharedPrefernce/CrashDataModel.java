package com.crypto.croytowallet.SharedPrefernce;

public class CrashDataModel {
    String username,password;

    public CrashDataModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CrashDataModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
