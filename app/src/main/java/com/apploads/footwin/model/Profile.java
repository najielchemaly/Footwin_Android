package com.apploads.footwin.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("round_detail")
    @Expose
    private RoundDetail roundDetail;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public RoundDetail getRoundDetail() {
        return roundDetail;
    }

    public void setRoundDetail(RoundDetail roundDetail) {
        this.roundDetail = roundDetail;
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