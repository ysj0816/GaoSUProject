package com.chuyu.gaosuproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.activity.MobileSignActivity;
import com.chuyu.gaosuproject.activity.SignNodeWebActivity;
import com.chuyu.gaosuproject.bean.SignBean;
import com.chuyu.gaosuproject.util.OtherUtils;

import java.util.List;

/**
 * Created by wo on 2017/8/14.
 * 地图上班的marker的Infowindow
 */

public class InfoAdapter extends BaseAdapter {

    private final Context context;
    private List<SignBean.Rows> list;
    public InfoAdapter(Context con, List<SignBean.Rows> list) {
        this.context = con;
        this.list = list;
    }

    @Override
    public int getCount() {

        return null == list ? 1 : list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHodler hodler;
            if (convertView == null) {
                hodler = new InfoAdapter.ViewHodler();
                convertView = View.inflate(context, R.layout.item_infowindow, null);
                hodler.name = (TextView) convertView.findViewById(R.id.name);
                hodler.type = (TextView) convertView.findViewById(R.id.type);
                hodler.time = (TextView) convertView.findViewById(R.id.time);
                hodler.detail = (LinearLayout) convertView.findViewById(R.id.detail);
                hodler.ll_content=(LinearLayout)convertView.findViewById(R.id.ll_content);
                hodler.ll_item=(LinearLayout)convertView.findViewById(R.id.ll_item);
                hodler.ll_title=(RelativeLayout)convertView.findViewById(R.id.ll_title);
                hodler.titleType=(TextView)convertView.findViewById(R.id.types);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            if (position==0){
                hodler.ll_title.setVisibility(View.VISIBLE);
                hodler.ll_content.setVisibility(View.GONE);
            }else {
                hodler.ll_title.setVisibility(View.GONE);
                hodler.ll_content.setVisibility(View.VISIBLE);
                //根据名字来设置左边距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //左边距
                int  leftPx= OtherUtils.dip2px(context, 19);
                int  leftPx3= OtherUtils.dip2px(context, 7);
                String name=list.get(position-1).getUserName();
                int length = name.length();
                if (length==2){
                    lp.setMargins(leftPx, 0, 0, 0);
                    hodler.type.setLayoutParams(lp);
                    hodler.name.setText(name);
                }else if(length>3){
                    String namestr=name.substring(0,3);
                    hodler.name.setText(namestr+"..");
                }else{
                    lp.setMargins(leftPx3, 0, 0, 0);
                    hodler.type.setLayoutParams(lp);
                    hodler.name.setText(name);
                }


                String type = list.get(position-1).getType();
                switch ("" + list.get(position-1).getDutyType()) {
                    case 1 + "":
                        switch (type){
                            case "0":
                                hodler.type.setText("上班(正常)");
                                break;
                            case "1":
                                hodler.type.setText("上班(入班)");
                                break;
                            case "2":
                                hodler.type.setText("上班(出差)");
                                break;
                            case "3":
                                hodler.type.setText("上班(出班)");
                                break;
                        }

                        break;
                    case 2 + "":
                        switch (type){
                            case "0":
                                hodler.type.setText("下班(正常)");
                                break;
                            case "1":
                                hodler.type.setText("下班(入班)");
                                break;
                            case "2":
                                hodler.type.setText("下班(出差)");
                                break;
                            case "3":
                                hodler.type.setText("下班(出班)");
                                break;
                        }
                        break;
                    case 3 + "":
                        switch (type){
                            case "1":
                                hodler.type.setText("请假(休假)");
                                break;
                            case "2":
                                hodler.type.setText("请假(病事假)");
                                break;

                        }

                        break;
                    default:
                        break;
                }
                hodler.time.setText(list.get(position-1).getDutyDate());

                hodler.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = list.get(position-1).getUserId();
                        Intent intent = new Intent(context, SignNodeWebActivity.class);
                        intent.putExtra("userid", userId);
                        context.startActivity(intent);

                    }
                });
            }
        return convertView;
    }

    public void setList(List<SignBean.Rows> listd) {
        list = listd;
        notifyDataSetChanged();
    }

    class ViewHodler {
        TextView name;
        TextView type;
        TextView time;
        LinearLayout detail;
        TextView detailText;
        LinearLayout ll_item;
        RelativeLayout ll_title;
        LinearLayout ll_content;
        TextView titleType;
    }
}