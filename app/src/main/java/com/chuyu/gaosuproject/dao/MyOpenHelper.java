package com.chuyu.gaosuproject.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chuyu.gaosuproject.greendao.DaoMaster;

/**
 * Created by wo on 2017/11/29.
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
