package com.chuyu.gaosuproject.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chuyu.gaosuproject.greendao.DaoMaster;
import com.chuyu.gaosuproject.greendao.DaoSession;

/**
 * @author 杨仕俊
 * @description  数据库管理相关工具类
 * Created by wo on 2017/11/29.
 */

public class DBManagerUtils {

    /**
     * 是否加密
     */
    public static final boolean IS_ENCRYPTED=true;
    /**
     * 数据库的名字
     */
    private static final String DB_NAME="gaosu.db";
    /**
     * content
     */
    private Context context;
    /**
     * 数据库
     */
    private static DBManagerUtils mDbManagerUtils;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSeesion;
    /**
     * 单例  私有化构造方法
     * @param mContenxt
     */
    private DBManagerUtils(Context mContenxt){
        this.context=mContenxt;
        /**
         * 数据库初始化
         */
        mDevOpenHelper=new DaoMaster.DevOpenHelper(mContenxt,DB_NAME);
        getDaoMaster(mContenxt);
        getDaoSession(mContenxt);
    }

    /**
     * 单例模式   同步，
     * @param mContenxt
     * @return
     */
    public static DBManagerUtils getInstance(Context mContenxt){
        if (null==mDbManagerUtils){
            synchronized (DBManagerUtils.class){
                if (null==mDbManagerUtils){
                    mDbManagerUtils=new DBManagerUtils(mContenxt);
                }
            }
        }
        return mDbManagerUtils;
    }

    /**
     * 获取DaoMaster
     * 检查有无数据库 没有就构造方法
     * @param mContext
     * @return
     */
    public static DaoMaster getDaoMaster(Context mContext){
        if (null==mDaoMaster){
            synchronized (DBManagerUtils.class){
                if (null==mDaoMaster){
                    SQLiteDatabase database = mDevOpenHelper.getReadableDatabase();
                    mDaoMaster=new DaoMaster(database);
                }
            }
        }
        return mDaoMaster;
    }

    /**
     *
     * @param mContext
     * @return
     */
    public static DaoSession getDaoSession(Context mContext){
        if (null==mDaoSeesion){
            synchronized (DBManagerUtils.class){
                if (null==mDaoSeesion){
                    mDaoSeesion=getDaoMaster(mContext).newSession();
                }
            }
        }
        return mDaoSeesion;
    }

    /**
     * 获取数据库
     * @param mContext
     * @return
     */
    public static SQLiteDatabase getDataBase(Context mContext){
        if (null==mDevOpenHelper){
            getInstance(mContext);
            Log.i("test","mDevOpenHelper为空");
        }
        return mDevOpenHelper.getReadableDatabase();
    }


}
