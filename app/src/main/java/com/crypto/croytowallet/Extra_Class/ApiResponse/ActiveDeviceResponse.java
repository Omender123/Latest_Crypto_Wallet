package com.crypto.croytowallet.Extra_Class.ApiResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveDeviceResponse {

   /* @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }*/
   @SerializedName("result")
   @Expose
    private Result[] results;

    public Result[] getResults() {
        return results;
    }

    public class Result {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("ipV4")
        @Expose
        private String ipV4;
        @SerializedName("osName")
        @Expose
        private String osName;
        @SerializedName("jwt")
        @Expose
        private String jwt;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("fcmToken")
        @Expose
        private String fcmToken;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIpV4() {
            return ipV4;
        }

        public void setIpV4(String ipV4) {
            this.ipV4 = ipV4;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getJwt() {
            return jwt;
        }

        public void setJwt(String jwt) {
            this.jwt = jwt;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
        }
    }
    }
