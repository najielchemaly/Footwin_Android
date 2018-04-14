package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config {

    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    @SerializedName("active_round")
    @Expose
    private ActiveRound activeRound;

    @SerializedName("base_url")
    @Expose
    private String baseUrl;

    @SerializedName("media_url")
    @Expose
    private String mediaUrl;

    @SerializedName("is_review")
    @Expose
    private Boolean isReview;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public ActiveRound getActiveRound() {
        return activeRound;
    }

    public void setActiveRound(ActiveRound activeRound) {
        this.activeRound = activeRound;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Boolean getIsReview() {
        return isReview;
    }

    public void setIsReview(Boolean isReview) {
        this.isReview = isReview;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}