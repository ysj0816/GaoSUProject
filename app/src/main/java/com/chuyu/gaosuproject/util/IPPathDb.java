package com.chuyu.gaosuproject.util;

import com.chuyu.gaosuproject.bean.urlip.IPPath;
import com.chuyu.gaosuproject.dao.DBManager;

import java.util.List;

/**
 * @author 杨仕俊
 * @description 用于IP的一些操作
 * Created by wo on 2017/12/12.
 */

public class IPPathDb {
    private static IPPathDb ipPathDb;
    private static DBManager<IPPath> dbManager;
    private IPPathDb(){
        dbManager = getDbManager();
    }

    public static IPPathDb getInstace() {
        if (null == ipPathDb) {
            synchronized (IPPathDb.class) {
                if (null == ipPathDb) {
                    ipPathDb = new IPPathDb();
                }
            }
        }
        return ipPathDb;
    }

    /**
     * 获取数据库操作类
     *
     * @return
     */
    public DBManager<IPPath> getDbManager() {
        if (dbManager == null) {
            dbManager = new DBManager<>(IPPath.class);
        }
        return dbManager;
    }

    /**
     * 查询有无签到请假数据
     *
     * @return
     */
    public List<IPPath> queryData() {
        List<IPPath> signDataList = dbManager.queryAllList(dbManager.getQueryBuiler());
        if (signDataList != null) {
            return signDataList;
        }
        return null;
    }

    /**
     * 查询有无签到请假数据
     *
     * @return
     */
    public void inertIPpath() {

    }

    /**
     * 更新数据
     * @param ipPath
     */
    public void updateIP(IPPath ipPath){
        dbManager.updateEntiy(ipPath);
    }
}
