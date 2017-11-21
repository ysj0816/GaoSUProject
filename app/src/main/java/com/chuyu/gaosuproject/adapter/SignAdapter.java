package com.chuyu.gaosuproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.activity.SignActivity;

import java.io.File;
import java.util.List;

/**
 * Created by wo on 2017/8/21.
 */

public class SignAdapter extends BaseAdapter {


    private final Context context;
    private List<String> imgSrc;


    public SignAdapter(Context context, List<String> imgSrc) {
        this.context = context;
        this.imgSrc = imgSrc;
    }

    public void setImgUrl(List<String> imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public int getCount() {
        return null == imgSrc ? 0 : imgSrc.size();
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
        if (convertView == null) {
            hodler = new SignAdapter.ViewHodler();
            convertView = View.inflate(context, R.layout.item_sign_img, null);
            hodler.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        File file = new File(imgSrc.get(position));
        Glide.with(context).load(file).into(hodler.image);

        return convertView;
    }

    class ViewHodler {
        ImageView image;
    }
}
