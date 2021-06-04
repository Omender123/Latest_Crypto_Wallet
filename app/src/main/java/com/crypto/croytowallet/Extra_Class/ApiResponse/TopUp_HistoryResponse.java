package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopUp_HistoryResponse  implements Serializable {
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("upiId")
    @Expose
    private String upiId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("attachement")
    @Expose
    private String attachement;
    @SerializedName("utility")
    @Expose
    private String utility;

    /**
     * No args constructor for use in serialization
     *
     */
    public TopUp_HistoryResponse() {
    }
    
    public TopUp_HistoryResponse(String reward, String status, String id, String accountName, String accountNo, String customerName, String transactionId, String upiId, String amount, String paymentMode, String currency, String userId, String createdAt, String updatedAt, String attachement, String utility) {
        super();
        this.reward = reward;
        this.status = status;
        this.id = id;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.customerName = customerName;
        this.transactionId = transactionId;
        this.upiId = upiId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.currency = currency;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attachement = attachement;
        this.utility = utility;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }


}
