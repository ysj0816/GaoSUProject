package com.chuyu.gaosuproject.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wo on 2017/7/7.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflate = null;

    public CommonAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mList = datas;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeData(List<T> newList) {
        this.mList = newList;
        if (this.mList == null) {
            this.mList = new ArrayList<T>();
        }
        notifyDataSetChanged();
    }

    public void addData(List<T> newList) {
        ((ArrayList<T>) this.mList).addAll((ArrayList<T>) newList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder = CommonViewHolder.get(mContext, convertView, parent, layout(), position);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getmConvertView();
    }

    /**
     * @Description 核心，查找控件，控件赋值等
     * @param viewHolder
     * @param result
     * @Author 尹银川(yinyc@p2m.com.cn)
     * @DATE 2015-6-15 上午9:30:06
     */
    public abstract void convert(CommonViewHolder viewHolder, T result, int position);

    /**
     * @Description 传入适配器布局文件
     * @return
     * @Author 尹银川(yinyc@p2m.com.cn)
     * @DATE 2015-6-15 上午9:29:53
     */
    public abstract int layout();

    public List<T> getmList() {
        return mList;
    }
}
