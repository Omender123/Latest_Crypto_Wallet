package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterBody {
    @SerializedName("filters")
    @Expose
    private Filters filters;
    @SerializedName("sort")
    @Expose
    private Sort sort;

    public FilterBody() {
    }

    public FilterBody(Filters filters, Sort sort) {
        super();
        this.filters = filters;
        this.sort = sort;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }


public static class Filters {

    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("lowAmount")
    @Expose
    private String lowAmount;
    @SerializedName("highAmount")
    @Expose
    private String highAmount;

    /**
     * No args constructor for use in serialization
     *
     */

    public Filters(String startDate, String endDate, String lowAmount, String highAmount) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLowAmount() {
        return lowAmount;
    }

    public void setLowAmount(String lowAmount) {
        this.lowAmount = lowAmount;
    }

    public String getHighAmount() {
        return highAmount;
    }

    public void setHighAmount(String highAmount) {
        this.highAmount = highAmount;
    }

}

public static class Sort {

    @SerializedName("amount")
    @Expose
    private String amount;

    /**
     * No args constructor for use in serialization
     */

    public Sort(String amount) {
        super();
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
}
