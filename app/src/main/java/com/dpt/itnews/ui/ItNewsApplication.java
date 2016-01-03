package com.dpt.itnews.ui;

import android.app.Application;
import android.content.Context;
import com.dpt.itnews.data.db.CnBlogDbHelper;

/**
 * Created by dupengtao on 16/1/3.
 */
public class ItNewsApplication extends Application{

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        setupDb();
    }

    private void setupDb() {
        new CnBlogDbHelper().initDb();
    }

    public static Context getInstance() {
        return instance;
    }
}
