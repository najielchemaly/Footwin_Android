package com.apploads.footwin.helpers;

import android.content.Context;

import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.Team;
import com.apploads.footwin.model.User;

public class StaticData {

    public static final String PREFS_NAME = "FOOTWIN_PREFS";
    public static final String PREFS_USER = "user";
    public static final String PREFS_BADGE = "badge";
    public static final String PREFS_COUNT = "predictionsCount";
    public static final String PREFS_LANG = "lang";

    public static Config config;
    public static Context context;
    public static Team favTeam;
    public static int badge;
    public static User user = new User();
}
