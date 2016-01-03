package com.dpt.itnews.data.db;

import android.database.sqlite.SQLiteDatabase;
import com.dpt.itnews.ui.ItNewsApplication;

/**
 * Created by dupengtao on 16/1/3.
 */
public class CnBlogDbHelper {

    private DaoSession daoSession;

    public void initDb(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(ItNewsApplication.getInstance(),"news.db",null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public NewsDao getDaoSession() {
        if(daoSession ==null){
            initDb();
        }
        return daoSession.getNewsDao();
    }
}
