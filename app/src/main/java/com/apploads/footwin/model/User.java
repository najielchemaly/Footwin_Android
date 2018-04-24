package com.apploads.footwin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("facebook_token")
    @Expose
    private Object facebookToken;
    @SerializedName("google_id")
    @Expose
    private String googleId;
    @SerializedName("google_token")
    @Expose
    private String googleToken;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("coins")
    @Expose
    private String coins;
    @SerializedName("winning_coins")
    @Expose
    private String winningCoins;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("expected_winning_team")
    @Expose
    private String expectedWinningTeam;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("favorite_team")
    @Expose
    private String favoriteTeam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(Object facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getWinningCoins() {
        return winningCoins;
    }

    public void setWinningCoins(String winningCoins) {
        this.winningCoins = winningCoins;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getExpectedWinningTeam() {
        return expectedWinningTeam;
    }

    public void setExpectedWinningTeam(String expectedWinningTeam) {
        this.expectedWinningTeam = expectedWinningTeam;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }

}