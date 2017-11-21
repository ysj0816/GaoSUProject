package com.chuyu.gaosuproject.presenter;

import android.util.Log;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.SignsModel;
import com.chuyu.gaosuproject.model.interfacemodel.ISignsModel;
import com.chuyu.gaosuproject.view.ISignsView;

import java.io.File;

/**
 * Created by wo on 2017/7/13.
 */

public class SignPresenter extends BasePresenter<ISignsView> {
    //提交
    public void submitData(String UserId,String DutyDate,String DutyType,String Location,String Lng,String lat,
        String Type,String rebark,File file){
        if (isViewAttached()){

        }else {
            return;
        }
        final ISignsView view = getView();
        //显示等待窗口
        //view.showWaiting();
        SignsModel.getInstance().SignSubmit(UserId, DutyDate, DutyType, Location,Lng, lat,
                 Type, rebark, file,new ISignsModel.SignSubmitListener() {
            @Override
            public void submitSuccess() {
                view.closeWaitting();
                view.showSubSuccess();
            }

            @Override
            public void submitFailed() {
                view.closeWaitting();
                view.showSubmitFaile();
            }

            @Override
            public void textEmpty() {
                view.closeWaitting();
                view.showMesg("为空");
            }

            @Override
            public void submitExpretion(String msg) {
                view.closeWaitting();
                view.showMesg(msg);
            }
        });

    }
    //判断是否重复提交
    public void isSign(String userid,String DutyDate,int DutyType){
        if (isViewAttached()){

        }else {
            return;
        }
        final ISignsView view = getView();
        //显示等待窗口
        view.showWaiting();
        SignsModel.getInstance().rePreSign(userid, DutyDate, DutyType, new ISignsModel.SignListener() {
            @Override
            public void Success(String meg) {
               //view.closeWaitting();

                view.isReprSign(true,"成功");
            }

            @Override
            public void Failed(String msg) {
                view.closeWaitting();
                view.isReprSign(false,msg);
               // view.showSubmitFaile();
            }

            @Override
            public void NetWorKException(String msg) {
                view.closeWaitting();
                view.showSubmitFaile();
                //view.showMesg(msg);
            }
        });

    }
}
