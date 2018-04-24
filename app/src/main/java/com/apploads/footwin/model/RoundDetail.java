package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoundDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("given_amount")
    @Expose
    private String givenAmount;
    @SerializedName("minimum_amount")
    @Expose
    private String minimumAmount;
    @SerializedName("all_in_coins")
    @Expose
    private String allInCoins;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenAmount() {
        return givenAmount;
    }

    public void setGivenAmount(String givenAmount) {
        this.givenAmount = givenAmount;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getAllInCoins() {
        return allInCoins;
    }

    public void setAllInCoins(String allInCoins) {
        this.allInCoins = allInCoins;
    }

}