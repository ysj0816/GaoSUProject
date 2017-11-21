package com.chuyu.gaosuproject.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.util.OtherUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wo on 2017/8/16.
 */

public class RightTopWindow extends PopupWindow {

    private Activity context;
    private View conentView;
    public LookLinsenter lookLinsenter;

    public RightTopWindow(final Activity context, final LookLinsenter lookLinsenter) {
        this.lookLinsenter=lookLinsenter;
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.more_popup_dialog, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        int width= OtherUtils.dip2px(context,165);

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        //点击事件
        LinearLayout llone = (LinearLayout) conentView
                .findViewById(R.id.llone);
        LinearLayout llall = (LinearLayout) conentView
                .findViewById(R.id.llall);
        LinearLayout llNo = (LinearLayout) conentView
                .findViewById(R.id.llNo);
        llone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookLinsenter.clickPosition(0);
                RightTopWindow.this.dismiss();
            }
        });
        llall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookLinsenter.clickPosition(1);
                RightTopWindow.this.dismiss();
            }
        });
        llNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookLinsenter.clickPosition(2);
                RightTopWindow.this.dismiss();
            }
        });

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            int par=parent.getLayoutParams().width / 2;
            //Log.i("test","偏移："+par);-410
            this.showAsDropDown(parent, par, 0);
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = 0.7f;
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗
            context.getWindow().setAttributes(lp);
            //此处作为点击PopupWindow之外的地方取消掉背景变暗
            this.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                    lp.alpha = 1f;
                    context.getWindow().setAttributes(lp);
                }
            });
        } else {
            this.dismiss();
        }
    }



    /**
     * 接口回调
     */
   public interface LookLinsenter {
        void clickPosition(int i);
    }


}
