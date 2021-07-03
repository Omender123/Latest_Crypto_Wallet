package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendCoinHistoryResponse {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("userPublicKey")
        @Expose
        private String userPublicKey;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("receiver")
        @Expose
        private String receiver;
        @SerializedName("amtOfCrypto")
        @Expose
        private String  amtOfCrypto;
        @SerializedName("withdrawlFees")
        @Expose
        private Integer withdrawlFees;
        @SerializedName("transactionHash")
        @Expose
        private String transactionHash;
        @SerializedName("cryptoCurrency")
        @Expose
        private String cryptoCurrency;
        @SerializedName("userId")
        @Expose
        private UserId userId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public String getUserPublicKey() {
            return userPublicKey;
        }

        public void setUserPublicKey(String userPublicKey) {
            this.userPublicKey = userPublicKey;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getAmtOfCrypto() {
            return amtOfCrypto;
        }

        public void setAmtOfCrypto(String amtOfCrypto) {
            this.amtOfCrypto = amtOfCrypto;
        }

        public Integer getWithdrawlFees() {
            return withdrawlFees;
        }

        public void setWithdrawlFees(Integer withdrawlFees) {
            this.withdrawlFees = withdrawlFees;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getCryptoCurrency() {
            return cryptoCurrency;
        }

        public void setCryptoCurrency(String cryptoCurrency) {
            this.cryptoCurrency = cryptoCurrency;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
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
        public class UserId {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("username")
            @Expose
            private String username;

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

        }
    }
}
