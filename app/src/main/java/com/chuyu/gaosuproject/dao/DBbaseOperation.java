package com.chuyu.gaosuproject.dao;

import android.database.sqlite.SQLiteDatabase;

import com.chuyu.gaosuproject.application.GaoSuApplication;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.apihint.Internal;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

/**
 * @author 杨仕俊
 * @description  数据库的基本操作 包括增删改查等，具体操作表可继承此类
 * @date 2017/11/29 11:21
 * Created by wo on 2017/11/29.
 */

public class DBbaseOperation<T,K> {
    public final AbstractDao<T,K> dao;
    /**
     * 构成方法 获取数据操作的dao
     * @param tClass
     */
    public DBbaseOperation(Class<T> tClass){
        dao= (AbstractDao<T, K>) GaoSuApplication.getInstance().getDaoSession().getDao(tClass);
    }

    /**
     * 数据库
     * @return
     */
    public SQLiteDatabase getDatabase() {
        return GaoSuApplication.getInstance().getDb();
    }

    public QueryBuilder<T> getQueryBuiler(){
        return dao.queryBuilder();
    }

    public AbstractDao<T,K> getDao(){
        return dao;
    }

    /**
     * 插入单条数据
     * @param entity
     * @return
     */
    public boolean insertObj(T entity){
        boolean isinsert=true;
        try {
            dao.insert(entity);
            isinsert=true;
        }catch (Exception e){
            e.printStackTrace();
            isinsert=false;
        }
        return isinsert;
    }

    /**
     * 插入多条数据
     * @param iterable 迭代器
     * @return
     */
    public boolean insertInTx(Iterable<T> iterable){
        boolean isInsert=true;
        try {
            dao.insertInTx(iterable);
        }catch (Exception e){
            e.printStackTrace();
            isInsert=false;
        }
        return isInsert;
    }

    /**
     * 删除所有
     * @return
     */
    public boolean deleteAll(){
        boolean isDelete=true;
        try {
            dao.deleteAll();
        }catch (Exception e){
            e.printStackTrace();
            isDelete=false;
        }
        return isDelete;
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    public boolean deleteByID(K id){
        boolean iskeyDele=true;
        try {
            dao.deleteByKey(id);
            iskeyDele=true;
        }catch (Exception e){
            e.printStackTrace();
            iskeyDele=false;
        }
        return iskeyDele;
    }

    /**
     * 删除所有查询到的
     * @param queryBuilder
     * @return
     */
    public boolean deleteOBj(QueryBuilder<T> queryBuilder){
        boolean isobj=true;
        try{
            queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e){
            e.printStackTrace();
            isobj=false;
        }
        return isobj;
    }

    /**
     * 查询所有的数据
     * @param queryBuilder
     * @return
     */
    public synchronized List<T> queryAllList(QueryBuilder<T> queryBuilder){
        try {
            List<T> list = queryBuilder.list();
            if (null==list){
                //返回一个空集合
                return Collections.emptyList();
            }else {
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *更新一条数据
     */
    public void updateEntiy(T t){
        dao.update(t);
    }

    /**
     * 删除所有数据
     */
    public void delelteAll(){
        getDao().deleteAll();
    }


    /**
     * 查询所有
     * @return
     */
    public List<T> findAll() {
        try {
            return dao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据库退出
     * @param qb
     * @return
     */
    public boolean exists(QueryBuilder<T> qb) {
        try {
            return qb.buildCount().count() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
