package com.chuyu.gaosuproject.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Process;

import com.chuyu.gaosuproject.dao.DBManagerUtils;
import com.chuyu.gaosuproject.greendao.DaoMaster;
import com.chuyu.gaosuproject.greendao.DaoSession;
import com.chuyu.gaosuproject.receviver.NetCheckReceiver;
import com.chuyu.gaosuproject.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;

/**
 * @author  杨仕俊
 * @description  app的一些全局配置及初始化
 * Created by wo on 2017/7/6.
 * app统一设置
 */

public class GaoSuApplication extends Application {

    private static GaoSuApplication application;
    private DBManagerUtils managerUtils;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase sqLiteDatabase;

    public static GaoSuApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        /**
         * 工具类初始化
         */
        Utils.init(this);
        /**
         * 注册广播监听
         */
        NetCheckReceiver.registerNetworkReceiver(this);
        /**
         * OkGo调用初始化
         */
        okGoInit();
        /**
         * 数据库初始化
         */
        DBbaseInit();


    }
    /**
     * 数据库初始化
     *    通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
     *    可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
     *    注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
     *    所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
     */
    private void DBbaseInit() {
        //    mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        //    db = mHelper.getWritableDatabase();
        //    注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        //   mDaoMaster = new DaoMaster(db);
        //   mDaoSession = mDaoMaster.newSession();
        managerUtils = DBManagerUtils.getInstance(this);
        mDaoMaster = DBManagerUtils.getDaoMaster(this);
        sqLiteDatabase = DBManagerUtils.getDataBase(this);
        mDaoSession = DBManagerUtils.getDaoSession(this);
    }
    /**
     * OkGo初始化
     */
    private void okGoInit() {
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    .debug("OkGo")
                    .setConnectTimeout(30000)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setCacheMode(CacheMode.NO_CACHE)              //全局统一设置缓存模式,默认是不使用缓存
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE); //全局统一设置缓存时间,默认永不过期
        } catch (Exception e) {
            e.printStackTrace();
        }
//        //OkGO初始化
//        //构建OkHttpClient.Builder
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//        //log颜色级别，决定了log在控制台显示的颜色
//        loggingInterceptor.setColorLevel(Level.INFO);
//        builder.addInterceptor(loggingInterceptor);
//        //非必要情况，不建议使用，第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
//       // builder.addInterceptor(new ChuckInterceptor(this));
//        //全局的读取超时时间
//        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //全局的写入超时时间
//        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //全局的连接超时时间
//        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//
//        OkGo.getInstance().init(this)                       //必须调用初始化
//                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
//                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
//                .setRetryCount(3);                          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    /**
     * 当Android系统检测到内存紧张时调用
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        /**
         * 内存回收时，反注册广播
         */
        NetCheckReceiver.unRegisterNetworkReceiver(this);
        /**
         * 干掉全部进程
         */
        Process.killProcess(Process.myPid());
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return sqLiteDatabase;
    }
}
