package com.apploads.footwin.common;

public class UserContextHolder {

    private static UserContext userContext;

    public static UserContext getUserContext() {
        return userContext;
    }

    public static void setUserContext(UserContext userContext) {
        UserContextHolder.userContext = userContext;
    }

    public static void clear() {
        userContext = null;
    }
}