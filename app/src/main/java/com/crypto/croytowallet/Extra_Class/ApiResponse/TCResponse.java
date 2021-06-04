package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TCResponse {

    @SerializedName("TCs")
    @Expose
    private TCs tCs;

    public TCs getTCs() {
        return tCs;
    }

    public void setTCs(TCs tCs) {
        this.tCs = tCs;
    }

    public class TCs {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("body")
        @Expose
        private String body;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
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
    }
