package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("token")
    @Expose
    private String token;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class Result {

        @SerializedName("balance")
        @Expose
        private Balance balance;
        @SerializedName("securityEnable")
        @Expose
        private SecurityEnable securityEnable;
        @SerializedName("preference")
        @Expose
        private Preference preference;
        @SerializedName("passwordExpired")
        @Expose
        private PasswordExpired passwordExpired;
        @SerializedName("kycVerified")
        @Expose
        private String kycVerified;
        @SerializedName("emailVerify")
        @Expose
        private Boolean emailVerify;
        @SerializedName("childrenUsers")
        @Expose
        private List<String> childrenUsers = null;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("reward")
        @Expose
        private String  reward;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("countryCode")
        @Expose
        private String countryCode;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("transactionPin")
        @Expose
        private String transactionPin;
        @SerializedName("myReferalcode")
        @Expose
        private String myReferalcode;
        @SerializedName("ethAddress")
        @Expose
        private String ethAddress;
        @SerializedName("ethPrivateKey")
        @Expose
        private String ethPrivateKey;
        @SerializedName("bitcoinAddress")
        @Expose
        private String bitcoinAddress;
        @SerializedName("bitcoinPrivateKey")
        @Expose
        private String bitcoinPrivateKey;
        @SerializedName("litecoinAddress")
        @Expose
        private String litecoinAddress;
        @SerializedName("litecoinPrivateKey")
        @Expose
        private String litecoinPrivateKey;
        @SerializedName("xrpAddress")
        @Expose
        private String xrpAddress;
        @SerializedName("xrpPrivateKey")
        @Expose
        private String xrpPrivateKey;
        @SerializedName("tronAddress")
        @Expose
        private String tronAddress;
        @SerializedName("tronHex")
        @Expose
        private String tronHex;
        @SerializedName("tronPrivateKey")
        @Expose
        private String tronPrivateKey;
        @SerializedName("passwordUpdatedDate")
        @Expose
        private String passwordUpdatedDate;
        @SerializedName("kyc")
        @Expose
        private List<Object> kyc = null;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("mnemonic")
        @Expose
        private String mnemonic;
        @SerializedName("earnedBonus")
        @Expose
        private Integer earnedBonus;
        @SerializedName("lastLogin")
        @Expose
        private String lastLogin;
        @SerializedName("otpDate")
        @Expose
        private String otpDate;

        public Balance getBalance() {
            return balance;
        }

        public void setBalance(Balance balance) {
            this.balance = balance;
        }

        public SecurityEnable getSecurityEnable() {
            return securityEnable;
        }

        public void setSecurityEnable(SecurityEnable securityEnable) {
            this.securityEnable = securityEnable;
        }

        public Preference getPreference() {
            return preference;
        }

        public void setPreference(Preference preference) {
            this.preference = preference;
        }

        public PasswordExpired getPasswordExpired() {
            return passwordExpired;
        }

        public void setPasswordExpired(PasswordExpired passwordExpired) {
            this.passwordExpired = passwordExpired;
        }

        public String getKycVerified() {
            return kycVerified;
        }

        public void setKycVerified(String kycVerified) {
            this.kycVerified = kycVerified;
        }

        public Boolean getEmailVerify() {
            return emailVerify;
        }

        public void setEmailVerify(Boolean emailVerify) {
            this.emailVerify = emailVerify;
        }

        public List<String> getChildrenUsers() {
            return childrenUsers;
        }

        public void setChildrenUsers(List<String> childrenUsers) {
            this.childrenUsers = childrenUsers;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String  getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTransactionPin() {
            return transactionPin;
        }

        public void setTransactionPin(String transactionPin) {
            this.transactionPin = transactionPin;
        }

        public String getMyReferalcode() {
            return myReferalcode;
        }

        public void setMyReferalcode(String myReferalcode) {
            this.myReferalcode = myReferalcode;
        }

        public String getEthAddress() {
            return ethAddress;
        }

        public void setEthAddress(String ethAddress) {
            this.ethAddress = ethAddress;
        }

        public String getEthPrivateKey() {
            return ethPrivateKey;
        }

        public void setEthPrivateKey(String ethPrivateKey) {
            this.ethPrivateKey = ethPrivateKey;
        }

        public String getBitcoinAddress() {
            return bitcoinAddress;
        }

        public void setBitcoinAddress(String bitcoinAddress) {
            this.bitcoinAddress = bitcoinAddress;
        }

        public String getBitcoinPrivateKey() {
            return bitcoinPrivateKey;
        }

        public void setBitcoinPrivateKey(String bitcoinPrivateKey) {
            this.bitcoinPrivateKey = bitcoinPrivateKey;
        }

        public String getLitecoinAddress() {
            return litecoinAddress;
        }

        public void setLitecoinAddress(String litecoinAddress) {
            this.litecoinAddress = litecoinAddress;
        }

        public String getLitecoinPrivateKey() {
            return litecoinPrivateKey;
        }

        public void setLitecoinPrivateKey(String litecoinPrivateKey) {
            this.litecoinPrivateKey = litecoinPrivateKey;
        }

        public String getXrpAddress() {
            return xrpAddress;
        }

        public void setXrpAddress(String xrpAddress) {
            this.xrpAddress = xrpAddress;
        }

        public String getXrpPrivateKey() {
            return xrpPrivateKey;
        }

        public void setXrpPrivateKey(String xrpPrivateKey) {
            this.xrpPrivateKey = xrpPrivateKey;
        }

        public String getTronAddress() {
            return tronAddress;
        }

        public void setTronAddress(String tronAddress) {
            this.tronAddress = tronAddress;
        }

        public String getTronHex() {
            return tronHex;
        }

        public void setTronHex(String tronHex) {
            this.tronHex = tronHex;
        }

        public String getTronPrivateKey() {
            return tronPrivateKey;
        }

        public void setTronPrivateKey(String tronPrivateKey) {
            this.tronPrivateKey = tronPrivateKey;
        }

        public String getPasswordUpdatedDate() {
            return passwordUpdatedDate;
        }

        public void setPasswordUpdatedDate(String passwordUpdatedDate) {
            this.passwordUpdatedDate = passwordUpdatedDate;
        }

        public List<Object> getKyc() {
            return kyc;
        }

        public void setKyc(List<Object> kyc) {
            this.kyc = kyc;
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

        public String getMnemonic() {
            return mnemonic;
        }

        public void setMnemonic(String mnemonic) {
            this.mnemonic = mnemonic;
        }

        public Integer getEarnedBonus() {
            return earnedBonus;
        }

        public void setEarnedBonus(Integer earnedBonus) {
            this.earnedBonus = earnedBonus;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getOtpDate() {
            return otpDate;
        }

        public void setOtpDate(String otpDate) {
            this.otpDate = otpDate;
        }

        public class Balance {

            @SerializedName("airDropIMT")
            @Expose
            private String airDropIMT;

            public String getAirDropIMT() {
                return airDropIMT;
            }

            public void setAirDropIMT(String airDropIMT) {
                this.airDropIMT = airDropIMT;
            }


        }

        public class PasswordExpired {

            @SerializedName("value")
            @Expose
            private Boolean value;
            @SerializedName("days")
            @Expose
            private Integer days;

            public Boolean getValue() {
                return value;
            }

            public void setValue(Boolean value) {
                this.value = value;
            }

            public Integer getDays() {
                return days;
            }

            public void setDays(Integer days) {
                this.days = days;
            }

        }
        public class Preference {

            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("language")
            @Expose
            private String language;

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

        }

        public class SecurityEnable {

            @SerializedName("google2fa")
            @Expose
            private Boolean google2fa;
            @SerializedName("email2fa")
            @Expose
            private Boolean email2fa;

            public Boolean getGoogle2fa() {
                return google2fa;
            }

            public void setGoogle2fa(Boolean google2fa) {
                this.google2fa = google2fa;
            }

            public Boolean getEmail2fa() {
                return email2fa;
            }

            public void setEmail2fa(Boolean email2fa) {
                this.email2fa = email2fa;
            }

        }

    }
}
