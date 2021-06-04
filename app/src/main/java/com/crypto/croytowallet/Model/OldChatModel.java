package com.crypto.croytowallet.Model;

public class OldChatModel {
    String username,userId,date,chatId;

    public OldChatModel(String username, String userId, String date, String chatId) {
        this.username = username;
        this.userId = userId;
        this.date = date;
        this.chatId = chatId;
    }

    public OldChatModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
