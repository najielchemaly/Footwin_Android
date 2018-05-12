package com.apploads.footwin.helpers.utils;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.model.User;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AppUtils {

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * for showing a native alertview with custom title and message
     *
     * @param context do not user getapplication context , user ActivityName.this
     * @param title   title of the dialogue
     * @param message message of the dialogue
     */
    public static void showAlert(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void saveUser(Context context, User user) {
        StaticData.user = user;

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(StaticData.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(StaticData.PREFS_USER, json);
        editor.apply();
    }

    public static int getBadge(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(StaticData.PREFS_NAME, Context.MODE_PRIVATE); //1

        int badge;
        badge = settings.getInt(StaticData.PREFS_BADGE, 0);
        return badge;
    }

    public static void updateBadge(Context context, int badge) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(StaticData.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt(StaticData.PREFS_BADGE, badge);
        editor.apply();
    }

    public static void setFirstLaunch(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(StaticData.PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("firstLaunch", true);
        editor.apply();
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(StaticData.PREFS_NAME, context.MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("firstLaunch", false);
        return isFirstLaunch;
    }

    public static void startCountAnimation(final TextView textView, int fromNumber, int toNumber, long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(fromNumber, toNumber);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }
}
