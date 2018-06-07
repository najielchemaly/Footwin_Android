package com.apploads.footwin.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("countries")
    @Expose
    private List<Country> countries = null;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;
    @SerializedName("active_round")
    @Expose
    private ActiveRound activeRound;
    @SerializedName("active_reward")
    @Expose
    private ActiveReward activeReward;
    @SerializedName("winning_user")
    @Expose
    private User winningUser;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("news_url")
    @Expose
    private String newsUrl;
    @SerializedName("admin_url")
    @Expose
    private String adminUrl;
    @SerializedName("is_review")
    @Expose
    private Boolean isReview;
    @SerializedName("is_app_active")
    @Expose
    private Boolean isAppActive;
    @SerializedName("is_iapready")
    @Expose
    private Boolean isIAPReady;
    @SerializedName("tutorial_text")
    @Expose
    private String tutorialText;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("version_android")
    @Expose
    private String version;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

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

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public Boolean getIsReview() {
        return isReview;
    }

    public void setIsReview(Boolean isReview) {
        this.isReview = isReview;
    }

    public Boolean getIsAppActive() {
        return isAppActive;
    }

    public void setIsAppActive(Boolean isAppActive) {
        this.isAppActive = isAppActive;
    }

    public String getTutorialText() {
        return tutorialText;
    }

    public void setTutorialText(String tutorialText) {
        this.tutorialText = tutorialText;
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

    public ActiveReward getActiveReward() {
        return activeReward;
    }

    public void setActiveReward(ActiveReward activeReward) {
        this.activeReward = activeReward;
    }

    public Boolean getReview() {
        return isReview;
    }

    public void setReview(Boolean review) {
        isReview = review;
    }

    public Boolean getAppActive() {
        return isAppActive;
    }

    public void setAppActive(Boolean appActive) {
        isAppActive = appActive;
    }

    public User getWinningUser() {
        return winningUser;
    }

    public void setWinningUser(User winningUser) {
        this.winningUser = winningUser;
    }

    public Boolean getIsIAPReady() {
        return isIAPReady;
    }

    public void setIsIAPReady(Boolean IAPReady) {
        isIAPReady = IAPReady;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;

    }
}
