package com.crypto.croytowallet.Extra_Class.ApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PublicKeyResponse {


    @SerializedName("publicKey")
    @Expose
    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
