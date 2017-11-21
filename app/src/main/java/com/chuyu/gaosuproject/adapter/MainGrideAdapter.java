package com.chuyu.gaosuproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.adapter.base.CommonAdapter;

import java.util.HashMap;

/**
 * Created by wo on 2017/7/7.
 * grideview adapter
 */

public class MainGrideAdapter extends BaseAdapter{


    private final Context context;
    private final int[] imgSrc;
    private final String[] itemTitle;


    public MainGrideAdapter(Context context, int[] imgSrc,String []itemTitle){
        this.context=context;
       this.imgSrc=imgSrc;
        this.itemTitle=itemTitle;
    }

    @Override
    public int getCount() {
        return imgSrc.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler hodler;
        if (convertView==null){
            hodler=new ViewHodler();
            convertView=View.inflate(context, R.layout.item_main_gride,null);
            hodler.image = (ImageView) convertView.findViewById(R.id.image);
            hodler.text=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(hodler);
        }else {
            hodler= (ViewHodler) convertView.getTag();
        }
        hodler.image.setImageResource(imgSrc[position]);
        hodler.text.setText(itemTitle[position]);
        return convertView;
    }
    class ViewHodler{
        ImageView image;
        TextView text;
    }
}
