package com.chuyu.gaosuproject.util.upload;

import android.util.Log;

import com.chuyu.gaosuproject.bean.daobean.SignAndLeaveData;
import com.chuyu.gaosuproject.dao.DBManager;

import java.util.List;

/**
 * @author 杨仕俊
 * @description  请假和签到的数据操作
 * Created by wo on 2017/12/8.
 */

public class SignLeaveDao {

    private static SignLeaveDao signLeaveDao;
    private static DBManager<SignAndLeaveData> dbManager;
    private SignLeaveDao(){
         dbManager = getDbManager();
    }

    public static SignLeaveDao getInstace() {
        if (null == signLeaveDao) {
            synchronized (SignLeaveDao.class) {
                if (null == signLeaveDao) {
                    signLeaveDao = new SignLeaveDao();
                }
            }
        }
        return signLeaveDao;
    }

    /**
     * 获取数据库操作类
     *
     * @return
     */
    public DBManager<SignAndLeaveData> getDbManager() {
        if (dbManager == null) {
            dbManager = new DBManager<>(SignAndLeaveData.class);
        }
        return dbManager;
    }

    /**
     * 查询有无签到请假数据
     *
     * @return
     */
    public  List<SignAndLeaveData> queryData() {
        List<SignAndLeaveData> signDataList = dbManager.queryAllList(dbManager.getQueryBuiler());
        if (signDataList != null) {
            return signDataList;
        }
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param id
     */
    public void deleteById(Long id) {
        Log.i("test", "删除：" + id);
        dbManager.deleteByID(id);
    }

    /**
     * 删除所有数据
     */
    public void deleteALL(){
        dbManager.deleteAll();
    }
}
