package com.crypto.croytowallet.SharedPrefernce;

public class UserData {
    private String id;
    private String name, email, mobile, username, mnemonic, Referral_code, transaction_Pin, token,ETH,BTC,LITE,XRP,EMAIL2FA;





    public UserData(String id, String name, String email, String mobile, String username, String mnemonic, String referral_code,
                    String transaction_Pin, String token, String ETH, String BTC, String LITE, String XRP,
                     String EMAIL2FA) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
        this.mnemonic = mnemonic;
        Referral_code = referral_code;
        this.transaction_Pin = transaction_Pin;
        this.token = token;
        this.ETH = ETH;
        this.BTC = BTC;
        this.LITE = LITE;
        this.XRP = XRP;

        this.EMAIL2FA = EMAIL2FA;
    }

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getReferral_code() {
        return Referral_code;
    }

    public void setReferral_code(String referral_code) {
        Referral_code = referral_code;
    }

    public String getTransaction_Pin() {
        return transaction_Pin;
    }

    public void setTransaction_Pin(String transaction_Pin) {
        this.transaction_Pin = transaction_Pin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getETH() {
        return ETH;
    }

    public void setETH(String ETH) {
        this.ETH = ETH;
    }

    public String getBTC() {
        return BTC;
    }

    public void setBTC(String BTC) {
        this.BTC = BTC;
    }

    public String getLITE() {
        return LITE;
    }

    public void setLITE(String LITE) {
        this.LITE = LITE;
    }

    public String getXRP() {
        return XRP;
    }

    public void setXRP(String XRP) {
        this.XRP = XRP;
    }


    public String getEMAIL2FA() {
        return EMAIL2FA;
    }

    public void setEMAIL2FA(String EMAIL2FA) {
        this.EMAIL2FA = EMAIL2FA;
    }
}