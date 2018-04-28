package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("home_id")
    @Expose
    private String homeId;
    @SerializedName("away_id")
    @Expose
    private String awayId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("home_score")
    @Expose
    private String homeScore;
    @SerializedName("away_score")
    @Expose
    private String awayScore;
    @SerializedName("round")
    @Expose
    private String round;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("home_name")
    @Expose
    private String homeName;
    @SerializedName("home_flag")
    @Expose
    private String homeFlag;
    @SerializedName("away_name")
    @Expose
    private String awayName;
    @SerializedName("away_flag")
    @Expose
    private String awayFlag;
    @SerializedName("prediction_coins")
    @Expose
    private String predictionCoins;
    @SerializedName("winning_coins")
    @Expose
    private String winningCoins;
    @SerializedName("exact_score_coins")
    @Expose
    private String exactScoreCoins;

    private boolean isHomeToWin;
    private boolean isAwayToWin;

    private boolean isConfirm;

    public boolean isHomeToWin() {
        return isHomeToWin;
    }

    public void setHomeToWin(boolean homeToWin) {
        isHomeToWin = homeToWin;
    }

    public boolean isAwayToWin() {
        return isAwayToWin;
    }

    public void setAwayToWin(boolean awayToWin) {
        isAwayToWin = awayToWin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getAwayId() {
        return awayId;
    }

    public void setAwayId(String awayId) {
        this.awayId = awayId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeFlag() {
        return homeFlag;
    }

    public void setHomeFlag(String homeFlag) {
        this.homeFlag = homeFlag;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getAwayFlag() {
        return awayFlag;
    }

    public void setAwayFlag(String awayFlag) {
        this.awayFlag = awayFlag;
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

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }
}