package com.crypto.croytowallet.Extra_Class.ApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GetNewCoinRespinse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("current_price")
    @Expose
    private Double currentPrice;
    @SerializedName("market_cap")
    @Expose
    private Long marketCap;
    @SerializedName("market_cap_rank")
    @Expose
    private Long marketCapRank;
    @SerializedName("fully_diluted_valuation")
    @Expose
    private Object fullyDilutedValuation;
    @SerializedName("total_volume")
    @Expose
    private Long totalVolume;
    @SerializedName("high_24h")
    @Expose
    private Double high24h;
    @SerializedName("low_24h")
    @Expose
    private Double low24h;
    @SerializedName("price_change_24h")
    @Expose
    private Double priceChange24h;
    @SerializedName("price_change_percentage_24h")
    @Expose
    private Double priceChangePercentage24h;
    @SerializedName("market_cap_change_24h")
    @Expose
    private String marketCapChange24h;
    @SerializedName("market_cap_change_percentage_24h")
    @Expose
    private Double marketCapChangePercentage24h;
    @SerializedName("circulating_supply")
    @Expose
    private Double circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Object totalSupply;
    @SerializedName("max_supply")
    @Expose
    private Object maxSupply;
    @SerializedName("ath")
    @Expose
    private Double ath;
    @SerializedName("ath_change_percentage")
    @Expose
    private Double athChangePercentage;
    @SerializedName("ath_date")
    @Expose
    private String athDate;
    @SerializedName("atl")
    @Expose
    private Double atl;
    @SerializedName("atl_change_percentage")
    @Expose
    private Double atlChangePercentage;
    @SerializedName("atl_date")
    @Expose
    private String atlDate;
    @SerializedName("roi")
    @Expose
    private Roi roi;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Long getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(Long marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public Object getFullyDilutedValuation() {
        return fullyDilutedValuation;
    }

    public void setFullyDilutedValuation(Object fullyDilutedValuation) {
        this.fullyDilutedValuation = fullyDilutedValuation;
    }

    public Long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(Double high24h) {
        this.high24h = high24h;
    }

    public Double getLow24h() {
        return low24h;
    }

    public void setLow24h(Double low24h) {
        this.low24h = low24h;
    }

    public Double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(Double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public Double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(Double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public String  getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(String  marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public Double getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(Double marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public Double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Object getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Object totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Object getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Object maxSupply) {
        this.maxSupply = maxSupply;
    }

    public Double getAth() {
        return ath;
    }

    public void setAth(Double ath) {
        this.ath = ath;
    }

    public Double getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(Double athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public Double getAtl() {
        return atl;
    }

    public void setAtl(Double atl) {
        this.atl = atl;
    }

    public Double getAtlChangePercentage() {
        return atlChangePercentage;
    }

    public void setAtlChangePercentage(Double atlChangePercentage) {
        this.atlChangePercentage = atlChangePercentage;
    }

    public String getAtlDate() {
        return atlDate;
    }

    public void setAtlDate(String atlDate) {
        this.atlDate = atlDate;
    }

    public Roi getRoi() {
        return roi;
    }

    public void setRoi(Roi roi) {
        this.roi = roi;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public class Roi {

        @SerializedName("times")
        @Expose
        private Double times;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("percentage")
        @Expose
        private Double percentage;

        public Double getTimes() {
            return times;
        }

        public void setTimes(Double times) {
            this.times = times;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }
}
