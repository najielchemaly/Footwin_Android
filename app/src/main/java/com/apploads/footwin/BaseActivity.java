package com.apploads.footwin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected final static int NULL_CONTENT_VIEW = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int contentViewId = getContentViewId();
            if (contentViewId != NULL_CONTENT_VIEW) {
                setContentView(getContentViewId());
            }
            doOnCreate();
        } catch (Exception e) {
            Log.e("Crash", "Error in: " + e.getMessage());
        }
    }

    /**
     * Retune the content view of the activity
     *
     * @return
     */
    public abstract int getContentViewId();

    public abstract void doOnCreate() throws Exception;

    /**
     * Change the color of the window status bar
     *
     * @param colorId
     */
    protected void setWindowStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(colorId));
        }
    }

    /**
     * Return the view by type
     *
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T> T _findViewById(int viewId) {
        return (T) findViewById(viewId);
    }
}
