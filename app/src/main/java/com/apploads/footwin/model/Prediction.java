package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("winning_team")
    @Expose
    private String winningTeam;
    @SerializedName("home_score")
    @Expose
    private String homeScore;
    @SerializedName("away_score")
    @Expose
    private String awayScore;
    @SerializedName("selected_team")
    @Expose
    private String selectedTeam;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("home_id")
    @Expose
    private String homeId;
    @SerializedName("home_name")
    @Expose
    private String homeName;
    @SerializedName("home_flag")
    @Expose
    private String homeFlag;
    @SerializedName("away_id")
    @Expose
    private String awayId;
    @SerializedName("away_name")
    @Expose
    private String awayName;
    @SerializedName("away_flag")
    @Expose
    private String awayFlag;
    @SerializedName("actual_home_score")
    @Expose
    private Object actualHomeScore;
    @SerializedName("actual_away_score")
    @Expose
    private Object actualAwayScore;
    @SerializedName("actual_winning_team")
    @Expose
    private String actualWinningTeam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
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

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
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

    public String getAwayId() {
        return awayId;
    }

    public void setAwayId(String awayId) {
        this.awayId = awayId;
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

    public Object getActualHomeScore() {
        return actualHomeScore;
    }

    public void setActualHomeScore(Object actualHomeScore) {
        this.actualHomeScore = actualHomeScore;
    }

    public Object getActualAwayScore() {
        return actualAwayScore;
    }

    public void setActualAwayScore(Object actualAwayScore) {
        this.actualAwayScore = actualAwayScore;
    }

    public String getActualWinningTeam() {
        return actualWinningTeam;
    }

    public void setActualWinningTeam(String actualWinningTeam) {
        this.actualWinningTeam = actualWinningTeam;
    }

}