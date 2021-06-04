package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBankDetails {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("IFSCcode")
    @Expose
    private String iFSCcode;
    @SerializedName("UPI")
    @Expose
    private String upi;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public ResponseBankDetails(String accountName, String accountNo, String bankName, String iFSCcode) {
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.bankName = bankName;
        this.iFSCcode = iFSCcode;
    }

    public ResponseBankDetails() {
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIFSCcode() {
        return iFSCcode;
    }

    public void setIFSCcode(String iFSCcode) {
        this.iFSCcode = iFSCcode;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
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


}
