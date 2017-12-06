package com.chuyu.gaosuproject.presenter;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.DailyCheckListFillModel;
import com.chuyu.gaosuproject.model.interfacemodel.IDailyCheckListFillModel;
import com.chuyu.gaosuproject.view.IDailyCheckListFillView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26. 日常检查表填报
 */

public class DailyCheckListFillPresenter extends BasePresenter<IDailyCheckListFillView>{
    //提交检查表数据
    public void sumbitFill(String checkunit, String checkproject, String userid, String checkresult, String content
          , String deductpoint, String checkid, List<File> file){
        if(isViewAttached()){

        }else {
            return;
        }
        final IDailyCheckListFillView view = getView();
        //此处作为正在提交状态
        view.showWaiting();
        DailyCheckListFillModel.getInsance().submitcheckfill(checkunit,checkproject,userid,checkresult,content
           ,deductpoint,checkid,file, new IDailyCheckListFillModel.FillListener() {
                    @Override
                    public void submitSuccess() {
                        view.colseWaiting();
                        view.fillSuccess();
                    }

                    @Override
                    public void submitFaile() {
                        view.colseWaiting();
                        view.fillFaile();
                    }

                    @Override
                    public void showExpretion(String msg) {
                        view.colseWaiting();
                        view.showExpretion(msg);
                    }
                });
    }

}
