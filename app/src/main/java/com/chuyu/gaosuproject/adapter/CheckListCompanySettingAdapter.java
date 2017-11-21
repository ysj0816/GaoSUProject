package com.chuyu.gaosuproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.bean.Book;
import com.chuyu.gaosuproject.bean.CheckListFillBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CheckListCompanySettingAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private CheckListCompanySettingAdapter.OnItemClickListener setitemClickListener;
    ViewHolder holder;
    // 标记用户当前选择的那一个发音(就是用来标记item的)
    private int index = -1;
    private List<CheckListFillBean.CheckListFillDate> Date;
    CheckListFillBean.CheckListFillDate date;

    public CheckListCompanySettingAdapter(Context context, CheckListCompanySettingAdapter.OnItemClickListener setitemClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.setitemClickListener = setitemClickListener;

    }

    // 选中当前选项时，让其他选项不被选中，一会会被调用的，这里逻辑也很清晰。
    public void select(int position) {

        if (!Date.get(position).isSelected()) {
            Date.get(position).setSelected(true);
            for (int i = 0; i < Date.size(); i++) {
                if (i != position) {
                    Date.get(i).setSelected(false);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setList(List<CheckListFillBean.CheckListFillDate> Date) {
        this.Date = Date;

    }

    @Override
    public int getCount() {
        return Date.size();
    }

    @Override
    public Object getItem(int position) {
        return Date.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dialog_list, null);
            holder.radioBtn = (RadioButton) convertView
                    .findViewById(R.id.rbtn_item_dialog_accent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //这里是让listview和数据对接上，很清晰看出
        date = (CheckListFillBean.CheckListFillDate) getItem(position);
        holder.radioBtn.setChecked(date.isSelected());
        holder.radioBtn.setText(date.getCheckproject());
//这里就是监听radiobutton啦，索引的位置跟那个select的结合，才能完美跟radiobutton结合，让点击跟位置跟数据结合以及只选一个
        holder.radioBtn
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            if (index == position) {
                                Log.i("test", "触发radioBtn");
                            } else {
                                index = position;
                                setitemClickListener.OnItem(index);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

        if (index == position) {// 选中的条目和当前的条目是否相等
            holder.radioBtn.setChecked(true);
        } else {
            holder.radioBtn.setChecked(false);
        }
        return convertView;
    }

    //使用了内部类，标记我们的item中的radiobutton
    class ViewHolder {
        RadioButton radioBtn;
    }

    public interface OnItemClickListener {
        void OnItem(int position);
    }
}
