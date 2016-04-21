package com.hochan.sqlite.tools;

import android.app.Application;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyApplication extends Application{

    public static int mWidthOfScreen;
    public static int mWidthOfDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenTools screenTools = ScreenTools.instance(getApplicationContext());
        mWidthOfScreen = screenTools.getScreenWidth();
        mWidthOfDialog = (int) (mWidthOfScreen*0.9);
    }
}
