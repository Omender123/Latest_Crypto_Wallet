package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopUp_HistoryResponse  implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("upiId")
    @Expose
    private String upiId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("utility")
    @Expose
    private String utility;
    @SerializedName("attachement")
    @Expose
    private String attachement;
    @SerializedName("reward")
    @Expose
    private String reward;

    /**
     * No args constructor for use in serialization
     *
     */
    public TopUp_HistoryResponse() {
    }


    public TopUp_HistoryResponse(String id, String status, String accountName, String accountNo, String customerName, String currency, String transactionId, String amount, String paymentMode, String upiId, String userId, String createdAt, String updatedAt, String utility, String attachement, String reward) {
        super();
        this.id = id;
        this.status = status;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.customerName = customerName;
        this.currency = currency;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.upiId = upiId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.utility = utility;
        this.attachement = attachement;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }}
