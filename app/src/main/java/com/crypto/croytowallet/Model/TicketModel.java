package com.crypto.croytowallet.Model;

public class TicketModel {
    String Subject,Description,Date,Status,UserId;

    public TicketModel(String subject, String description, String date, String status, String userId) {
        Subject = subject;
        Description = description;
        Date = date;
        Status = status;
        UserId = userId;
    }

    public TicketModel() {
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
