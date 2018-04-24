package com.apploads.footwin.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaderboardResponse {

    @SerializedName("leaderboard")
    @Expose
    private List<Leaderboard> leaderboard = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Leaderboard> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<Leaderboard> leaderboard) {
        this.leaderboard = leaderboard;
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