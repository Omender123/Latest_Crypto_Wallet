package com.crypto.croytowallet.Model;

public class TicketChatModel {
    String RoleId,Message,Time,messageId;

    public TicketChatModel(String roleId, String message, String time,String messageId) {
        this.RoleId = roleId;
        this.Message = message;
        this.Time = time;
        this.messageId=messageId;
    }

    public TicketChatModel() {
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
