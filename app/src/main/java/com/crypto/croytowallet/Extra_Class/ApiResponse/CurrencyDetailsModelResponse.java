package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrencyDetailsModelResponse {

    @SerializedName("ERC")
    @Expose
    private Erc erc;

    public Erc getErc() {
        return erc;
    }

    public void setErc(Erc erc) {
        this.erc = erc;
    }
    public class Erc {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("aboutCurrency")
        @Expose
        private String aboutCurrency;
        @SerializedName("currencyName")
        @Expose
        private String currencyName;
        @SerializedName("blog")
        @Expose
        private List<Blog> blog = null;
        @SerializedName("video")
        @Expose
        private List<Video> video = null;
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

        public String getAboutCurrency() {
            return aboutCurrency;
        }

        public void setAboutCurrency(String aboutCurrency) {
            this.aboutCurrency = aboutCurrency;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public List<Blog> getBlog() {
            return blog;
        }

        public void setBlog(List<Blog> blog) {
            this.blog = blog;
        }

        public List<Video> getVideo() {
            return video;
        }

        public void setVideo(List<Video> video) {
            this.video = video;
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
        public class Blog {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("link")
            @Expose
            private String link;
            @SerializedName("image")
            @Expose
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
        public class Video {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("videoId")
            @Expose
            private String videoId;
            @SerializedName("videoEmbed")
            @Expose
            private String videoEmbed;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("image")
            @Expose
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

            public String getVideoEmbed() {
                return videoEmbed;
            }

            public void setVideoEmbed(String videoEmbed) {
                this.videoEmbed = videoEmbed;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

        }

        }
}
