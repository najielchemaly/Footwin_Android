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
    @SerializedName("winning_team")
    @Expose
    private String winningTeam;
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
    @SerializedName("is_confirmed")
    @Expose
    private String is_confirmed;
    @SerializedName("prediction_winning_team")
    @Expose
    private String predictionWinningTeam;
    @SerializedName("prediction_home_score")
    @Expose
    private String predictionHomeScore;
    @SerializedName("prediction_away_score")
    @Expose
    private String predictionAwayScore;

    private boolean homeToWin;
    private boolean awayToWin;
    private boolean draw;

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isHomeToWin() {
        return homeToWin;
    }

    public void setHomeToWin(boolean homeToWin) {
        this.homeToWin = homeToWin;
    }

    public boolean isAwayToWin() {
        return awayToWin;
    }

    public void setAwayToWin(boolean awayToWin) {
        this.awayToWin = awayToWin;
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

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
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

    public String getIsConfirmed() {
        return is_confirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.is_confirmed = isConfirmed;
    }

    public String getPredictionWinningTeam() {
        return predictionWinningTeam;
    }

    public void setPredictionWinningTeam(String predictionWinningTeam) {
        this.predictionWinningTeam = predictionWinningTeam;
    }

    public String getPredictionHomeScore() {
        return predictionHomeScore;
    }

    public void setPredictionHomeScore(String predictionHomeScore) {
        this.predictionHomeScore = predictionHomeScore;
    }

    public String getPredictionAwayScore() {
        return predictionAwayScore;
    }

    public void setPredictionAwayScore(String predictionAwayScore) {
        this.predictionAwayScore = predictionAwayScore;
    }

    public String getIs_confirmed() {
        return is_confirmed;
    }

    public void setIs_confirmed(String is_confirmed) {
        this.is_confirmed = is_confirmed;
    }
}