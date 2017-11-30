package com.chuyu.gaosuproject.dao;

/**
 * Created by wo on 2017/11/29.
 */

public class DBManager<T> extends DBbaseOperation<T,Long> {
    /**
     * 构成方法 获取数据操作的dao
     *
     * @param tClass
     */
    public DBManager(Class<T> tClass) {
        super(tClass);
    }


}
