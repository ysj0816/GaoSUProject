package com.chuyu.gaosuproject.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chuyu.gaosuproject.R;


/**
 * Created by wo on 2017/7/12.
 */

public class RadioButtonDialog extends Dialog {

    private Context context;
    private String title;     //这里定义个title，一会可以看到是指向上面xml文件的控件title的，也就是我们可以通过这个进行动态修改title

    //可以看到两个构造器，想自定义样式的就用第二个啦
    public RadioButtonDialog(Context context) {
        super(context);
        this.context = context;
    }

    public RadioButtonDialog(Context context, int theme , SelectLinstener selectLinstener) {
        super(context, theme);
        this.context = context;
        this.selectLinstener=selectLinstener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }



    //控件的声明
    RadioButton rbtn_Rule;
    RadioButton rbtn_Ruban;
    RadioButton rbtn_Evection;
    RadioButton rbtn_OutWork;

    private void init() {
        //以view的方式引入，然后回调activity方法，setContentView，实现自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign, null);
        setContentView(view);
        //radiobutton的初始化
        RadioGroup groupBroadcast = (RadioGroup) view.findViewById(R.id.groupBroadcast);
        rbtn_Rule = (RadioButton) view.findViewById(R.id.rbtn_Rule);
        rbtn_Ruban = (RadioButton) view.findViewById(R.id.rbtn_Ruban);
        rbtn_Evection = (RadioButton) view.findViewById(R.id.rbtn_Evection);
        rbtn_OutWork = (RadioButton) view.findViewById(R.id.rbtn_OutWork);

        groupBroadcast.setOnCheckedChangeListener(listener);
        //设置dialog大小，这里是一个小赠送，模块好的控件大小设置
        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);//设置对话框位置
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(params);

    }

    //监听接口
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rbtn_Rule.getId()) {
                selectLinstener.setSelectType(0,"正常");
            } else if (checkedId == rbtn_Ruban.getId()) {
                selectLinstener.setSelectType(1,"入班");
            } else if (checkedId == rbtn_Evection.getId()) {
                selectLinstener.setSelectType(2,"出差");
            } else if (checkedId == rbtn_OutWork.getId()) {
                selectLinstener.setSelectType(3,"出班");
            }
        }
    };

    public interface SelectLinstener{
        void setSelectType(int type,String selectType);
    }
    private SelectLinstener selectLinstener;
}


