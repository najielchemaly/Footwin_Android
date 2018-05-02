package com.apploads.footwin.helpers;

import com.apploads.footwin.model.Config;
import com.apploads.footwin.model.Team;
import com.apploads.footwin.model.User;

public class StaticData {

    public static final String PREFS_NAME = "FOOTWIN_PREFS";
    public static final String PREFS_USER = "user";
    public static final String PREFS_LANG = "lang";

    public static Config config;
    public static Team favTeam;
    public static User user = new User();
}
