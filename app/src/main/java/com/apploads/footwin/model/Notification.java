package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("is_read")
    @Expose
    private String isRead;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("home_score")
    @Expose
    private String homeScore;
    @SerializedName("home_name")
    @Expose
    private String homeName;
    @SerializedName("home_flag")
    @Expose
    private String homeFlag;
    @SerializedName("away_score")
    @Expose
    private String awayScore;
    @SerializedName("away_name")
    @Expose
    private String awayName;
    @SerializedName("away_flag")
    @Expose
    private String awayFlag;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
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

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
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
}
