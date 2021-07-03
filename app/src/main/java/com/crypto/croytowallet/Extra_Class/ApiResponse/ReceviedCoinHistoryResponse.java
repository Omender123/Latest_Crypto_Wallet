package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceviedCoinHistoryResponse {
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("value")
    @Expose
    private String  value;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
