package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardHistoryResponse {
    @SerializedName("result")
    @Expose
    private Result[] results;

    public Result[] getResults() {
        return results;
    }

    public static class Result   implements Serializable {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("senderId")
        @Expose
        private String senderId;
        @SerializedName("closingBalance")
        @Expose
        private String  closingBalance;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("amount")
        @Expose
        private String  amount;
        @SerializedName("earnedReward")
        @Expose
        private String  earnedReward;
        @SerializedName("senderName")
        @Expose
        private String senderName;
        @SerializedName("receiverName")
        @Expose
        private String receiverName;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("receiverId")
        @Expose
        private String receiverId;
        @SerializedName("withdrawlFees")
        @Expose
        private Integer withdrawlFees;
        
        public Result() {
        }
        public Result(String type, String id, String senderId, String closingBalance, String status, String amount, String earnedReward, String senderName, String receiverName, String createdAt, String updatedAt, String receiverId, Integer withdrawlFees) {
            super();
            this.type = type;
            this.id = id;
            this.senderId = senderId;
            this.closingBalance = closingBalance;
            this.status = status;
            this.amount = amount;
            this.earnedReward = earnedReward;
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.receiverId = receiverId;
            this.withdrawlFees = withdrawlFees;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getClosingBalance() {
            return closingBalance;
        }

        public void setClosingBalance(String closingBalance) {
            this.closingBalance = closingBalance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getEarnedReward() {
            return earnedReward;
        }

        public void setEarnedReward(String earnedReward) {
            this.earnedReward = earnedReward;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
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

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public Integer getWithdrawlFees() {
            return withdrawlFees;
        }

        public void setWithdrawlFees(Integer withdrawlFees) {
            this.withdrawlFees = withdrawlFees;
        }
    }

}
