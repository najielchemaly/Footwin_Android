package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveRound {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("given_amount")
    @Expose
    private String givenAmount;
    @SerializedName("minimum_amount")
    @Expose
    private String minimumAmount;
    @SerializedName("prediction_coins")
    @Expose
    private String predictionCoins;
    @SerializedName("winning_coins")
    @Expose
    private String winningCoins;
    @SerializedName("exact_score_coins")
    @Expose
    private String exactScoreCoins;
    @SerializedName("all_in_coins")
    @Expose
    private String allInCoins;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPredictionCoins() {
        return predictionCoins;
    }

    public void setPredictionCoins(String predictionCoins) {
        this.predictionCoins = predictionCoins;
    }

    public String getWinningCoins() {
        return winningCoins;
    }

    public void setWinningCoins(String winningCoins) {
        this.winningCoins = winningCoins;
    }

    public String getExactScoreCoins() {
        return exactScoreCoins;
    }

    public void setExactScoreCoins(String exactScoreCoins) {
        this.exactScoreCoins = exactScoreCoins;
    }

    public String getAllInCoins() {
        return allInCoins;
    }

    public void setAllInCoins(String allInCoins) {
        this.allInCoins = allInCoins;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}