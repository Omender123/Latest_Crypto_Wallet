package com.crypto.croytowallet.Model;

public class SwapRespoinseModel {
    String TransId,Status;

    public SwapRespoinseModel(String transId, String status) {
        TransId = transId;
        Status = status;
    }

    public SwapRespoinseModel() {
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
